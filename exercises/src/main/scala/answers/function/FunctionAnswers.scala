package answers.function

import java.time.Duration

import cats.Eval

import scala.annotation.tailrec
import scala.collection.mutable
import scala.concurrent.{Await, Future}
import scala.math.BigDecimal.RoundingMode
import scala.math.BigDecimal.RoundingMode.RoundingMode
import scala.util.Random

object FunctionAnswers {

  //////////////////////////////////////////////////////
  // 1. Functions as input (aka higher order functions)
  //////////////////////////////////////////////////////

  def selectDigits(s: String): String =
    s.filter(c => c.isDigit)

  def secret(s: String): String =
    s.map(_ => '*')

  val isValidUsernameCharacter: Char => Boolean =
    c => c.isLetterOrDigit || c == '-' || c == '_'

  def isValidUsername(username: String): Boolean =
    username.forall(isValidUsernameCharacter)

  case class Point(x: Int, y: Int) {
    def isPositive: Boolean =
      x >= 0 && y >= 0

    def isEven: Boolean =
      (x % 2 == 0) && (y % 2 == 0)

    def forAll(predicate: Int => Boolean): Boolean =
      predicate(x) && predicate(y)

    def isPositiveForAll: Boolean =
      forAll(_ >= 0)

    def isEvenForAll: Boolean =
      forAll(_ % 2 == 0)
  }

  //////////////////////////////////////////////////
  // 2. functions as output (aka curried functions)
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

  ////////////////////////////
  // 3. parametric functions
  ////////////////////////////

  case class Pair[A](first: A, second: A) {
    def swap: Pair[A] =
      Pair(second, first)

    def map[B](f: A => B): Pair[B] =
      Pair(f(first), f(second))

    def forAll(predicate: A => Boolean): Boolean =
      predicate(first) && predicate(second)

    def zipWith[B, C](other: Pair[B], combine: (A, B) => C): Pair[C] =
      Pair(combine(first, other.first), combine(second, other.second))

    def zipWithCurried[B, C](other: Pair[B])(combine: (A, B) => C): Pair[C] =
      zipWith(other, combine)
  }

  val names: Pair[String] = Pair("John", "Elisabeth")
  val ages: Pair[Int]     = Pair(32, 46)
  case class User(name: String, age: Int)

  val users: Pair[User] =
    names.zipWithCurried(ages)(User)

  val longerThan5: Boolean =
    names.map(_.length).forAll(_ >= 5)

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

  def splitFoldMap[A, B](xs: List[List[A]])(f: A => B)(z: B, combine: (B, B) => B): B =
    foldMap(xs)(foldMap(_)(f)(z, combine))(z, combine)

  def splitParFoldMap[A, B](xs: List[List[A]])(f: A => B)(z: B, combine: (B, B) => B): Future[B] = {
    import scala.concurrent.ExecutionContext.Implicits._
    Future
      .traverse(xs)(subList => Future { foldMap(subList)(f)(z, combine) })
      .map(foldMap(_)(identity)(z, combine))
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

  val size                            = 10000000
  val largeList: List[Int]            = List.fill(size)(Random.nextInt())
  def chunks(n: Int): List[List[Int]] = largeList.grouped(size / n).toList
  val chunked: List[List[Int]]        = chunks(10)

  time(foldLeft(largeList, 0L)(_ + _))
  time(foldMap(largeList)(x => x: Long)(0L, _ + _))
  time(splitFoldMap(chunked)(x => x: Long)(0, _ + _))
  time(Await.result(splitParFoldMap(chunked)(x => x: Long)(0, _ + _), scala.concurrent.duration.Duration.Inf))

}
