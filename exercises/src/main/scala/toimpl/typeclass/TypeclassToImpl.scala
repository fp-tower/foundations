package toimpl.typeclass

import exercises.typeclass.{Monoid, MyId, Semigroup}

trait TypeclassToImpl {
  implicit val intMonoid: Monoid[Int]
  implicit val doubleMonoid: Monoid[Double]
  implicit val stringMonoid: Monoid[String]
  implicit val unitMonoid: Monoid[Unit]
  implicit val myIdMonoid: Monoid[MyId]
  implicit def listMonoid[A]: Monoid[List[A]]
  implicit val intAndStringMonoid: Monoid[(Int, String)]
  implicit def tuple2Monoid[A: Monoid, B: Monoid]: Monoid[(A, B)]

  def sum(xs: List[Int]): Int
  def averageWordLength(xs: List[String]): Double
  def isEmpty[A: Monoid](x: A): Boolean
  def ifEmpty[A: Monoid](x: A)(other: => A): A
  def repeat[A: Monoid](n: Int)(x: A): A
  def intercalate[A](xs: List[A], before: A, between: A, after: A)(implicit ev: Monoid[A]): A
  def intercalate[A](xs: List[A], between: A)(implicit ev: Monoid[A]): A
  def scsvFormat(xs: List[String]): String
  def tupleFormat(xs: List[String]): String
  def foldMap[A, B](xs: List[A])(f: A => B)(implicit ev: Monoid[B]): B
  def charSequence(xs: List[String]): List[Char]
  def fold2[A](xs: List[A])(implicit ev: Monoid[A]): A

  implicit def optionMonoid[A: Semigroup]: Monoid[Option[A]]
  implicit def mapMonoid[K, A: Semigroup]: Monoid[Map[K, A]]
}
