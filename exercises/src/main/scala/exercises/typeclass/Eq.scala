package exercises.typeclass

import cats.data.NonEmptyList

trait Eq[A] {
  def eqv(x: A, y: A): Boolean

  def neqv(x: A, y: A): Boolean = !eqv(x, y)

}

object Eq {
  def apply[A](implicit ev: Eq[A]): Eq[A] = ev

  def by[@specialized A, @specialized B](f: A => B)(implicit ev: Eq[B]): Eq[A] =
    new Eq[A] {
      def eqv(x: A, y: A) = ev.eqv(f(x), f(y))
    }

  object syntax {

    implicit class EqSyntax[A](self: A) {
      def eqv(other: A)(implicit ev: Eq[A]): Boolean =
        ev.eqv(self, other)

      def ===(other: A)(implicit ev: Eq[A]): Boolean = eqv(other)

      def neqv(other: A)(implicit ev: Eq[A]): Boolean = !eqv(other)
    }

  }

  import syntax._

  implicit val intEq: Eq[Int] = new Eq[Int] {
    def eqv(x: Int, y: Int): Boolean = x == y
  }

  implicit def listEq[A: Eq]: Eq[List[A]] = new Eq[List[A]] {
    def eqv(x: List[A], y: List[A]): Boolean = x == y
  }

  implicit def nelistEq[A: Eq]: Eq[NonEmptyList[A]] = new Eq[NonEmptyList[A]] {
    def eqv(x: NonEmptyList[A], y: NonEmptyList[A]): Boolean = (x, y) match {
      case (NonEmptyList(h1, t1), NonEmptyList(h2, t2)) =>
        h1 === h2 && t1 === t2
    }
  }

  implicit def optionEq[A: Eq]: Eq[Option[A]] = new Eq[Option[A]] {
    def eqv(x: Option[A], y: Option[A]): Boolean = (x, y) match {
      case (Some(a), Some(b)) => a === b
      case (None, None) => true
      case _ => false
    }
  }

  implicit def eitherEq[A: Eq, B: Eq]: Eq[Either[A, B]] = new Eq[Either[A, B]] {
    def eqv(x: Either[A, B], y: Either[A, B]): Boolean = (x, y) match {
      case (Left(a), Left(b)) => a === b
      case (Right(a), Right(b)) => a === b
      case _ => false
    }
  }

  implicit def tupleEq[A: Eq, B: Eq]: Eq[(A, B)] = new Eq[(A, B)] {
    def eqv(x: (A, B), y: (A, B)): Boolean =
      x._1 === y._1 && x._2 === y._2
  }

  implicit def setEq[A: Eq]: Eq[Set[A]] = new Eq[Set[A]] {
    def eqv(x: Set[A], y: Set[A]): Boolean = x == y
  }

  implicit def vectorEq[A: Eq]: Eq[Vector[A]] = new Eq[Vector[A]] {
    def eqv(x: Vector[A], y: Vector[A]): Boolean = x == y
  }

  implicit def mapEq[A: Eq, B: Eq]: Eq[Map[A, B]] = new Eq[Map[A, B]] {
    def eqv(x: Map[A, B], y: Map[A, B]): Boolean = x == y
  }

  implicit val dblEq: Eq[Double] = new Eq[Double] {
    def eqv(x: Double, y: Double): Boolean = x == y
  }

  implicit val unitEq: Eq[Unit] = new Eq[Unit] {
    def eqv(x: Unit, y: Unit): Boolean = true
  }

  implicit val boolEq: Eq[Boolean] = new Eq[Boolean] {
    def eqv(x: Boolean, y: Boolean): Boolean = x == y
  }

  implicit val stringEq: Eq[String] = new Eq[String] {
    def eqv(x: String, y: String): Boolean = x == y
  }
}
