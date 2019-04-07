package answers.typeclass

import cats.data.NonEmptyList
import exercises.typeclass.Monoid.syntax._
import exercises.typeclass._
import toimpl.typeclass.TypeclassToImpl
import scala.math.Ordering.Implicits._

import scala.annotation.tailrec

object TypeclassAnswers extends TypeclassToImpl {

  implicit val intMonoid: Monoid[Int] = new Monoid[Int] {
    def combine(x: Int, y: Int): Int = x + y
    def empty: Int = 0
  }

  implicit val doubleMonoid: Monoid[Double] = new Monoid[Double] {
    def combine(x: Double, y: Double): Double = x + y
    def empty: Double = 0.0
  }

  implicit val stringMonoid: Monoid[String] = new Monoid[String] {
    def combine(x: String, y: String): String = x + y
    def empty: String = ""
  }

  implicit val unitMonoid: Monoid[Unit] = new Monoid[Unit] {
    def combine(x: Unit, y: Unit): Unit = ()
    def empty: Unit = ()
  }

  implicit val myIdMonoid: Monoid[MyId] = new Monoid[MyId] {
    def combine(x: MyId, y: MyId): MyId = MyId(x.value + y.value)
    def empty: MyId = MyId("")
  }

  implicit def listMonoid[A]: Monoid[List[A]] = new Monoid[List[A]] {
    def combine(x: List[A], y: List[A]): List[A] = x ++ y
    def empty: List[A] = Nil
  }

  implicit def vectorMonoid[A]: Monoid[Vector[A]] = new Monoid[Vector[A]] {
    def combine(x: Vector[A], y: Vector[A]): Vector[A] = x ++ y
    def empty: Vector[A] = Vector.empty
  }

  implicit def setMonoid[A]: Monoid[Set[A]] = new Monoid[Set[A]] {
    def combine(x: Set[A], y: Set[A]): Set[A] = x ++ y
    def empty: Set[A] = Set.empty
  }

  implicit val intAndStringMonoid: Monoid[(Int, String)] = tuple2Monoid[Int, String]

  implicit def tuple2Monoid[A: Monoid, B: Monoid]: Monoid[(A, B)] = new Monoid[(A, B)] {
    def combine(x: (A, B), y: (A, B)): (A, B) = (x._1 |+| y._1, x._2 |+| y._2)
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


  implicit val productMonoid: Monoid[Product] = new Monoid[Product] {
    def combine(x: Product, y: Product): Product = Product(x.getProduct * y.getProduct)
    def empty: Product = Product(1)
  }

  implicit val allMonoid: Monoid[All] = new Monoid[All] {
    def combine(x: All, y: All): All = All(x.getAll && y.getAll)
    def empty: All = All(true)
  }

  implicit def endoMonoid[A]: Monoid[Endo[A]] = new Monoid[Endo[A]] {
    def combine(x: Endo[A], y: Endo[A]): Endo[A] = Endo(x.getEndo compose y.getEndo)
    def empty: Endo[A] = Endo(identity)
  }

  implicit def nelSemigroup[A]: Semigroup[NonEmptyList[A]] = new Semigroup[NonEmptyList[A]] {
    def combine(x: NonEmptyList[A], y: NonEmptyList[A]): NonEmptyList[A] = x ::: y
  }

  implicit def minSemigroup[A: Ordering]: Semigroup[Min[A]] = new Semigroup[Min[A]] {
    def combine(x: Min[A], y: Min[A]): Min[A] =
      if(x.getMin < y.getMin) x else y
  }

  implicit def firstSemigroup[A]: Semigroup[First[A]] = new Semigroup[First[A]] {
    def combine(x: First[A], y: First[A]): First[A] = x
  }

  implicit def dualSemigroup[A: Semigroup]: Semigroup[Dual[A]] = new Semigroup[Dual[A]] {
    def combine(x: Dual[A], y: Dual[A]): Dual[A] = Dual(Semigroup[A].combine(y.getDual, x.getDual))
  }

  implicit def optionMonoid[A: Semigroup]: Monoid[Option[A]] = new Monoid[Option[A]] {
    def combine(x: Option[A], y: Option[A]): Option[A] =
      (x, y) match {
        case (Some(a1), Some(a2)) => Some(Semigroup[A].combine(a1, a2))
        case _                  => x.orElse(y)
      }
    def empty: Option[A] = None
  }

  implicit def mapMonoid[K, A: Semigroup]: Monoid[Map[K, A]] = new Monoid[Map[K, A]] {
    def combine(x: Map[K, A], y: Map[K, A]): Map[K, A] =
      (x.keySet ++ y.keySet)
        .map(k => k -> (x.get(k) |+| y.get(k)))
        .collect{ case (k, Some(v)) => k -> v}
        .toMap

    def empty: Map[K, A] = Map.empty
  }
}
