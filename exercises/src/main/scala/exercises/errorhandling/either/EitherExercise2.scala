package exercises.errorhandling.either

import exercises.errorhandling.either.EitherExercise2.UsernameError._
import exercises.errorhandling.either.EitherExercise2.CountryError._

object EitherExercise2 {

  case class User(username: Username, residence: Country)
  case class Username(value: String)
  sealed abstract class Country(val code: String)
  object Country {
    val all: List[Country] = List(France, Germany, Switzerland, UnitedKingdom)

    case object France        extends Country("FRA")
    case object Germany       extends Country("DEU")
    case object Switzerland   extends Country("CHE")
    case object UnitedKingdom extends Country("GBR")
  }

  // a. Implement `validateCountry` which takes a 3-letter country and returns
  // the matching `Country` value.
  // `validateCountry` can fail for two reasons:
  // * `countryCode` format is invalid - we expect 3 upper case letters (see Alpha-3 code format)
  // * the country is not supported by the application
  // For example,
  // validateCountry("FRA")   == Right(France)
  // validateCountry("hello") == Right(InvalidFormat("hello"))
  // validateCountry("ARG")   == Right(NotSupported("ARG"))
  def validateCountry(countryCode: String): Either[CountryError, Country] =
    ???

  // b. Implement `validateUsernameSize` which checks if a username is at least
  // 3 characters long. For example,
  // validateUsernameSize("bob_2167") == Right(())
  // validateUsernameSize("bo")       == Left(TooSmall(2))
  def validateUsernameSize(username: String): Either[TooSmall, Unit] =
    ???

  // c. Implement `validateUsernameCharacters` which checks if all characters are valid
  // according to the function `isValidUsernameCharacter`. For example,
  // validateUsernameCharacters("_abc-123_")  == Right(())
  // validateUsernameCharacters("foo!~23}AD") == Left(InvalidCharacters(List('!','~','}')))
  def validateUsernameCharacters(username: String): Either[InvalidCharacters, Unit] =
    ???

  def isValidUsernameCharacter(c: Char): Boolean =
    c.isLetter || c.isDigit || c == '_' || c == '-'

  // d. Implement `validateUsername` which verifies the username size and content
  // is correct according to `validateUsernameSize` and `validateUsernameCharacters`.
  // For example,
  // validateUsername("bob_2167")   == Right(Username("bob_2167"))
  // validateUsername("bo")         == Left(TooSmall(2))
  // validateUsername("foo!~23}AD") == Left(InvalidCharacters(List('!','~','}')))
  def validateUsername(username: String): Either[UsernameError, Username] =
    ???

  // e. Implement `validateUser` which verifies that both the username and the country
  // of residence are correct according to `validateUsernameSize` and `validateUsernameCharacters`.
  // What should be the return type of `validateUser`?
  def validateUser(username: String, residence: String) =
    ???

  sealed trait CountryError
  object CountryError {
    case class InvalidFormat(country: String) extends CountryError
    case class NotSupported(country: String)  extends CountryError
  }

  sealed trait UsernameError
  object UsernameError {
    case class TooSmall(length: Int)               extends UsernameError
    case class InvalidCharacters(char: List[Char]) extends UsernameError
  }

}
