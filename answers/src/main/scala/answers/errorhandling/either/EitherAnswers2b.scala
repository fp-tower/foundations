package answers.errorhandling.either

import answers.errorhandling.either.EitherAnswers2b.ValidationError._

object EitherAnswers2b {

  case class User(username: Username, country: Country)

  case class Username(value: String)

  sealed abstract class Country(val code: String)
  object Country {
    val all: List[Country] = List(France, Germany, Switzerland, UnitedKingdom)

    case object France        extends Country("FRA")
    case object Germany       extends Country("DEU")
    case object Switzerland   extends Country("CHE")
    case object UnitedKingdom extends Country("GBR")
  }

  def validateCountry(country: String): Either[ValidationError, Country] =
    if (country.length == 3 && country.forall(c => c.isLetter && c.isUpper))
      Country.all
        .find(_.code == country)
        .toRight(NotSupported(country))
    else
      Left(InvalidFormat(country))

  def checkUsernameSize(username: String): Either[TooSmall, Unit] =
    if (username.length < 3) Left(TooSmall(username.length)) else Right(())

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

  sealed trait ValidationError
  object ValidationError {
    case class InvalidFormat(input: String)        extends ValidationError
    case class NotSupported(input: String)         extends ValidationError
    case class TooSmall(inputLength: Int)          extends ValidationError
    case class InvalidCharacters(char: List[Char]) extends ValidationError
  }

}
