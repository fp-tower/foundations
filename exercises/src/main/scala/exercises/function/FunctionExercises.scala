package exercises.function

import exercises.function.HttpClientBuilder
import exercises.function.HttpClientBuilder._
import toimpl.function.FunctionToImpl

import scala.annotation.tailrec
import scala.concurrent.duration._
import scala.util.Random

// you can run and print things here
object FunctionApp extends App {
  import FunctionExercises._

  println(plus(3, 4))

}

object FunctionExercises extends FunctionToImpl {

  ////////////////////////////
  // 1. first class functions
  ////////////////////////////

  // 1a. Implement tripleVal such as it behaves in the same way as triple
  def triple(x: Int): Int = 3 * x

  val tripleVal: Int => Int =
    (x: Int) => ???

  // 1b. Implement tripleList by reusing triple or tripleVal, what's the difference?
  // such as tripleList(List(1,2,3)) == List(3,6,9)
  // hint: you can use map from List
  def tripleList(xs: List[Int]): List[Int] = ???

  // 1c. Implement tripleVal2 by transforming triple into a val
  // You may want to read about eta expansion
  val tripleVal2: Int => Int = _ => ???

  // 1d. Implement move that increase or decrease an Int based on a Direction
  // such as move(Up)(5) == 6
  // but     move(Down)(5) == 4
  sealed trait Direction
  object Direction {
    case object Up   extends Direction
    case object Down extends Direction
  }

  def move(direction: Direction)(x: Int): Int = ???

  // 1e. what's the difference between all these versions of move?
  def move2(direction: Direction, x: Int): Int =
    move(direction)(x)

  val move3: (Direction, Int) => Int =
    (direction, x) => move(direction)(x)

  val move4: Direction => Int => Int =
    direction => x => move(direction)(x)

  ////////////////////////////
  // 2. polymorphic functions
  ////////////////////////////

  // 2a. Implement identity
  // such as identity(1) == 1
  //         identity("foo") == "foo"
  // Imagine you were a hacker trying to introduce a bug in identity.
  // Which other implementations of identity could you use such as it
  // satisfies the type checker.
  def identity[A](x: A): A = ???

  // 2b. Implement const
  // such as const(5)("foo") == 5
  //         List(1,2,3).map(const(0)) == List(0,0,0)
  def const[A, B](a: A)(b: B): A = ???

  // 2c. Implement setUsersAge which updates the age of all users
  // such as setUsersAge(10) == List(User("John", 10), User("Lisa", 10))
  // hint: use updateUsersAge with one of the polymorphic functions we just saw
  case class User(name: String, age: Int)

  def updateUsersAge(f: Int => Int): List[User] =
    List(User("John", 26), User("Lisa", 5)).map { p =>
      p.copy(age = f(p.age))
    }

  def setUsersAge(value: Int): List[User] = ???

  // 2d. implement getUsers which returns all users
  // such as getUsers == List(User("John", 26), User("Lisa", 5))
  // hint: use updateUsersAge with one of the polymorphic functions we just saw
  def getUsers: List[User] = ???

  // 2e. Transform identity into a function (val). See Eta expansion https://stackoverflow.com/a/39446986
  // val idVal = ???

  // 2f. what's the difference between mapOption and mapOption2?
  // Which one should you use?
  def mapOption[A, B](option: Option[A], f: A => B): Option[B]  = option.map(f)
  def mapOption2[A, B](option: Option[A])(f: A => B): Option[B] = option.map(f)

  // 2g. Implement andThen and compose
  // such as
  // val isEven: Int => Boolean = _ % 2 == 0
  // val inc   : Int => Int = _ + 1
  // compose(isEven, inc)(10) == false
  // andThen(inc, isEven)(10) == false
  def andThen[A, B, C](f: A => B, g: B => C): A => C = ???

  def compose[A, B, C](f: B => C, g: A => B): A => C = ???

  // 2h. Implement the function f(x) = 2 * x + 1 using inc, double with compose or andThen
  val inc: Int => Int    = x => x + 1
  val double: Int => Int = x => 2 * x

  val doubleInc: Int => Int = identity // ???

  // 2i. Same for f(x) = 2 * (x + 1)
  val incDouble: Int => Int = identity // ???

  // 2j. inc and double are a special case of function where the input and output type is the same.
  // These functions are called endofunctions.
  // Endofunctions are particularly convenient for API because composing two endofunctions give you an endoufunction
  // Can you think of a common design pattern that relies on endofunctions?
  type Endo[A] = A => A
  def composeEndo[A](f: Endo[A], g: Endo[A]): Endo[A] = f compose g

  ///////////////////////////
  // 3. Recursion & Laziness
  ///////////////////////////

  // 3a. Implement sumList using an imperative approach (while, for loop)
  // such as sumList(List(1,5,2)) == 8
  def sumList(xs: List[Int]): Int = ???

  // 3b. Use recursion to implement sumList2
  // does your implementation work with a large list? e.g. sumList2(List.fill(1000000)(1))
  val largeList: List[Int] = List.fill(1000000)(1) // List(1,1,1,1, ...)

  def sumList2(xs: List[Int]): Int = ???

  // 3c. Implement mkString, you can use an imperative approach or recursion
  // such as mkString(List('H', 'e', 'l', 'l', 'o')) == "Hello"
  def mkString(xs: List[Char]): String = ???

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
  def foldLeftRec[A, B](xs: List[A], z: B)(f: (B, A) => B): B =
    xs match {
      case Nil    => z
      case h :: t => foldLeftRec(t, f(z, h))(f)
    }

