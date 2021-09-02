package answers.errorhandling.validation
import answers.errorhandling.NEL

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

  def zip[E2 >: E, Other](other: Validation[E2, Other]): Validation[E2, (A, Other)] =
    (this, other) match {
      case (Valid(a), Valid(b))         => Valid((a, b))
      case (Invalid(es), Valid(_))      => Invalid(es)
      case (Valid(_), Invalid(es))      => Invalid(es)
      case (Invalid(es1), Invalid(es2)) => Invalid(es1 ++ es2)
    }

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
