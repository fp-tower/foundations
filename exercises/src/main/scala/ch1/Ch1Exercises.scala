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


}
