package exercises.function

import toimpl.function.FunctionToImpl

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

  val tripleVal: Int => Int = (x: Int) => ???

  // 1b. Implement tripleList by reusing triple or tripleVal, what's the difference?
  // such as tripleList(List(1,2,3)) == List(3,6,9)
  // hint: you can use map from List
  def tripleList(xs: List[Int]): List[Int] = ???

  // 1c. Implement tripleVal2 by transforming triple into a val
  val tripleVal2: Int => Int = _ => ???

  // 1d. Implement move that increase or decrease an Int based on a Boolean flag
  // such as move(true ).apply(5) == 6
  // but     move(false).apply(5) == 4
  // note: move(true).apply(5) can be shorten to move(true)(5)
  def move(increment: Boolean): Int => Int = ???

  // 1e. Implement move2 and move3 by reusing move
  // what's the difference between the two?
  val move2: (Boolean, Int) => Int = (_, _) => ???

  val move3: Boolean => Int => Int = _ => ???

  // 1f. Implement applyMany
  // such as applyMany(List(_ + 1, _ - 1, _ * 2))(10) == List(11, 9, 20)
  def applyMany(xs: List[Int => Int]): Int => List[Int] = ???

  // 1g. Implement applyManySum
  // such as applyManySum(List(_ + 1, _ - 1, _ * 2))(10) == 30
  def applyManySum(xs: List[Int => Int]): Int => Int = ???

  ////////////////////////////
  // 2. polymorphic functions
  ////////////////////////////

  // 2a. Implement identity
  // such as identity(1) == 1
  //         identity("foo") == "foo"
  def identity[A](x: A): A = ???

  // 2b. Implement const
  // such as const(5)("foo") == 5
  //         List(1,2,3).map(const(0)) == List(0,0,0)
  def const[A, B](a: A)(b: B): A = ???

  // 2d. Transform identity into a function (val). See Eta expansion https://stackoverflow.com/a/39446986
  // val idVal = ???

  // 2e. implement setAge using updateAge
  // such as setAge(10) == List(User("John", 10), User("Lisa", 10))
  // hint: try to use either identity or const
  case class User(name: String, age: Int)

  def updateAge(f: Int => Int): List[User] =
    List(User("John", 26), User("Lisa", 5)).map { p =>
      p.copy(age = f(p.age))
    }

  def setAge(value: Int): List[User] = ???

  // 2f. implement noopAge using updateAge
  // such as getUsersUnchanged == List(User("John", 26), User("Lisa", 5))
  // hint: try to use either identity or const
  def getUsersUnchanged: List[User] = ???

  // 2g. Implement andThen and compose
  def andThen[A, B, C](f: A => B, g: B => C): A => C = ???

  def compose[A, B, C](f: B => C, g: A => B): A => C = ???

  // 2h. Implement the function f(x) = 2 * x + 1 using inc, double with compose or andThen
  val inc: Int => Int    = x => x + 1
  val double: Int => Int = x => 2 * x

  val doubleInc: Int => Int = identity // ???

  // 2i. Same for f(x) = 2 * (x + 1)
  val incDouble: Int => Int = identity // ???

  // 2j. Implement curry and uncurry
  def curry[A, B, C](f: (A, B) => C): A => B => C = ???

  def uncurry[A, B, C](f: A => B => C): (A, B) => C = ???

  // 2k. Implement join
  def join[A, B, C, D](f: A => B, g: A => C)(h: (B, C) => D): A => D = ???

  ////////////////////////
  // 3. Pure functions
  ////////////////////////

  // 3a. is plus a pure function? why?
  def plus(a: Int, b: Int): Int = a + b

  // 3b. is div a pure function? why?
  def div(a: Int, b: Int): Int =
    if (b == 0) sys.error("Cannot divide by 0")
    else a / b

  // 3c. is times2 a pure function? why?
  var counterTimes2 = 0
  def times2(i: Int): Int = {
    counterTimes2 += 1
    i * 2
  }

  // 3d. is boolToInt a pure function? why?
  def boolToInt(b: Boolean): Int =
    if (b) 5
    else Random.nextInt() / 2

  // 3e. is mapLookup a pure function? why?
  def mapLookup(map: Map[String, Int], key: String): Int =
    map(key)

  // 3f. is times3 a pure function? why?
  def times3(i: Int): Int = {
    println("do something here") // could be a database access or http call
    i * 3
  }

  // 3g. is circleArea a pure function? why?
  val pi = 3.14
  def circleArea(radius: Double): Double =
    radius * radius * pi

  // 3h. is inc or inc_v2 a pure function? why?
  def inc(xs: Array[Int]): Unit =
    for { i <- 0 to xs.length } xs(i) = xs(i) + 1

  def inc_v2(xs: Array[Int]): Unit =
    for { i <- xs.indices } xs(i) = xs(i) + 1

  // 3i. is incAll a pure function? why?
  def incAll(value: Any): Any = value match {
    case x: Int    => x + 1
    case x: Long   => x + 1
    case x: Double => x + 1
  }

  // 3j. is incAll_v2 a pure function? why?
  def incAll_v2(value: Any): Any = value match {
    case x: Int    => x + 1
    case x: Long   => x + 1
    case x: Double => x + 1
    case _         => 0
  }

  // 3k. is sum a pure function? why?
  def sum(xs: List[Int]): Int = {
    var acc = 0
    xs.foreach(x => acc += x)
    acc
  }

  ///////////////////////////
  // 4. Recursion & Laziness
  ///////////////////////////

  // 4a. Use recursion to implement sumList
  def sumList(xs: List[Int]): Int = ???

  // 4b. Run it with different size of list
  // What happens when the list is big enough? Why?
  // You can use `to` to generate a List from 1 to x
  val oneToTen = 1.to(10).toList

  // 4c. implement sumList2 recursively without the same issue
  def sumList2(xs: List[Int]): Int = ???

  // 4d. Implement foldLeft using recursion
  def foldLeft[A, B](xs: List[A], z: B)(f: (B, A) => B): B = ???

  // 4e. Implement foldRight using recursion
  def foldRight[A, B](xs: List[A], z: B)(f: (A, => B) => B): B = ???

  // 4f. Implement sumList3 using a fold, which one should you choose?
  def sumList3(xs: List[Int]): Int = ???

  // 4g. Implement find using a fold, which one should you choose?
  def find[A](xs: List[A])(p: A => Boolean): Option[A] = ???

  // 4h. Run isEven / isOdd for small and large input. Search for mutual tail recursion in scala
  def isEven(x: Int): Boolean =
    if (x > 0) isOdd(x - 1)
    else if (x < 0) isOdd(x + 1)
    else true

  def isOdd(x: Int): Boolean =
    if (x > 0) isEven(x - 1)
    else if (x < 0) isEven(x + 1)
    else false

  // 4i. does the commented function below compile? If yes, what happens when you call it
  // Search for General recursion
  // or https://www.quora.com/Whats-the-big-deal-about-recursion-without-a-terminating-condition
  //  def foo: Int = foo

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
