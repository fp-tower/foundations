package exercises.errorhandling.validation

object ValidationExercises {

  // 1. Copy-Paste all code (methods + classes) from either.EitherExercises2 into this object.
  // Then, replace all occurrences of `Either` by `Validated`.
  // Note: You can use `valid` or `invalid` extension methods to create `Validated`.
  // For example,
  // 5.valid        == Valid(5)
  // "oops".invalid == Invalid(List("oops"))
  // Note: You can use `toValidated` extension method to transform an Either into a Validated.
  // val result: Either[String, Int] = Right(1)
  // result.toValidated == Valid(1)

  // 2. Implement the method `zip` in the class `Validated` so that it accumulated errors.
  // For example,
  // "error1".invalid.zip("error2".invalid) == Invalid(List("error1", "error2"))
  // "error1".invalid.zip("Hello".valid)    == Invalid(List("error1"))
  // 1.valid.zip("error2".invalid)          == Invalid(List("error2"))
  // 1.valid.zip("Hello".valid)             == Valid((1, "Hello"))

  // 3. Update `validateUser` so that it accumulate errors. For example,
  // validateUser("bo", "ARG") == Invalid(List(TooSmall(2), NotSupported("ARG")))
  // Note: You can use `zip` extension method on tuples.
  // For example,
  // ("error1".invalid, "error2".invalid).zip == Invalid(List("error1", "error2"))
  // (1.invalid, "hello".invalid).zip         == Valid((1, "Hello"))
  // Note: You can use `zipWith` extension method on tuples which is a combination of `zip` followed by `map`.
  // (1.invalid, 2.invalid).zipWith(_ + _) == Valid(3)

}
