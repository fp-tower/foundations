package exercises.function

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
  // Can you re-use `isValidUsernameCharacter` or `_isValidUsernameCharacter` or both? Why?
  def isValidUsername(username: String): Boolean = ???

  //////////////////////////////////////////////////
  // 2. functions as output (aka curried functions)
  //////////////////////////////////////////////////

  // 2a. Implement `increment` and `decrement` using `add`
  // such as increment(5) == 6
  // and     decrement(5) == 4
  // Note: You can remove the lazy keyword as soon you `increment` and `decrement`.
  def add(x: Int)(y: Int): Int = x + y

  lazy val increment: Int => Int = ???

  lazy val decrement: Int => Int = ???

  ////////////////////////////
  // 3. parametric functions
  ////////////////////////////

  case class Pair[A](first: A, second: A) {
    // 3a. Implement `swap` which exchanges `first` and `second`
    // such as Pair("John", "Doe").swap == Pair("Doe", "John")
    // Bonus: how many implementations of `swap` would compile?
    def swap: Pair[A] =
      ???

    // 3b. Implement `map` which applies a function to `first` and `second`
    // such as Pair("John", "Doe").map(_.length) == Pair(4,3)
    def map[B](f: A => B): Pair[B] =
      ???

    // 3c. Implement `forAll` which check if a predicate is true for both `first` and `second`
    // such as Pair(2, 6).forAll(_ > 0) == true
    // but     Pair(2, 6).forAll(_ > 2) == false
    //         Pair(2, 6).forAll(_ > 9) == false
    def forAll(predicate: A => Boolean): Boolean =
      ???

    // 3d. Implement `zipWith` which merges two `Pair` using a `combine` function
    // such as Pair(0, 2).zipWith(Pair(3, 3), (x: Int, y: Int) => x + y) == Pair(3, 5)
    def zipWith[B, C](other: Pair[B], combine: (A, B) => C): Pair[C] =
      ???

    // 3e. Would you rather define `zipWith` with one input parameter list (like above) or two
    // parameter lists (like below)? In other words, is there any benefits to curry `zipWith`?
    def zipWithCurried[B, C](other: Pair[B])(combine: (A, B) => C): Pair[C] =
      zipWith(other, combine)
  }

  val names: Pair[String] = Pair("John", "Elisabeth")
  val ages: Pair[Int]     = Pair(32, 46)
  case class User(name: String, age: Int)

  // 3f. Combine `names` and `ages` into `users` using Pair API
  // such as users == Pair(User("John", 32), User("Elisabeth", 46))
  lazy val users: Pair[User] = ???

  // 3g. Check that the length of each string in `names` is strictly longer than 5 using Pair API
  lazy val longerThan5: Boolean = ???

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
