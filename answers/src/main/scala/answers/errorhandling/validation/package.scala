package answers.errorhandling
import answers.errorhandling.domain.Nel

package object validation {

  implicit class ValidationExtension[A](value: A) {
    def valid: Validated[Nothing, A]   = Validated.Valid(value)
    def invalid: Validated[A, Nothing] = Validated.Invalid(Nel(value))
  }

  implicit class EitherValidationExtension[E, A](value: Either[E, A]) {
    def toValidated: Validated[E, A] = Validated.fromEither(value)
  }

  implicit class Tuple2ValidationExtension[E, A1, A2](tuple: (Validated[E, A1], Validated[E, A2])) {
    def zip: Validated[E, (A1, A2)] = {
      val (v1, v2) = tuple
      v1.zip(v2)
    }

    def zipWith[Next](update: (A1, A2) => Next): Validated[E, Next] =
      zip.map(update.tupled)
  }

  implicit class Tuple3ValidationExtension[E, A1, A2, A3](
    tuple: (Validated[E, A1], Validated[E, A2], Validated[E, A3])
  ) {
    def zip: Validated[E, (A1, A2, A3)] = {
      val (v1, v2, v3) = tuple
      v1.zip(v2).zip(v3).map { case ((a1, a2), a3) => (a1, a2, a3) }
    }

    def zipWith[Next](update: (A1, A2, A3) => Next): Validated[E, Next] =
      zip.map(update.tupled)
  }

}
