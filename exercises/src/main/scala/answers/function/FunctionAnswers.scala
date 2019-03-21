package answers.function

import exercises.function.FunctionExercises.{Person, updateAge, inc, double}
import toimpl.function.FunctionToImpl

import scala.annotation.tailrec
import scala.collection.mutable

object FunctionAnswers extends FunctionToImpl {

  def identity[A](x: A): A = x

  def const[A, B](a: A)(b: B): A = a

  def triple(x: Int): Int = x * 3

  val tripleVal: Int => Int = triple _

  def tripleAge(xs: List[Person]): List[Person] =
    updateAge(xs, _ * 3)

  def setAge(xs: List[Person], value: Int): List[Person] =
    updateAge(xs, const(value))

  def noopAge(xs: List[Person]): List[Person] =
    updateAge(xs, identity)

  def apply[A, B](f: A => B, value: A): B =
    f(value)

  def andThen[A, B, C](f: A => B, g: B => C): A => C =
    a => g(f(a))

  def compose[A, B, C](f: B => C, g: A => B): A => C =
    a => f(g(a))

  val doubleInc: Int => Int = andThen(double, inc)

  val incDouble: Int => Int = compose(double, inc)

  def curry[A, B, C](f: (A, B) => C): A => B => C =
    a => b => f(a, b)

  def uncurry[A, B, C](f: A => B => C): (A, B) => C =
    (a, b) => f(a)(b)

  def join[A, B, C, D](f: A => B, g: A => C)(h: (B, C) => D): A => D =
    a => h(f(a), g(a))

  def sumList(xs: List[Int]): Int =
    xs match {
      case Nil    => 0
      case h :: t => h + sumList(t)
    }

  def sumList2(xs: List[Int]): Int = {
    @tailrec
    def _sumList(ys: List[Int], acc: Int): Int =
      ys match {
        case Nil    => acc
        case h :: t => _sumList(t, acc + h)
      }

    _sumList(xs, 0)
  }

  @tailrec
  def foldLeft[A, B](xs: List[A], z: B)(f: (B, A) => B): B =
    xs match {
      case Nil    => z
      case h :: t => foldLeft(t, f(z, h))(f)
    }


  def foldRight[A, B](xs: List[A], z: B)(f: (A, => B) => B): B =
    xs match {
      case Nil    => z
      case h :: t => f(h, foldRight(t, z)(f))
    }

  def find[A](xs: List[A])(p: A => Boolean): Option[A] =
    foldRight(xs, Option.empty[A])((a, rest) => if(p(a)) Some(a) else rest)

  def sumList3(xs: List[Int]): Int =
    foldLeft(xs, 0)(_ + _)

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
