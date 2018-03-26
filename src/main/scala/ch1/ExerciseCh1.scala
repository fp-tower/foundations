package ch1

object ExerciseCh1 extends App {

  // 1. Implement isAdult
  def isAdult(i: Int): Boolean = ???





  // 2. What if a user pass a negative number? e.g. isAdult(-5)
  // how would update the definition / signature of isAdult





  // 3a. What is the most precise type?
  // Int           => Either[String, Boolean]
  // Positive[Int] => Boolean



  // 3b. Why? How do you define precision?





  // 4.
  def inc(xs: Array[Int]): Unit =
    for { i <- 0 until xs.length } xs(i) = xs(i) + 1

  def inc_v2(xs: Array[Int]): Unit =
    for { i <- xs.indices } xs(i) = xs(i) + 1

  val foo = Array(1,2,3)
  println(inc(foo))
  println(foo.toList)

}
