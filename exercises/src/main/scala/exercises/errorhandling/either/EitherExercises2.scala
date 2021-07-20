package exercises.errorhandling.either

import exercises.errorhandling.either.EitherExercises2.UsernameError._
import exercises.errorhandling.either.EitherExercises2.CountryError._

object EitherExercises2 {

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

  // a. Implement `validateCountry` which takes a 3-letter country code and returns
  // the matching `Country` value (see Alpha-3 code format).
  // `validateCountry` can fail for two reasons:
  // * string format is invalid - we expect 3 upper case letters
  // * the country is not supported by the application
  // For example,
  // validateCountry("FRA") == Right(France)
  // validateCountry("UK")  == Left(InvalidFormat("UK"))
  // validateCountry("ARG") == Left(NotSupported("ARG")), ARG represents Argentina
  def validateCountry(countryCode: String): Either[CountryError, Country] =
    ???

  // b. Implement `checkUsernameSize` which checks if a username is
  // at least 5 characters long. For example,
  // checkUsernameSize("bob_2167") == Right(())
  // checkUsernameSize("bob_2")    == Right(())
  // checkUsernameSize("bo")       == Left(TooSmall(2))
  def checkUsernameSize(username: String): Either[TooSmall, Unit] =
    Either.cond(username.length >= 5, (), TooSmall(username.length))

  // c. Implement `checkUsernameCharacters` which checks if all characters are valid
  // according to the function `isValidUsernameCharacter`. For example,
  // checkUsernameCharacters("_abc-123_")  == Right(())
  // checkUsernameCharacters("foo!~23}AD") == Left(InvalidCharacters(List('!','~','}')))
  def checkUsernameCharacters(username: String): Either[InvalidCharacters, Unit] =
    ???

  def isValidUsernameCharacter(c: Char): Boolean =
    c.isLetter || c.isDigit || c == '_' || c == '-'

  // d. Implement `validateUsername` which verifies the username size and content
  // is correct according to `checkUsernameSize` and `checkUsernameCharacters`.
  // For example,
  // validateUsername("bob_2167")   == Right(Username("bob_2167"))
  // validateUsername("bo")         == Left(TooSmall(2))
  // validateUsername("foo!~23}AD") == Left(InvalidCharacters(List('!','~','}')))
  def validateUsername(username: String): Either[UsernameError, Username] =
    ???

  // e. Implement `validateUser` which verifies that both the username and the country
  // of residence are correct according to `checkUsernameSize` and `checkUsernameCharacters`.
  // What should be the return type of `validateUser`?
  def validateUser(usernameStr: String, countryStr: String) =
    ???

  sealed trait UserError

  sealed trait CountryError extends UserError
  object CountryError {
    case class InvalidFormat(country: String) extends CountryError
    case class NotSupported(country: String)  extends CountryError
  }

  sealed trait UsernameError extends UserError
  object UsernameError {
    case class TooSmall(inputLength: Int)          extends UsernameError
    case class InvalidCharacters(char: List[Char]) extends UsernameError
  }

  sealed trait ValidationError
  object ValidationError {
    case class InvalidFormat(input: String)        extends ValidationError
    case class NotSupported(input: String)         extends ValidationError
    case class TooSmall(inputLength: Int)          extends ValidationError
    case class InvalidCharacters(char: List[Char]) extends ValidationError
  }

}
