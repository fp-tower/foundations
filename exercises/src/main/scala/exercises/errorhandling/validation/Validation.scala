package exercises.errorhandling.validation
import exercises.errorhandling.NEL

// Validation is not part of the standard library.
// It is called `Validated` in cats and `ZValidation` in zio-prelude.
sealed trait Validation[+E, +A] {
  import Validation._

  def map[Next](update: A => Next): Validation[E, Next] =
    this match {
      case Invalid(errors) => Invalid(errors)
      case Valid(value)    => Valid(update(value))
    }

  def flatMap[EE >: E, Next](update: A => Validation[EE, Next]): Validation[EE, Next] =
    this match {
      case Invalid(errors) => Invalid(errors)
      case Valid(value)    => update(value)
    }

  def zip[EE >: E, Other](other: Validation[EE, Other]): Validation[EE, (A, Other)] =
    ???

  // alias for `zip` followed by `map`.
  def zipWith[EE >: E, Other, Next](other: Validation[EE, Other])(
    update: (A, Other) => Next
  ): Validation[EE, Next] =
    zip(other).map { case (a, other) => update(a, other) }
}

object Validation {
  case class Invalid[+E](value: NEL[E]) extends Validation[E, Nothing]
  case class Valid[+A](value: A)        extends Validation[Nothing, A]

  def fromEither[E, A](either: Either[E, A]): Validation[E, A] =
    either match {
      case Left(value)  => Invalid(NEL(value))
      case Right(value) => Valid(value)
    }
}
