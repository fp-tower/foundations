package exercises.errorhandling.validation

import exercises.errorhandling.validation.ValidationExercises.ValidationError._

object ValidationExercises {

  // 1. Replace all occurrences of `Either` by `Validated` in the code below (taken from EitherExercises2).
  // Note: You can use `valid` or `invalid` extension methods to create `Validated`.
  // For example,
  // 5.valid        == Valid(5)
  // "oops".invalid == Invalid(List("oops"))
  // Note: You can use `toValidated` extension method to transform an Either into a Validated.
  // val result: Either[String, Int] = Right(1)
  // result.toValidated == Valid(1)

  def validateCountry(country: String): Either[ValidationError, Country] =
    if (country.length == 3 && country.forall(c => c.isLetter && c.isUpper))
      Country.all
        .find(_.code == country)
        .toRight(NotSupported(country))
    else
      Left(InvalidFormat(country))

  def checkUsernameSize(username: String): Either[TooSmall, Unit] =
    Either.cond(username.length >= 5, left = TooSmall(username.length), right = ())

  def checkUsernameCharacters(username: String): Either[InvalidCharacters, Unit] =
    username.toList.filterNot(isValidUsernameCharacter) match {
      case Nil        => Right(())
      case characters => Left(InvalidCharacters(characters))
    }

  def isValidUsernameCharacter(c: Char): Boolean =
    c.isLetter || c.isDigit || c == '_' || c == '-'

  def validateUsername(username: String): Either[ValidationError, Username] =
    for {
      _ <- checkUsernameSize(username)
      _ <- checkUsernameCharacters(username)
    } yield Username(username)

  def validateUser(usernameStr: String, countryStr: String): Either[ValidationError, User] =
    for {
      username <- validateUsername(usernameStr)
      country  <- validateCountry(countryStr)
    } yield User(username, country)

  case class User(username: Username, countryOfResidence: Country)
  case class Username(value: String)

  sealed abstract class Country(val code: String)
  object Country {
    val all: List[Country] = List(France, Germany, Switzerland, UnitedKingdom)

    case object France        extends Country("FRA")
    case object Germany       extends Country("DEU")
    case object Switzerland   extends Country("CHE")
    case object UnitedKingdom extends Country("GBR")
  }

  sealed trait ValidationError
  object ValidationError {
    case class InvalidFormat(input: String)        extends ValidationError
    case class NotSupported(input: String)         extends ValidationError
    case class TooSmall(inputLength: Int)          extends ValidationError
    case class InvalidCharacters(char: List[Char]) extends ValidationError
  }

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
