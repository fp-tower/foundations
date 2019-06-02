package toimpl.errorhandling

import cats.data.NonEmptyList
import exercises.errorhandling.EitherExercises.UsernameError
import exercises.errorhandling.Validated

trait ValidatedToImpl {

  def tuple2[E, A, B](fa: Validated[E, A], fb: Validated[E, B])(combineError: (E, E) => E): Validated[E, (A, B)]

  def map2[E, A, B, C](fa: Validated[E, A], fb: Validated[E, B])(
    combineSuccess: (A, B) => C,
    combineError: (E, E) => E
  ): Validated[E, C]

  def tuple2Nel[E, A, B](
    fa: Validated[NonEmptyList[E], A],
    fb: Validated[NonEmptyList[E], B]
  ): Validated[NonEmptyList[E], (A, B)]

  def map2Nel[E, A, B, C](fa: Validated[NonEmptyList[E], A], fb: Validated[NonEmptyList[E], B])(
    f: (A, B) => C
  ): Validated[NonEmptyList[E], C]

  // 2a. Implement validatePassword
  def validatePassword(s: String): Validated[UsernameError, Unit]

}
