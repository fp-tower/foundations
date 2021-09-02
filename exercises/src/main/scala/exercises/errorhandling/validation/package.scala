package exercises.errorhandling

package object validation {

  implicit class ValidationExtension[A](value: A) {
    // 5.valid == Valid(5)
    def valid: Validation[Nothing, A] = Validation.Valid(value)
    // 5.invalid == Invalid(Nel(5))
    def invalid: Validation[A, Nothing] = Validation.Invalid(NEL(value))
  }

  implicit class ValidationNelExtension[A](value: NEL[A]) {
    // Nel(5).invalid == Invalid(Nel(5))
    def invalid: Validation[A, Nothing] = Validation.Invalid(value)
  }

  implicit class EitherValidationExtension[E, A](value: Either[E, A]) {
    // Right(5).toValidation == Valid(5)
    // Left(5).toValidation  == Invalid(Nel(5))
    def toValidation: Validation[E, A] = Validation.fromEither(value)
  }

  implicit class OptionValidationExtension[A](value: Option[A]) {
    // Equivalent of Option#toRight, but for Validation
    // Some(5).toValid("oops") == Valid(5)
    // None.toValid("oops")    == Invalid(Nel("oops"))
    def toValid[E](ifNone: => E): Validation[E, A] = Validation.fromOption(value, ifNone)
  }

  implicit class Tuple2ValidationExtension[E, A1, A2](tuple: (Validation[E, A1], Validation[E, A2])) {
    // (Valid(5), Valid("Hello")).zip == Valid((5, "Hello"))
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
    // (Valid(5), Valid("Hello"), Valid(true)).zip == Valid((5, "Hello", true))
    def zip: Validation[E, (A1, A2, A3)] = {
      val (v1, v2, v3) = tuple
      v1.zip(v2).zip(v3).map { case ((a1, a2), a3) => (a1, a2, a3) }
    }

    def zipWith[Next](update: (A1, A2, A3) => Next): Validation[E, Next] =
      zip.map(update.tupled)
  }

}
