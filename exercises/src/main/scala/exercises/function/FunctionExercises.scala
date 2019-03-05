package exercises.function

import toimpl.function.FunctionToImpl

import scala.util.Random

// you can run and print things here
object FunctionApp extends App {
  import FunctionExercises._

  println(plus(3, 4))

}

object FunctionExercises extends FunctionToImpl {

  // 1. Pure functions

  // 1a. is plus a pure function? why?
  def plus(a: Int, b: Int): Int = a + b

  // 1b. is div a pure function? why?
  def div(a: Int, b: Int): Int =
    if(b == 0) sys.error("Cannot divide by 0")
    else a / b

  // 1c. is times2 a pure function? why?
  var counterTimes2 = 0
  def times2(i: Int): Int = {
    counterTimes2 += 1
    i * 2
  }

  // 1d. is boolToInt a pure function? why?
  def boolToInt(b: Boolean): Int =
    if(b) 5
    else Random.nextInt() / 2

  // 1e. is mapLookup a pure function? why?
  def mapLookup(map: Map[String, Int], key: String): Int =
    map(key)

  // 1f. is times3 a pure function? why?
  def times3(i: Int): Int = {
    println("do something here") // could be a database access or http call
    i * 3
  }

  // 1g. is circleArea a pure function? why?
  val pi = 3.14
  def circleArea(radius: Double): Double =
    radius * radius * pi

  // 1h. is inc or inc_v2 a pure function? why?
  def inc(xs: Array[Int]): Unit =
    for { i <- 0 to xs.length } xs(i) = xs(i) + 1

  def inc_v2(xs: Array[Int]): Unit =
    for { i <- xs.indices } xs(i) = xs(i) + 1


  // 1i. is incAll a pure function? why?
  def incAll(value: Any): Any = value match {
    case x: Int    => x + 1
    case x: Long   => x + 1
    case x: Double => x + 1
  }

  // 1j. is incAll_v2 a pure function? why?
  def incAll_v2(value: Any): Any = value match {
    case x: Int    => x + 1
    case x: Long   => x + 1
    case x: Double => x + 1
    case _         => 0
  }

  // 1k. is sum a pure function? why?
  def sum(xs: List[Int]): Int = {
    var acc = 0
    xs.foreach(x => acc += x)
    acc
  }

  // 2. polymorphic functions

  // 2a. Implement identity
  def identity[A](x: A): A = ???

  // 2b. Implement const
  def const[A, B](a: A)(b: B): A = ???

  // 2c. Transform triple method (def) into tripleVal function (val)
  def triple(x: Int): Int = x * 3

  val tripleVal: Int => Int = identity

  // 2d. Transform identity into a function (val). See Eta expansion https://stackoverflow.com/a/39446986


  // 3. higher order functions
  case class Person(name: String, age: Int)

  def updateAge(xs: List[Person], f: Int => Int): List[Person] =
    xs.map{ p =>
      p.copy(age = f(p.age))
    }

  // 3a. implement tripleAge using updateAge
  def tripleAge(xs: List[Person]): List[Person] = ???

  // 3b. implement setAge using updateAge and one polymorphic function we saw before
  def setAge(xs: List[Person], value: Int): List[Person] = ???

  // 3c. implement noopAge (do nothing) using updateAge and one polymorphic function we saw before
  def noopAge(xs: List[Person]): List[Person] = ???

  // 3d. implement updateAge2 and setAge2
  def updateAge2(f: Int => Int): List[Person] => List[Person] = ???

  def setAge2(value: Int): List[Person] => List[Person] = ???

  // 3e. Implement apply
  def apply[A, B](f: A => B, value: A): B = ???

  // 3f. Implement andThen and compose
  def andThen[A, B, C](f: A => B, g: B => C): A => C = ???

  def compose[A, B, C](f: B => C, g: A => B): A => C = ???

  // 3g. Implement the function f(x) = 2 * x + 1 using inc, double with compose or andThen
  val inc   : Int => Int = x => x + 1
  val double: Int => Int = x => 2 * x

  val doubleInc: Int => Int = identity


  // 3h. Same for f(x) = 2 * (x + 1)
  val incDouble: Int => Int = identity


  // 3i. Implement curry and uncurry
  def curry[A, B, C](f: (A, B) => C): A => B => C = ???

  def uncurry[A, B, C](f: A => B => C): (A, B) => C = ???


  // 3j. Implement join
  def join[A, B, C, D](f: A => B, g: A => C)(h: (B, C) => D): A => D = ???



  // 4. Recursion

  // 4a. Use recursion to implement sumList
  def sumList(xs: List[Int]): Int = ???


  // 4b. Run it with different size of list
  // What happens when the list is big enough? Why?
  // You can use `to` to generate a List from 1 to x
  val oneToTen = 1.to(10).toList


  // 4c. Re-write sumList recursively without the same issue


  // 4d. Run isEven / isOdd for small and large input. Search for mutual tail recursion in scala
  def isEven(x: Int): Boolean =
    if(x > 0) !isOdd(x - 1)
    else if (x < 0) !isOdd(x + 1)
    else true

  def isOdd(x: Int): Boolean =
    if(x > 0) !isEven(x - 1)
    else if (x < 0) !isEven(x + 1)
    else false

  // 4e. does the commented function below compile? If yes, what happens when you call it
  // Search for General recursion
  // or https://www.quora.com/Whats-the-big-deal-about-recursion-without-a-terminating-condition
  //  def foo: Int = foo


  // 5a. Implement memoize such as
  // val cachedInc = memoize((_: Int) + 1)
  // cachedInc(3) // 4 calculated
  // cachedInc(3) // from cache
  // see https://medium.com/musings-on-functional-programming/scala-optimizing-expensive-functions-with-memoization-c05b781ae826
  // or https://github.com/scalaz/scalaz/blob/series/7.3.x/tests/src/test/scala/scalaz/MemoTest.scala
  def memoize[A, B](f: A => B): A => B = ???


  // 5b. How would you adapt memoize to work on recursive function e.g. fibonacci
  // can you generalise the pattern?

}
