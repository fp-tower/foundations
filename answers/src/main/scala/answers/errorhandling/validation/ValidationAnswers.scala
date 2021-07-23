package answers.errorhandling.validation

import answers.errorhandling.validation.ValidationAnswers.ValidationError._

object ValidationAnswers {

  def validateCountry(country: String): Validation[ValidationError, Country] =
    if (country.length == 3 && country.forall(c => c.isLetter && c.isUpper))
      Country.all
        .find(_.code == country)
        .toValid(NotSupported(country))
    else
      InvalidFormat(country).invalid

  def checkUsernameSize(username: String): Validation[TooSmall, Unit] =
    Validation.cond(username.length >= 5, success = (), failure = TooSmall(username.length))

  def checkUsernameCharacters(username: String): Validation[InvalidCharacters, Unit] =
    username.toList.filterNot(isValidUsernameCharacter) match {
      case Nil        => ().valid
      case characters => InvalidCharacters(characters).invalid
    }

  def isValidUsernameCharacter(c: Char): Boolean =
    c.isLetter || c.isDigit || c == '_' || c == '-'

  def validateUsername(username: String): Validation[ValidationError, Username] =
    (checkUsernameSize(username), checkUsernameCharacters(username))
      .zipWith((_, _) => Username(username))

  def validateUser(usernameStr: String, countryStr: String): Validation[ValidationError, User] =
    (validateUsername(usernameStr), validateCountry(countryStr)).zipWith(User.apply)

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

}
