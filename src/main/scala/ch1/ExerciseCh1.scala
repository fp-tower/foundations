package ch1

object ExerciseCh1 extends App {

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

  // 1d. is times3 a pure function? why?
  def times3(i: Int): Int = {
    println("do something here") // could be a database access or http call
    i * 3
  }

  // 1e. is circleArea a pure function? why?
  val pi = 3.14
  def circleArea(radius: Double): Double =
    radius * radius * pi

  // 1f. is inc or inc_v2 a pure function? why?
  def inc(xs: Array[Int]): Unit =
    for { i <- 0 until xs.length } xs(i) = xs(i) + 1

  def inc_v2(xs: Array[Int]): Unit =
    for { i <- xs.indices } xs(i) = xs(i) + 1





  // 2a. Implement isAdult
  def isAdult(i: Int): Boolean = ???





  // 2b. What if a user pass a negative number? e.g. isAdult(-5)
  // how would update the definition / signature of isAdult





  // 2c. What is the most precise type?
  // Int                  => Option[Boolean]
  // Int Refined Positive => Boolean          (see https://github.com/fthomas/refined)



  // 2d. Why? How do you define precision?




}
