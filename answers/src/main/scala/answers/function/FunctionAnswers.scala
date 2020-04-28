package answers.function

import java.time.Duration
import java.util.concurrent.atomic.{AtomicInteger, AtomicReference}
import java.util.concurrent.{CountDownLatch, Executors, ThreadFactory}

import cats.Eval

import scala.annotation.tailrec
import scala.collection.mutable
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.io.Source
import scala.math.BigDecimal.RoundingMode
import scala.math.BigDecimal.RoundingMode.RoundingMode

object FunctionAnswers {

  //////////////////////////////////////////////////
  // 3. functions as output (aka curried functions)
  //////////////////////////////////////////////////

  def add(x: Int)(y: Int): Int = x + y

  val increment: Int => Int = add(1)
  val decrement: Int => Int = add(-1)

  def formatDouble(roundingMode: RoundingMode, digits: Int, number: Double): String =
    BigDecimal(number)
      .setScale(digits, roundingMode)
      .toDouble
      .toString

  val formatDoubleCurried: RoundingMode => Int => Double => String =
    roundingMode => digits => number => formatDouble(roundingMode, digits, number)

  val formatDoubleCurried2: RoundingMode => Int => Double => String =
    (formatDouble _).curried

  val format2Ceiling: Double => String =
    formatDoubleCurried(RoundingMode.CEILING)(2)

  /////////////////
  // 4. Iteration
  /////////////////

  def sum(xs: List[Int]): Int = {
    var sum = 0
    for (x <- xs) sum += x
    sum
  }

  def mkString(xs: List[Char]): String = {
    var str = ""
    for (x <- xs) str += x
    str
  }

  def wordCount(xs: List[String]): Map[String, Int] = {
    var letters = Map.empty[String, Int]
    for (x <- xs) letters = addWord(letters, x)
    letters
  }

  def addWord(state: Map[String, Int], word: String): Map[String, Int] =
    state.updatedWith(word) {
      case None    => Some(1)
      case Some(n) => Some(n + 1)
    }

  def foldLeft[A, B](fa: List[A], b: B)(f: (B, A) => B): B = {
    var acc = b
    for (a <- fa) acc = f(acc, a)
    acc
  }

  def sumFoldLeft(xs: List[Int]): Int =
    foldLeft(xs, 0)(_ + _)

  def mkStringFoldLeft(xs: List[Char]): String =
    foldLeft(xs, "")(_ + _)

  def wordCountFoldLeft(xs: List[String]): Map[String, Int] =
    foldLeft(xs, Map.empty[String, Int])(addWord)

  def foldMap[A, B](xs: List[A])(f: A => B)(z: B, combine: (B, B) => B): B =
    foldLeft(xs, z)((acc, a) => combine(acc, f(a)))

  def partitionFoldMap[A, B](partitions: List[List[A]])(f: A => B)(default: B, combine: (B, B) => B): B =
    foldMap(partitions)(foldMap(_)(f)(default, combine))(default, combine)

  def parallelPartitionFoldMap[A, B](partitions: List[List[A]])(f: A => B)(default: B, combine: (B, B) => B): B = {
    implicit val ec: ExecutionContext = fixedSize(partitions.size, "parallelPartitionFoldMap")
    Await.result(
      Future
        .traverse(partitions)(chunk => Future { foldMap(chunk)(f)(default, combine) })
        .map(foldMap(_)(identity)(default, combine)),
      scala.concurrent.duration.Duration.Inf
    )
  }

  def parallelPartitionFoldMapCommutative[A, B](partitions: List[List[A]])(f: A => B)(default: B,
                                                                                      combine: (B, B) => B): B = {
    val numberOfPartitions   = partitions.size
    val countDownLatch       = new CountDownLatch(numberOfPartitions)
    val state                = new AtomicReference(default)
    val ec: ExecutionContext = fixedSize(numberOfPartitions, "parallelPartitionFoldMapCommutative")

    partitions.foreach(
      chunk =>
        Future { foldMap(chunk)(f)(default, combine) }(ec).foreach { res =>
          modify(state)(combine(_, res))
          countDownLatch.countDown()
        }(ec)
    )

    countDownLatch.await()
    state.get()
  }

  def fixedSize(threads: Int, prefix: String): ExecutionContext =
    ExecutionContext.fromExecutor(Executors.newFixedThreadPool(threads, threadFactory(prefix, daemon = true)))

  def threadFactory(prefix: String, daemon: Boolean): ThreadFactory =
    new ThreadFactory {
      val ctr = new AtomicInteger(0)
      def newThread(r: Runnable): Thread = {
        val back = new Thread(r)
        back.setName(prefix + "-" + ctr.getAndIncrement())
        back.setDaemon(daemon)
        back
      }
    }

  def modify[A](ref: AtomicReference[A])(f: A => A): Unit = {
    @tailrec
    def spin: Unit = {
      val current = ref.get
      val updated = f(current)
      if (!ref.compareAndSet(current, updated)) spin
      else ()
    }
    spin
  }

