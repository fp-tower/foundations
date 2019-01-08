package ch1

import scala.util.Random

object Ch1Exercises {

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


  // 2a. Implement andThen and compose
  def andThen[A, B, C](f: A => B, g: B => C): A => C = ???

  def compose[A, B, C](f: B => C, g: A => B): A => C = ???

  // 2b. Implement identity
  def identity[A](x: A): A = ???

  // 2.c Implement const
  def const[A, B](a: A)(b: B): A = ???

  // 2c. Implement join
  def join[A, B, C, D](f: A => B, g: A => C)(h: (B, C) => D): A => D = ???


  // 2d. Implement getX and getY
  case class Point(x: Double, y: Double)

  val getX: Point => Double = ???
  val getY: Point => Double = ???

  // 2e. Implement square
  val square: Double => Double = ???

  // 2f. Implement sum
  val sum: (Double, Double) => Double = ???

  // 2g. Implement squareRoot
  val squareRoot: Double => Double = ???

  // 2g. Implement euclideanDistance such as euclideanDistance(Point(x, y)) = squareRoot(x^2 + y^2)
  // try to use join, compose, andThen
  val euclideanDistance: Point => Double = ???

}
