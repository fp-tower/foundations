package answers.function

import exercises.function.HttpClientBuilder
import exercises.function.HttpClientBuilder._

import scala.annotation.tailrec
import scala.collection.mutable
import scala.concurrent.duration._

object FunctionAnswers {

  ////////////////////////////
  // 1. first class functions
  ////////////////////////////

  def isEven(x: Int): Boolean =
    x % 2 == 0

  val isEvenVal: Int => Boolean =
    (x: Int) => x % 2 == 0

  val isEvenDefToVal: Int => Boolean =
    isEven _ // or just isEven

  def keepEvenNumbers(xs: List[Int]): List[Int] =
    xs.filter(isEven)

  def keepNumbersSmallThan(xs: List[Int])(threshold: Int): List[Int] =
    xs.filter(_ <= threshold)

  sealed trait Direction
  case object Up   extends Direction
  case object Down extends Direction

  def move(direction: Direction)(x: Int): Int =
    direction match {
      case Up   => x + 1
      case Down => x - 1
    }

  ////////////////////////////
  // 2. polymorphic functions
  ////////////////////////////

  def identity[A](x: A): A = x

  def const[A, B](a: A)(b: B): A = a

  case class User(name: String, age: Int)

  def updateAge(f: Int => Int): List[User] =
    List(User("John", 26), User("Lisa", 5)).map { p =>
      p.copy(age = f(p.age))
    }

  def setUsersAge(value: Int): List[User] =
    updateAge(const(value))

  val getUsers: List[User] =
    updateAge(identity)

  def andThen[A, B, C](f: A => B, g: B => C): A => C =
    a => g(f(a))

  def compose[A, B, C](f: B => C, g: A => B): A => C =
    a => f(g(a))

  val inc: Int => Int    = x => x + 1
  val double: Int => Int = x => 2 * x

  val doubleInc: Int => Int = andThen(double, inc)

  val incDouble: Int => Int = compose(double, inc)

  val default: HttpClientBuilder = HttpClientBuilder.default("localhost", 8080)

  val clientBuilder1: HttpClientBuilder = default
    .withTimeout(10.seconds)
    .withFollowRedirect(true)
    .withMaxParallelRequest(3)

  val clientBuilder2: HttpClientBuilder =
    (withTimeout(10.seconds) compose
      withFollowRedirect(true) compose
      withMaxParallelRequest(3)).apply(default)

  ///////////////////////////
  // 3. Recursion & Laziness
  ///////////////////////////

  def sumList(xs: List[Int]): Int = {
    var sum = 0
    for (x <- xs) sum += x
    sum
  }

  def sumList2(xs: List[Int]): Int = {
    @tailrec
    def _sumList(ys: List[Int], acc: Int): Int =
      ys match {
        case Nil    => acc
        case h :: t => _sumList(t, acc + h)
      }

    _sumList(xs, 0)
  }

  def sumList3(xs: List[Int]): Int =
    foldLeft(xs, 0)(_ + _)

  def mkString(xs: List[Char]): String = {
    var str = ""
    for (x <- xs) str += x
    str
  }

  def mkString2(xs: List[Char]): String =
    foldLeft(xs, "")(_ + _)

  @tailrec
  def foldLeft[A, B](xs: List[A], z: B)(f: (B, A) => B): B =
    xs match {
      case Nil    => z
      case h :: t => foldLeft(t, f(z, h))(f)
    }

  def reverse[A](xs: List[A]): List[A] =
    foldLeft(xs, List.empty[A])(_.::(_))

  def multiply(xs: List[Int]): Int = foldLeft(xs, 1)(_ * _)

  def filter[A](xs: List[A])(p: A => Boolean): List[A] =
    foldLeft(xs, List.empty[A])((acc, a) => if (p(a)) a :: acc else acc).reverse

  def foldRight[A, B](xs: List[A], z: B)(f: (A, => B) => B): B =
    xs match {
      case Nil    => z
      case h :: t => f(h, foldRight(t, z)(f))
    }

  @tailrec
  def find[A](fa: List[A])(p: A => Boolean): Option[A] =
    fa match {
      case Nil     => None
      case x :: xs => if (p(x)) Some(x) else find(xs)(p)
    }

  @tailrec
  def forAll(fa: List[Boolean]): Boolean =
    fa match {
      case Nil        => true
      case false :: _ => false
      case true :: xs => forAll(xs)
    }

  def find2[A](xs: List[A])(p: A => Boolean): Option[A] =
    foldRight(xs, Option.empty[A])((a, rest) => if (p(a)) Some(a) else rest)

  def forAll2(xs: List[Boolean]): Boolean =
    foldRight(xs, true) {
      case (false, _)   => false
      case (true, rest) => rest
    }

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
