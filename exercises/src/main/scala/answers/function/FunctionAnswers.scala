package answers.function

import toimpl.function.FunctionToImpl
import scala.collection.mutable

import scala.annotation.tailrec

object FunctionAnswers extends FunctionToImpl {

  def apply[A, B](f: A => B, value: A): B =
    f(value)

  def andThen[A, B, C](f: A => B, g: B => C): A => C =
    a => g(f(a))

  def compose[A, B, C](f: B => C, g: A => B): A => C =
    a => f(g(a))

  val inc   : Int => Int = x => x + 1
  val double: Int => Int = x => 2 * x

  val doubleInc: Int => Int = andThen(double, inc)

  def curry[A, B, C](f: (A, B) => C): A => B => C =
    a => b => f(a, b)

  def uncurry[A, B, C](f: A => B => C): (A, B) => C =
    (a, b) => f(a)(b)

  def join[A, B, C, D](f: A => B, g: A => C)(h: (B, C) => D): A => D =
    a => h(f(a), g(a))

  def identity[A](x: A): A = x

  def const[A, B](a: A)(b: B): A = a

  def sumList(xs: List[Int]): Int = {
    @tailrec
    def _sumList(ys: List[Int], acc: Int): Int =
      ys match {
        case Nil    => 0
        case h :: t => _sumList(t, acc + h)
      }

    _sumList(xs, 0)
  }

  def memoize[A, B](f: A => B): A => B = {
    val cache = mutable.Map.empty[A, B]
    (a: A) => {
      cache.get(a) match {
        case Some(b) => b    // cache succeeds
        case None    =>
          val b = f(a)
          cache.update(a, b) // update cache
          b
      }
    }
  }
}
