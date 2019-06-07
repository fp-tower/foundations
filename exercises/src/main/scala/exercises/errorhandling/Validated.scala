package exercises.errorhandling

import cats.data.NonEmptyList
import exercises.typeclass.Eq

sealed trait Validated[+E, +A] {
  import Validated.{Invalid, Valid}

  def toEither: Either[E, A] = this match {
    case Invalid(e) => Left(e)
    case Valid(a)   => Right(a)
  }
}

object Validated {
  case class Invalid[+E](value: E) extends Validated[E, Nothing]
  case class Valid[+A](value: A)   extends Validated[Nothing, A]

  def invalid[E, A](value: E): Validated[E, A]                  = Invalid(value)
  def invalidNel[E, A](value: E): Validated[NonEmptyList[E], A] = invalid(NonEmptyList.of(value))

  def valid[E, A](value: A): Validated[E, A]                  = Valid(value)
  def validNel[E, A](value: A): Validated[NonEmptyList[E], A] = valid(value)

  def fromEither[E, A](fa: Either[E, A]): Validated[E, A] =
    fa match {
      case Left(e)  => Invalid(e)
      case Right(a) => Valid(a)
    }

  implicit def eq[E: Eq, A: Eq]: Eq[Validated[E, A]] =
    Eq.by(_.toEither)

}
