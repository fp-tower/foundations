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

  def flatMap[E1 >: E, Next](update: A => Validation[E1, Next]): Validation[E1, Next] =
    this match {
      case Invalid(errors) => Invalid(errors)
      case Valid(value)    => update(value)
    }

  // Implement the method `zip` so that it accumulates errors if both are Invalid.
  // For example,
  // "error1".invalid.zip("error2".invalid) == Invalid(NEL("error1", "error2"))
  // "error1".invalid.zip("Hello".valid)    == Invalid(NEL("error1"))
  // 1.valid.zip("error2".invalid)          == Invalid(NEL("error2"))
  // 1.valid.zip("Hello".valid)             == Valid((1, "Hello"))
  def zip[E1 >: E, Other](other: Validation[E1, Other]): Validation[E1, (A, Other)] =
    ???

  // alias for `zip` followed by `map`.
  def zipWith[E1 >: E, Other, Next](other: Validation[E1, Other])(
    update: (A, Other) => Next
  ): Validation[E1, Next] =
    zip(other).map { case (a, other) => update(a, other) }
}

object Validation {
  case class Invalid[+E](value: NEL[E]) extends Validation[E, Nothing]
  case class Valid[+A](value: A)        extends Validation[Nothing, A]

  def valid[A](value: A): Validation[Nothing, A] =
    Valid(value)

  def invalid[E](value: E, other: E*): Validation[E, Nothing] =
    Invalid(NEL(value, other.toList))

  // Similar to `Either.cond`
  def cond[E, A](test: Boolean, success: => A, failure: E): Validation[E, A] =
    if (test) valid(success) else invalid(failure)

  def fromEither[E, A](either: Either[E, A]): Validation[E, A] =
    either match {
      case Left(value)  => Invalid(NEL(value))
      case Right(value) => Valid(value)
    }

  def fromOption[E, A](option: Option[A], ifNone: => E): Validation[E, A] =
    option match {
      case None        => invalid(ifNone)
      case Some(value) => Valid(value)
    }
}
