package exercises.errorhandling.validation

import exercises.errorhandling.NEL
import exercises.errorhandling.validation.Validation._
import exercises.errorhandling.validation.ValidationExercises.ValidationError._

object ValidationExercises {

  // 1. Copy-paste `validateCountry` from `EitherExercises2` and adapt it to `Validation`.
  // For example,
  // validateCountry("FRA") == Valid(France)
  // validateCountry("UK")  == Invalid(NEL(InvalidFormat("UK")))
  // validateCountry("ARG") == Invalid(NEL(NotSupported("ARG")))
  // Note: You can find several helpers methods in the companion object of Validation,
  //       as well as many extension methods in `package.scala`.
  def validateCountry(country: String): Validation[ValidationError, Country] =
    ???

  // 2. Copy-paste `checkUsernameSize` from `EitherExercises2` and adapt it to `Validation`.
  def checkUsernameSize(username: String): Validation[TooSmall, Unit] =
    ???

  // 3. Copy-paste `checkUsernameCharacters` from `EitherExercises2` and adapt it to `Validation`.
  def checkUsernameCharacters(username: String): Validation[InvalidCharacters, Unit] =
    ???

  def isValidUsernameCharacter(c: Char): Boolean =
    c.isLetter || c.isDigit || c == '_' || c == '-'

  // 4. Copy-paste `validateUsername` from `EitherExercises2` and adapt it to `Validation`.
  // In this version, we would like to know if the username is both too small and
  // contains invalid characters.
  // Note: Check the methods `zip` and `zipWith` inside `Validation`.
  def validateUsername(username: String): Validation[ValidationError, Username] =
    ???

  // 5. Implement `validateUser` so that it reports all errors.
  def validateUser(usernameStr: String, countryStr: String): Validation[ValidationError, User] =
    ???

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

  // 3. Update `validateUser` so that it accumulate errors. For example,
  // validateUser("bo", "ARG") == Invalid(List(TooSmall(2), NotSupported("ARG")))
  // Note: You can use `zip` extension method on tuples.
  // For example,
  // ("error1".invalid, "error2".invalid).zip == Invalid(List("error1", "error2"))
  // (1.invalid, "hello".invalid).zip         == Valid((1, "Hello"))
  // Note: You can use `zipWith` extension method on tuples which is a combination of `zip` followed by `map`.
  // (1.invalid, 2.invalid).zipWith(_ + _) == Valid(3)

}
