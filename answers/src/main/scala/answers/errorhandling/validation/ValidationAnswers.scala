package answers.errorhandling.validation

import answers.errorhandling.validation.ValidationAnswers.ValidationError._

object ValidationAnswers {

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

  def validateCountry(country: String): Validated[ValidationError, Country] =
    if (country.length == 3 && country.forall(c => c.isLetter && c.isUpper))
      Country.all
        .find(_.code == country)
        .toRight(NotSupported(country))
        .toValidated
    else
      InvalidFormat(country).invalid

  def checkUsernameSize(username: String): Validated[TooSmall, Unit] =
    if (username.length < 3) TooSmall(username.length).invalid
    else ().valid

  def checkUsernameCharacters(username: String): Validated[InvalidCharacters, Unit] =
    username.toList.filterNot(isValidUsernameCharacter) match {
      case Nil        => ().valid
      case characters => InvalidCharacters(characters).invalid
    }

  def isValidUsernameCharacter(c: Char): Boolean =
    c.isLetter || c.isDigit || c == '_' || c == '-'

  def validateUsername(username: String): Validated[ValidationError, Username] =
    (checkUsernameSize(username), checkUsernameCharacters(username))
      .zipWith((_, _) => Username(username))

  def validateUser(usernameStr: String, countryStr: String): Validated[ValidationError, User] =
    (validateUsername(usernameStr), validateCountry(countryStr)).zipWith(User.apply)

  sealed trait ValidationError
  object ValidationError {
    case class InvalidFormat(input: String)        extends ValidationError
    case class NotSupported(input: String)         extends ValidationError
    case class TooSmall(inputLength: Int)          extends ValidationError
    case class InvalidCharacters(char: List[Char]) extends ValidationError
  }

}
