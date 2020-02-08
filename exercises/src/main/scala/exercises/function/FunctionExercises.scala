package exercises.function

import exercises.function.HttpClientBuilder
import exercises.function.HttpClientBuilder._

import scala.annotation.tailrec
import scala.concurrent.duration._
import scala.util.Random

// you can run and print things here
object FunctionApp extends App {
  import FunctionExercises._

  println("Hello World!")
}

object FunctionExercises {

  /////////////////////////////////////////////////////
  // 1. Functions as input (aka higher order functions)
  /////////////////////////////////////////////////////

  // 1a. Implement `keepLetters` which iterates over a String and only keep the characters that are letters.
  // such as keepLetters("123foo0-!Bar~+3") == "fooBar"
  // Note: You can use `filter` method from `String`, also check out the API of Char
  def keepLetters(s: String): String = ???

  // 1b. Implement `secret` which transforms all characters in a String to '*'
  // such as secret("Welcome123") == "**********"
  // Note: You can use `map` method from `String`
  def secret(s: String): String = ???

  // 1c. Implement `isValidUsernameCharacter` which checks if a character is suitable for a username.
  // We accept:
  // * lower and upper case letters
  // * digits
  // * special characters: '-' and '_'
  // For example, isValidUsernameCharacter('3') == true
  // but          isValidUsernameCharacter('^') == false
  def isValidUsernameCharacter(c: Char): Boolean = ???

  // 1d. Now, we are going to experiment with the val function syntax.
  // Implement `_isValidUsernameCharacter` which behaves exactly like `isValidUsernameCharacter`.
  // Note: You can remove the lazy keyword as soon you implement `_isValidUsernameCharacter`.
  // It is only required to avoid tests throwing an exception.
  lazy val _isValidUsernameCharacter: Char => Boolean = ???

  // 1e. Implements `isValidUsername` which checks that all the characters in a String are valid
  // such as isValidUsername("john-doe") == true
  // but     isValidUsername("*john*") == false
  // Can you re-use `isValidUsernameCharacter` or `_isValidUsernameCharacter` or both?
  def isValidUsername(username: String): Boolean = ???

  /////////////////////////////////////////////////
  // 2. functions as output (aka curried function)
  /////////////////////////////////////////////////

  // 1f. Implement `move` which increases or decreases a number based on a `Direction` (enumeration)
  // such as move(Up)(5) == 6
  // but     move(Down)(5) == 4
  sealed trait Direction
  case object Up   extends Direction
  case object Down extends Direction

  def move(direction: Direction)(x: Int): Int =
    ???

  // 1g. Implement `increment` and `decrement` by reusing `move`
  // such as increment(10) == 11
  // such as decrement(10) == 9
  lazy val increment: Int => Int = ???

  lazy val decrement: Int => Int = ???

  ////////////////////////////
  // 2. polymorphic functions
  ////////////////////////////

  val zero: Pair[Int]        = Pair(0, 0)
  val fullName: Pair[String] = Pair("John", "Doe")

  case class Pair[A](first: A, second: A) {
    // 2a. Implement `map` which applies a function to `first` and `second`
    // such as Pair("John", "Doe").map(_.length) == Pair(4,3)
    def map[B](f: A => B): Pair[B] =
      ???
  }

  // 2b. Implement `mapOption` which applies a function to an Option if it is a `Some`.
  // Use patter matching on Option (see `sizeOption`) instead of using Option API
  // such as mapOption(Some(2), isEven)    == Some(true)
  //         mapOption(Some(2), increment) == Some(3)
  // but     mapOption(Option.empty[Int], increment) == None
  // Note: Option is a enumeration with two constructors `Some` and `None`.
  def mapOption[A, B](option: Option[A], f: A => B): Option[B] =
    ???

  def sizeOption[A](option: Option[A]): Int =
    option match {
      case None    => 0
      case Some(a) => 1
    }

  // 2c. What is the difference between `mapOption` and `mapOption2`?
  // Which one should you use?
  def mapOption2[A, B](option: Option[A])(f: A => B): Option[B] =
    mapOption(option, f)

  // 2d. Implement `identity` which returns its input unchanged
  // such as identity(1) == 1
  //         identity("foo") == "foo"
  def identity[A](x: A): A = ???

