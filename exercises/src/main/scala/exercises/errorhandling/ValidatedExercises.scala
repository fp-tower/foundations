package exercises.errorhandling

import cats.data.NonEmptyList
import exercises.errorhandling.EitherExercises.PasswordError
import exercises.errorhandling.Validated.{Invalid, Valid}
import toimpl.errorhandling.ValidatedToImpl

object ValidatedExercises extends ValidatedToImpl {

  def map[E, A, B](fa: Validated[E, A])(f: A => B): Validated[E, B] =
    fa match {
      case Invalid(e) => Invalid(e)
      case Valid(a)   => Valid(f(a))
    }

  def leftMap[E, A, B](fa: Validated[E, A])(f: E => B): Validated[B, A] =
    fa match {
      case Invalid(e) => Invalid(f(e))
      case Valid(a)   => Valid(a)
    }

  // 1a. Implement tuple2 using pattern matching
  // such as tuple2(Valid(1), Valid("foo"))(???) == Valid((1, "foo"))
  // but     tuple2(Invalid(1), Valid("foo"))(???) == Invalid(1)
  // but     tuple2(Invalid("error1"), Invalid("error2"))(_ ++ _) == Invalid("error1error2")
  def tuple2[E, A, B](fa: Validated[E, A], fb: Validated[E, B])(combineError: (E, E) => E): Validated[E, (A, B)] = ???

  // 1b. Implement tuple2 using pattern matching
  // such as map2(Valid(3), Valid(2))(_ * _, ???) == Valid(6)
  // but     map2(Invalid(1), Valid("foo"))(???, ???) == Invalid(1)
  // but     map2(Invalid("error1"), Invalid("error2"))(???, _ ++ _) == Invalid("error1error2")
  def map2[E, A, B, C](fa: Validated[E, A], fb: Validated[E, B])(combineSuccess: (A, B) => C,
                                                                 combineError: (E, E) => E): Validated[E, C] = ???

  // 1c. Implement tuple2Nel
  // such as tuple2Nel(Valid(1), Valid("foo")) == Valid((1, "foo"))
  // but     tuple2Nel(Invalid(NonEmptyList.of(1)), Valid("foo")) == Invalid(NonEmptyList.of(1))
  //         tuple2Nel(Invalid(NonEmptyList.of("error1")), Invalid(NonEmptyList.of("error2"))) == Invalid(NonEmptyList.of("error1", "error2"))
  type ValidatedNel[+E, +A] = Validated[NonEmptyList[E], A]

  def tuple2Nel[E, A, B](fa: ValidatedNel[E, A], fb: ValidatedNel[E, B]): ValidatedNel[E, (A, B)] = ???

  // 1c. Implement map2Nel
  def map2Nel[E, A, B, C](fa: ValidatedNel[E, A], fb: ValidatedNel[E, B])(f: (A, B) => C): ValidatedNel[E, C] = ???

  // 2a. Implement validatePassword
  def validatePassword(s: String): Validated[PasswordError, Unit] = ???

}
