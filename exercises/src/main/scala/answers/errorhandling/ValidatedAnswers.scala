package answers.errorhandling

import cats.data.NonEmptyList
import exercises.errorhandling.Validated.{Invalid, Valid}
import exercises.errorhandling.{EitherExercises, Validated}
import toimpl.errorhandling.ValidatedToImpl

object ValidatedAnswers extends ValidatedToImpl {

  type ValidatedNel[+E, +A] = Validated[NonEmptyList[E], A]

  def tuple2[E, A, B](fa: Validated[E, A], fb: Validated[E, B])(combineError: (E, E) => E): Validated[E, (A, B)] =
    (fa, fb) match {
      case (Valid(a), Valid(b))       => Valid((a, b))
      case (Valid(_), Invalid(e))     => Invalid(e)
      case (Invalid(e), Valid(_))     => Invalid(e)
      case (Invalid(e1), Invalid(e2)) => Invalid(combineError(e1, e2))
    }

  def map2[E, A, B, C](fa: Validated[E, A], fb: Validated[E, B])(combineSuccess: (A, B) => C,
                                                                 combineError: (E, E) => E): Validated[E, C] =
    (fa, fb) match {
      case (Valid(a), Valid(b))       => Valid(combineSuccess(a, b))
      case (Valid(_), Invalid(e))     => Invalid(e)
      case (Invalid(e), Valid(_))     => Invalid(e)
      case (Invalid(e1), Invalid(e2)) => Invalid(combineError(e1, e2))
    }

  def tuple2Nel[E, A, B](fa: ValidatedNel[E, A], fb: ValidatedNel[E, B]): ValidatedNel[E, (A, B)] =
    tuple2(fa, fb)(_ ::: _)

  def map2Nel[E, A, B, C](fa: ValidatedNel[E, A], fb: ValidatedNel[E, B])(f: (A, B) => C): ValidatedNel[E, C] =
    map2(fa, fb)(f, _ ::: _)

  def validatePassword(s: String): Validated[EitherExercises.PasswordError, Unit] = ???
}
