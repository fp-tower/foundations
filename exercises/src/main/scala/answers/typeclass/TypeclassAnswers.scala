package answers.typeclass

import exercises.typeclass.{Monoid, MyId, Semigroup}
import toimpl.typeclass.TypeclassToImpl
import Monoid.syntax._

import scala.annotation.tailrec

object TypeclassAnswers extends TypeclassToImpl {

  implicit val intMonoid: Monoid[Int] = new Monoid[Int] {
    def combine(a1: Int, a2: Int): Int = a1 + a2
    def empty: Int = 0
  }

  implicit val doubleMonoid: Monoid[Double] = new Monoid[Double] {
    def combine(a1: Double, a2: Double): Double = a1 + a2
    def empty: Double = 0.0
  }

  implicit val stringMonoid: Monoid[String] = new Monoid[String] {
    def combine(a1: String, a2: String): String = a1 + a2
    def empty: String = ""
  }

  implicit val unitMonoid: Monoid[Unit] = new Monoid[Unit] {
    def combine(a1: Unit, a2: Unit): Unit = ()
    def empty: Unit = ()
  }

  implicit val myIdMonoid: Monoid[MyId] = new Monoid[MyId] {
    def combine(a1: MyId, a2: MyId): MyId = MyId(a1.value + a2.value)
    def empty: MyId = MyId("")
  }

  implicit def listMonoid[A]: Monoid[List[A]] = new Monoid[List[A]] {
    def combine(a1: List[A], a2: List[A]): List[A] = a1 ++ a2
    def empty: List[A] = Nil
  }

  implicit val intAndStringMonoid: Monoid[(Int, String)] = tuple2Monoid[Int, String]

  implicit def tuple2Monoid[A: Monoid, B: Monoid]: Monoid[(A, B)] = new Monoid[(A, B)] {
    def combine(a1: (A, B), a2: (A, B)): (A, B) = (a1._1 |+| a2._1, a1._2 |+| a2._2)
    def empty: (A, B) = (mempty[A], mempty[B])
  }

  def fold[A](fa: List[A])(implicit ev: Monoid[A]): A = {
    @tailrec
    def loop(xs: List[A], acc: A): A = xs match {
      case Nil     => acc
      case x :: xs => loop(xs, ev.combine(acc, x))
    }

    loop(fa, ev.empty)
  }

  def sum(xs: List[Int]): Int = fold(xs)

  def averageWordLength(xs: List[String]): Double =
    if(xs.isEmpty) 0.0 else fold(xs.map(_.length)) / xs.size

  def isEmpty[A: Monoid](x: A): Boolean =
    Monoid[A].empty == x

  def ifEmpty[A: Monoid](x: A)(other: => A): A =
    if(isEmpty(x)) other else x

  def repeat[A: Monoid](n: Int)(x: A): A =
    fold(List.fill(n)(x))

  def intercalate[A](xs: List[A], before: A, between: A, after: A)(implicit ev: Monoid[A]): A =
    if(xs.isEmpty) mempty[A]
    else fold(before +: xs.init.map(_ |+| between) :+ xs.last :+ after)

  def intercalate[A](xs: List[A], between: A)(implicit ev: Monoid[A]): A =
    intercalate(xs, mempty[A], between, mempty[A])

  def scsvFormat(xs: List[String]): String =
    intercalate(xs, ";")

  def tupleFormat(xs: List[String]): String =
    intercalate(xs, "(", ",", ")")

  def foldMap[A, B](fa: List[A])(f: A => B)(implicit ev: Monoid[B]): B = {
    @tailrec
    def loop(xs: List[A], acc: B): B = xs match {
      case Nil     => acc
      case x :: xs => loop(xs, ev.combine(acc, f(x)))
    }

    loop(fa, ev.empty)
  }

  def charSequence(xs: List[String]): List[Char] =
    foldMap(xs)(_.toList)

  def fold2[A](xs: List[A])(implicit ev: Monoid[A]): A =
    foldMap(xs)(identity)

  implicit def optionMonoid[A: Semigroup]: Monoid[Option[A]] = new Monoid[Option[A]] {
    def combine(a1: Option[A], a2: Option[A]): Option[A] =
      (a1, a2) match {
        case (Some(x), Some(y)) => Some(Semigroup[A].combine(x, y))
        case _                  => a1.orElse(a2)
      }
    def empty: Option[A] = None
  }

  implicit def mapMonoid[K, A: Semigroup]: Monoid[Map[K, A]] = new Monoid[Map[K, A]] {
    def combine(a1: Map[K, A], a2: Map[K, A]): Map[K, A] =
      (a1.keySet ++ a2.keySet)
        .map(k => k -> (a1.get(k) |+| a2.get(k)))
        .collect{ case (k, Some(v)) => k -> v}
        .toMap

    def empty: Map[K, A] = Map.empty
  }
}
