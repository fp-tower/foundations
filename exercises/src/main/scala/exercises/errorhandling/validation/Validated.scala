package exercises.errorhandling.validation

sealed trait Validated[+E, +A] {
  import Validated._

  def map[Next](update: A => Next): Validated[E, Next] =
    this match {
      case Invalid(errors) => Invalid(errors)
      case Valid(value)    => Valid(update(value))
    }

  def flatMap[EE >: E, Next](update: A => Validated[EE, Next]): Validated[EE, Next] =
    this match {
      case Invalid(errors) => Invalid(errors)
      case Valid(value)    => update(value)
    }

  def zip[EE >: E, Other](other: Validated[EE, Other]): Validated[EE, (A, Other)] =
    ???
}

object Validated {
  case class Invalid[+E](errors: List[E]) extends Validated[E, Nothing]
  case class Valid[+A](value: A)          extends Validated[Nothing, A]

  def fromEither[E, A](either: Either[E, A]): Validated[E, A] =
    either match {
      case Left(value)  => Invalid(List(value))
      case Right(value) => Valid(value)
    }
}