  // 2e. Implement `identityVal` a function which behaves like `identity` but it is a val instead of a def.
  lazy val identityVal = ???

  // 2f. Implement `const` which returns its first input unchanged and discards its second input
  // such as const(5)("foo") == 5
  // For example, you can use const in conjunction with `map` to set the values in a List or String:
  // List(1,2,3).map(const(0)) == List(0,0,0)
  // "FooBar86".map(const(*))  == "********"
  def const[A, B](a: A)(b: B): A = ???

  // 2g. Implement `andThen` and `compose` which pipes the result of one function to the input of another function
  // such as compose(isEven, increment)(10) == false
  // and     andThen(increment, isEven)(10) == false
  def andThen[A, B, C](f: A => B, g: B => C): A => C = ???

  def compose[A, B, C](f: B => C, g: A => B): A => C = ???

  // 2h. Implement `doubleInc` using `inc`, `double` with `compose` or `andThen`
  // such as `doubleInc` is equivalent to the maths function: f(x) = (2 * x) + 1
  val inc: Int => Int    = x => x + 1
  val double: Int => Int = x => 2 * x

  lazy val doubleInc: Int => Int = ???

  // 2i. Implement `incDouble` using `inc`, `double` with `compose` or `andThen`
  // such as `incDouble` is equivalent to the maths function: f(x) = 2 * (x + 1)
  lazy val incDouble: Int => Int = ???

  // 2j. inc and double are a special case of functions where the input and output type is the same.
  // These functions are called endofunctions.
  // Endofunctions are particularly convenient for API because composing two endofunctions give you an endoufunction
  // Can you think of a common design pattern that relies on endofunctions?
  type Endo[A] = A => A
  def composeEndo[A](f: Endo[A], g: Endo[A]): Endo[A] = f compose g

  ///////////////////////////
  // 3. Recursion & Laziness
  ///////////////////////////

  // 3a. Implement `sumList` using an imperative approach (while, for loop)
  // such as sumList(List(1,5,2)) == 8
  def sumList(xs: List[Int]): Int =
    ???

  // 3b. Implement `mkString` using an imperative approach (while, for loop)
  // such as mkString(List('H', 'e', 'l', 'l', 'o')) == "Hello"
  def mkString(xs: List[Char]): String =
    ???

  // 3c. Implement `sumList2` using recursion (same behaviour than `sumList`).
  // Does your implementation work with a large list? e.g. List.fill(1000000)(1)
  def sumList2(xs: List[Int]): Int =
    ???

  ///////////////////////
  // GO BACK TO SLIDES
  ///////////////////////

  def foldLeft[A, B](fa: List[A], b: B)(f: (B, A) => B): B = {
    var acc = b
    for (a <- fa) {
      acc = f(acc, a)
    }
    acc
  }

  @tailrec
  def foldLeftRec[A, B](xs: List[A], b: B)(f: (B, A) => B): B =
    xs match {
      case Nil => b
      case h :: t =>
        val newB = f(b, h)
        foldLeftRec(t, newB)(f)
    }

  def sumList3(xs: List[Int]): Int =
    foldLeft(xs, 0)(_ + _)

  // 3d. Implement `mkString2` using `foldLeft` (same behaviour than `mkString`)
  def mkString2(xs: List[Char]): String =
    ???

  // 3e. Implement `multiply` using `foldLeft`
  // such as multiply(List(3,2,4)) == 3 * 2 * 4 = 24
  // and     multiply(Nil) == 1
  def multiply(xs: List[Int]): Int =
    ???

  // 3f. Implement `forAll` which checks if all elements in a List are true
  // such as forAll(List(true, true , true)) == true
  // but     forAll(List(true, false, true)) == false
  // does your implementation terminate early? e.g. forAll(List(false, false, false)) does not go through the entire list
  // does your implementation work with a large list? e.g. forAll(List.fill(1000000)(true))
  def forAll(xs: List[Boolean]): Boolean =
    ???

