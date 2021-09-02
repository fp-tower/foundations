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

  def flatMap[E2 >: E, Next](update: A => Validation[E2, Next]): Validation[E2, Next] =
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
  def zip[E2 >: E, Other](other: Validation[E2, Other]): Validation[E2, (A, Other)] =
    ???

  // alias for `zip` followed by `map`.
  def zipWith[E2 >: E, Other, Next](other: Validation[E2, Other])(
    update: (A, Other) => Next
  ): Validation[E2, Next] =
    zip(other).map { case (a, other) => update(a, other) }

  def mapError[E2](update: E => E2): Validation[E2, A] =
    this match {
      case Invalid(value) => Invalid(value.map(update))
      case Valid(value)   => Valid(value)
    }

  def mapErrorAll[E2](update: NEL[E] => NEL[E2]): Validation[E2, A] =
    this match {
      case Invalid(value) => Invalid(update(value))
      case Valid(value)   => Valid(value)
    }
}

object Validation {
  case class Invalid[+E](value: NEL[E]) extends Validation[E, Nothing]
  case class Valid[+A](value: A)        extends Validation[Nothing, A]

  def valid[A](value: A): Validation[Nothing, A] =
    Valid(value)

  def invalid[E](value: E, other: E*): Validation[E, Nothing] =
    Invalid(NEL(value, other.toList))

  // Similar to `Either.cond`
  def cond[E, A](test: Boolean, success: => A, failure: => E): Validation[E, A] =
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

  //////////////////////////////////////////////
  // Bonus question (not covered by the videos)
  //////////////////////////////////////////////

  // Accumulate all errors.
  // sequence(List(1.invalid, 2.valid, 3.invalid)) == Invalid(Nel(1,3))
  def sequence[E, A](validations: List[Validation[E, A]]): Validation[E, List[A]] =
    ???

  // Alias for map + sequence
  def traverse[E, A, Next](values: List[A])(update: A => Validation[E, Next]): Validation[E, List[Next]] =
    sequence(values.map(update))
}