  def sumList3(xs: List[Int]): Int =
    foldLeft(xs, 0)(_ + _)

  def mkString2(xs: List[Char]): String =
    foldLeft(xs, "")(_ + _)

  // 3d. Implement multiply using foldLeft
  // such as multiply(List(3,2,4)) == 24
  def multiply(xs: List[Int]): Int = ???

  // 3e. Implement filter using foldLeft
  // such as filter(List(1,2,3,4))(isEven) == List(2,4)
  def filter[A](xs: List[A])(p: A => Boolean): List[A] = ???

  // 3f. Implement forAll using a recursion or loop
  // forAll returns true if all the elements in the List are true
  // such as forAll(List(true, true , true)) == true
  // but     forAll(List(true, false, true)) == false
  // does your implementation terminate early? e.g. forAll(List(false, false, false)) does not go through the entire list
  // does your implementation work with a large list? e.g. forAll(List.fill(1000000)(true))
  def forAll(xs: List[Boolean]): Boolean = ???

  // 3g. Implement find using a recursion or loop
  // find returns the first element of a List that matches the predicate `p`
  // such as find(List(1,3,10,2,6))(_ > 5) == Some(10)
  // but     find(List(1,2,3))(_ == -1) == None
  // does your implementation terminate early? e.g. find(List(1,2,3,4)(_ == 2) stop iterating as soon as it finds 2
  // does your implementation work with a large list? e.g. find(1.to(1000000).toList)(_ == -1)
  def find[A](xs: List[A])(p: A => Boolean): Option[A] = ???

  ///////////////////////
  // GO BACK TO SLIDES
  ///////////////////////

  // 3h. Implement forAll2 and find2 using foldRight
  // foldRight is an abstraction over recursion that can terminate early
  // early termination is achieved by laziness (see call by name `=> B`)
  def foldRight[A, B](xs: List[A], z: B)(f: (A, => B) => B): B =
    xs match {
      case Nil    => z
      case h :: t => f(h, foldRight(t, z)(f))
    }

  def forAll2(xs: List[Boolean]): Boolean = ???

  def find2[A](xs: List[A])(p: A => Boolean): Option[A] = ???

  // 3i. which fold (left or right) would you use to implement the following min and take? Why
  def min(xs: List[Int]): Option[Int] = ???

  def take[A](xs: List[A])(x: Int): List[Int] = ???

  // 3j. Run isEven / isOdd for small and large input. Search for mutual tail recursion in scala
  def isEven(x: Int): Boolean =
    if (x > 0) isOdd(x - 1)
    else if (x < 0) isOdd(x + 1)
    else true

  def isOdd(x: Int): Boolean =
    if (x > 0) isEven(x - 1)
    else if (x < 0) isEven(x + 1)
    else false

  // 3k. does the commented function below compile? If yes, what happens when you call it
  // Search for General recursion
  // or https://www.quora.com/Whats-the-big-deal-about-recursion-without-a-terminating-condition
  //  def foo: Int = foo

  ////////////////////////
  // 4. Pure functions
  ////////////////////////

  // 4a. is plus a pure function? why?
  def plus(a: Int, b: Int): Int = a + b

  // 4b. is div a pure function? why?
  def div(a: Int, b: Int): Int =
    if (b == 0) sys.error("Cannot divide by 0")
    else a / b

  // 4c. is times2 a pure function? why?
  var counterTimes2 = 0
  def times2(i: Int): Int = {
    counterTimes2 += 1
    i * 2
  }

  // 4d. is boolToInt a pure function? why?
  def boolToInt(b: Boolean): Int =
    if (b) 5
    else Random.nextInt() / 2

  // 4e. is mapLookup a pure function? why?
  def mapLookup(map: Map[String, Int], key: String): Int =
    map(key)

  // 4f. is times3 a pure function? why?
  def times3(i: Int): Int = {
    println("do something here") // could be a database access or http call
    i * 3
  }

  // 4g. is circleArea a pure function? why?
  val pi = 3.14
  def circleArea(radius: Double): Double =
    radius * radius * pi

  // 4h. is inc or inc_v2 a pure function? why?
  def inc(xs: Array[Int]): Unit =
    for { i <- 0 to xs.length } xs(i) = xs(i) + 1

  def inc_v2(xs: Array[Int]): Unit =
    for { i <- xs.indices } xs(i) = xs(i) + 1

  // 4i. is incAll a pure function? why?
  def incAll(value: Any): Any = value match {
    case x: Int    => x + 1
    case x: Long   => x + 1
    case x: Double => x + 1
  }

  // 4j. is incAll_v2 a pure function? why?
  def incAll_v2(value: Any): Any = value match {
    case x: Int    => x + 1
    case x: Long   => x + 1
    case x: Double => x + 1
    case _         => 0
  }

  // 4k. is sum a pure function? why?
  def sum(xs: List[Int]): Int = {
    var acc = 0
    xs.foreach(x => acc += x)
    acc
  }

  ////////////////////////
  // 5. Memoization
  ////////////////////////

  // 5a. Implement memoize such as
  // val cachedInc = memoize((_: Int) + 1)
  // cachedInc(3) // 4 calculated
  // cachedInc(3) // from cache
  // see https://medium.com/musings-on-functional-programming/scala-optimizing-expensive-functions-with-memoization-c05b781ae826
  // or https://github.com/scalaz/scalaz/blob/series/7.3.x/tests/src/test/scala/scalaz/MemoTest.scala
  def memoize[A, B](f: A => B): A => B = ???

  // 5b. How would you adapt memoize to work on recursive function e.g. fibonacci
  // can you generalise the pattern?
  def memoize2 = ???

}
