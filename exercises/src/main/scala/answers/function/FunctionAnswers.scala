package answers.function

import exercises.function.HttpClientBuilder
import exercises.function.HttpClientBuilder._

import scala.annotation.tailrec
import scala.collection.mutable
import scala.concurrent.duration._

object FunctionAnswers {

  //////////////////////////////////////////////////////
  // 1. Functions as input (aka higher order functions)
  //////////////////////////////////////////////////////

  def keepLetters(s: String): String =
    s.filter(c => c.isLetter)

  def secret(s: String): String =
    s.map(_ => '*')

  def isValidUsernameCharacter(c: Char): Boolean =
    c.isLetterOrDigit || c == '-' || c == '_'

  lazy val _isValidUsernameCharacter: Char => Boolean =
    c => c.isLetterOrDigit || c == '-' || c == '_'

  def isValidUsername(username: String): Boolean =
    username.forall(isValidUsernameCharacter)

  //////////////////////////////////////////////////
  // 2. functions as output (aka curried functions)
  //////////////////////////////////////////////////

  def add(x: Int)(y: Int): Int = x + y

  lazy val increment: Int => Int = add(1)
  lazy val decrement: Int => Int = add(-1)

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

  def mapOption[A, B](option: Option[A], f: A => B): Option[B] =
    option match {
      case None    => None
      case Some(a) => Some(f(a))
    }

  def identity[A](x: A): A = x

  def const[A, B](a: A)(b: B): A = a

  def setOption[A, B](option: Option[A])(value: B): Option[B] =
    option.map(const(value))

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

  def forAll2(xs: List[Boolean]): Boolean =
    foldRight(xs, true)(_ && _)

  def headOption[A](xs: List[A]): Option[A] =
    foldRight(xs, Option.empty[A])((a, _) => Some(a))

  def find2[A](xs: List[A])(p: A => Boolean): Option[A] =
    foldRight(xs, Option.empty[A])((a, rest) => if (p(a)) Some(a) else rest)

  def min(xs: List[Int]): Option[Int] =
    xs match {
      case Nil          => None
      case head :: tail => Some(foldLeft(tail, head)(_ min _))
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