  // 3g. Implement `find` which returns the first element in a List where the predicate function returns true
  // such as find(List(1,3,10,2,6))(_ > 5) == Some(10)
  // but     find(List(1,2,3))(_ > 5) == None
  // does your implementation terminate early? e.g. find(List(1,2,3,4)(_ == 2) stop iterating as soon as it finds 2
  // does your implementation work with a large list? e.g. find(1.to(1000000).toList)(_ == -1)
  def find[A](xs: List[A])(predicate: A => Boolean): Option[A] =
    ???

  ///////////////////////
  // GO BACK TO SLIDES
  ///////////////////////

  def foldRight[A, B](xs: List[A], b: B)(f: (A, => B) => B): B =
    xs match {
      case Nil    => b
      case h :: t => f(h, foldRight(t, b)(f))
    }

  // 3h. Implement `forAll2` using `foldRight` (same behaviour than `forAll`)
  def forAll2(xs: List[Boolean]): Boolean =
    ???

  // 3i. Implement `headOption` using `foldRight`.
  // `headOption` returns the first element of a List if it exists
  // such as headOption(List(1,2,3)) == Some(1)
  // but     headOption(Nil) == None
  def headOption[A](xs: List[A]): Option[A] =
    ???

  // 3j. What fold (left or right) would you use to implement `min`? Why?
  def min(xs: List[Int]): Option[Int] = ???

  // 3k. Run `isEven` or `isOdd` for small and large input.
  // Search for mutual tail recursion in Scala.
  def isEvenRec(x: Int): Boolean =
    if (x > 0) isOddRec(x - 1)
    else if (x < 0) isOddRec(x + 1)
    else true

  def isOddRec(x: Int): Boolean =
    if (x > 0) isEvenRec(x - 1)
    else if (x < 0) isEvenRec(x + 1)
    else false

  // 3l. What happens when we call `foo`? Search for General recursion
  // or read https://www.quora.com/Whats-the-big-deal-about-recursion-without-a-terminating-condition
  def foo: Int = foo

  ////////////////////////
  // 4. Pure functions
  ////////////////////////

  // 4a. is `plus` a pure function? why?
  def plus(a: Int, b: Int): Int = a + b

  // 4b. is `div` a pure function? why?
  def div(a: Int, b: Int): Int =
    if (b == 0) sys.error("Cannot divide by 0")
    else a / b

  // 4c. is `times2` a pure function? why?
  var counterTimes2 = 0
  def times2(i: Int): Int = {
    counterTimes2 += 1
    i * 2
  }

  // 4d. is `boolToInt` a pure function? why?
  def boolToInt(b: Boolean): Int =
    if (b) 5
    else Random.nextInt() / 2

  // 4e. is `mapLookup` a pure function? why?
  def mapLookup(map: Map[String, Int], key: String): Int =
    map(key)

  // 4f. is `times3` a pure function? why?
  def times3(i: Int): Int = {
    println("do something here") // could be a database access or http call
    i * 3
  }

  // 4g. is `circleArea` a pure function? why?
  val pi = 3.14
  def circleArea(radius: Double): Double =
    radius * radius * pi

  // 4h. is `inc` or inc_v2 a pure function? why?
  def inc_v2(xs: Array[Int]): Unit =
    for { i <- xs.indices } xs(i) = xs(i) + 1

  // 4i. is `incAll` a pure function? why?
  def incAll(value: Any): Any = value match {
    case x: Int    => x + 1
    case x: Long   => x + 1
    case x: Double => x + 1
  }

  // 4j. is `sum` a pure function? why?
  def sum(xs: List[Int]): Int = {
    var acc = 0
    xs.foreach(x => acc += x)
    acc
  }

  ////////////////////////
  // 5. Memoization
  ////////////////////////

  // 5a. Implement `memoize` such as
  // val cachedInc = memoize((_: Int) + 1)
  // cachedInc(3) // 4 calculated
  // cachedInc(3) // from cache
  // see https://medium.com/musings-on-functional-programming/scala-optimizing-expensive-functions-with-memoization-c05b781ae826
  // or https://github.com/scalaz/scalaz/blob/series/7.3.x/tests/src/test/scala/scalaz/MemoTest.scala
  def memoize[A, B](f: A => B): A => B = ???

  // 5b. How would you adapt `memoize` to work on recursive function e.g. fibonacci
  // can you generalise the pattern?
  def memoize2 = ???

}