  /////////////////
  // 5. Recursion
  /////////////////

  def sumRecursive(xs: List[Int]): Int = {
    @tailrec
    def _sumRecursive(ys: List[Int], acc: Int): Int =
      ys match {
        case Nil          => acc
        case head :: tail => _sumRecursive(tail, head + acc)
      }
    _sumRecursive(xs, 0)
  }

  def wordCountRecursive(xs: List[String]): Map[String, Int] = {
    def _wordCountRecursive(ys: List[String], acc: Map[String, Int]): Map[String, Int] =
      xs match {
        case Nil          => acc
        case head :: tail => _wordCountRecursive(tail, addWord(acc, head))
      }

    _wordCountRecursive(xs, Map.empty)
  }

  @tailrec
  def foldLeftRecursive[A, B](xs: List[A], z: B)(f: (B, A) => B): B =
    xs match {
      case Nil    => z
      case h :: t => foldLeftRecursive(t, f(z, h))(f)
    }

  ///////////////////////////
  // 6. Laziness
  ///////////////////////////

  @tailrec
  def forAll(fa: List[Boolean]): Boolean =
    fa match {
      case Nil        => true
      case false :: _ => false
      case true :: xs => forAll(xs)
    }

  @tailrec
  def find[A](fa: List[A])(p: A => Boolean): Option[A] =
    fa match {
      case Nil     => None
      case x :: xs => if (p(x)) Some(x) else find(xs)(p)
    }

  def foldRight[A, B](xs: List[A], b: B)(f: (A, => B) => B): B =
    xs match {
      case Nil    => b
      case h :: t => f(h, foldRight(t, b)(f))
    }

  def foldRightCats[A, B](xs: List[A], b: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
    Eval.defer(
      xs match {
        case Nil    => b
        case h :: t => f(h, foldRightCats(t, b)(f))
      }
    )

  def forAllFoldRight(xs: List[Boolean]): Boolean =
    foldRightCats(xs, Eval.now(true)) {
      case (false, _)   => Eval.now(false)
      case (true, rest) => rest
    }.value

  def findFoldRight[A](xs: List[A])(predicate: A => Boolean): Option[A] =
    foldRightCats(xs, Eval.now(Option.empty[A]))((a, rest) => if (predicate(a)) Eval.now(Some(a)) else rest).value

  def headOption[A](xs: List[A]): Option[A] =
    foldRight(xs, Option.empty[A])((a, _) => Some(a))

  def multiply(xs: List[Int]): Int =
    foldLeft(xs, 1)(_ * _)

  def min(xs: List[Int]): Option[Int] =
    xs match {
      case Nil          => None
      case head :: tail => Some(foldLeft(tail, head)(_ min _))
    }

  def filter[A](xs: List[A])(predicate: A => Boolean): List[A] =
    foldLeft(xs, List.empty[A])((acc, a) => if (predicate(a)) a :: acc else acc).reverse

  ////////////////////////
  // 5. Memoization
  ////////////////////////

  def memoize[A, B](f: A => B): A => B = {
    val cache = mutable.Map.empty[A, B]
    (a: A) =>
      {
        cache.get(a) match {
          case Some(b) => b // cache succeeds
          case None =>
            val b = f(a)
            cache.update(a, b) // update cache
            b
        }
      }
  }
}

object FunctionAnswersApp extends App {
  import FunctionAnswers._

  def time[R](block: => R): R = {
    val t0          = System.nanoTime()
    val result      = block
    val t1          = System.nanoTime()
    val duration    = Duration.ofNanos(t1 - t0)
    val durationStr = String.format("%02d:%02d", duration.toSecondsPart, duration.toMillisPart)
    println(s"Elapsed time: $durationStr")
    result
  }

  def chunks[A](n: Int, xs: List[A]): List[List[A]] = xs.grouped(xs.size / n).toList

  val files        = List.fill(100)("long-text.txt")
  val chunkedFiles = chunks(10, files)

  def wordCountByFile(filename: String): Map[String, Int] =
    Source
      .fromResource(filename)
      .getLines()
      .map(wordCount)
      .foldLeft(Map.empty[String, Int])(combineMaps)

  def wordCount(text: String): Map[String, Int] =
    text
      .split(" ")
      .toList
      .foldLeft(Map.empty[String, Int])((acc, word) => acc.updated(word, acc.getOrElse(word, 0) + 1))

  def combineMaps[K](m1: Map[K, Int], m2: Map[K, Int]): Map[K, Int] =
    m2.foldLeft(m1) {
      case (acc, (s, n)) => acc.updated(s, (acc.getOrElse(s, 0) + n))
    }

  time(foldMap(files)(wordCountByFile)(Map.empty, combineMaps))
  time(parallelPartitionFoldMap(chunks(8, files))(wordCountByFile)(Map.empty, combineMaps))
  time(parallelPartitionFoldMapCommutative(chunks(8, files))(wordCountByFile)(Map.empty, combineMaps))

}
