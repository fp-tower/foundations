package answers.errorhandling.validation
import answers.errorhandling.domain.Nel

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
    (this, other) match {
      case (Valid(a), Valid(b))         => Valid((a, b))
      case (Invalid(es), Valid(_))      => Invalid(es)
      case (Valid(_), Invalid(es))      => Invalid(es)
      case (Invalid(es1), Invalid(es2)) => Invalid(es1 ++ es2)
    }
}

object Validated {
  case class Invalid[+E](errors: Nel[E]) extends Validated[E, Nothing]
  case class Valid[+A](value: A)         extends Validated[Nothing, A]

  def fromEither[E, A](either: Either[E, A]): Validated[E, A] =
    either match {
      case Left(value)  => Invalid(Nel(value))
      case Right(value) => Valid(value)
    }
}
