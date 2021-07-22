package exercises.errorhandling

package object validation {

  implicit class ValidationExtension[A](value: A) {
    def valid: Validation[Nothing, A]   = Validation.Valid(value)
    def invalid: Validation[A, Nothing] = Validation.Invalid(Nel(value))
  }

  implicit class EitherValidationExtension[E, A](value: Either[E, A]) {
    def toValidated: Validation[E, A] = Validation.fromEither(value)
  }

  implicit class Tuple2ValidationExtension[E, A1, A2](tuple: (Validation[E, A1], Validation[E, A2])) {
    def zip: Validation[E, (A1, A2)] = {
      val (v1, v2) = tuple
      v1.zip(v2)
    }

    def zipWith[Next](update: (A1, A2) => Next): Validation[E, Next] =
      zip.map(update.tupled)
  }

  implicit class Tuple3ValidationExtension[E, A1, A2, A3](
    tuple: (Validation[E, A1], Validation[E, A2], Validation[E, A3])
  ) {
    def zip: Validation[E, (A1, A2, A3)] = {
      val (v1, v2, v3) = tuple
      v1.zip(v2).zip(v3).map { case ((a1, a2), a3) => (a1, a2, a3) }
    }

    def zipWith[Next](update: (A1, A2, A3) => Next): Validation[E, Next] =
      zip.map(update.tupled)
  }

}
