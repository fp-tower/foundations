package answers.errorhandling.validation

import answers.errorhandling.NEL
import answers.errorhandling.validation.ValidationAnswers.FormError._

object ValidationAnswers {

  def validateCountry(country: String): Validation[FormError, Country] =
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

  def validateUsername(username: String): Validation[FormError, Username] =
    (checkUsernameSize(username), checkUsernameCharacters(username))
      .zipWith((_, _) => Username(username))

  def validateUser(usernameStr: String, countryStr: String): Validation[FieldError, User] =
    (
      form(FieldIds.username)(validateUsername(usernameStr)),
      form(FieldIds.countryOfResidence)(validateCountry(countryStr))
    ).zipWith(User.apply)

  def form[A](fieldId: String)(validation: Validation[FormError, A]): Validation[FieldError, A] =
    validation.mapErrorAll(es => NEL(FieldError(fieldId, es)))

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

  object FieldIds {
    val username           = "username"
    val countryOfResidence = "country_of_residence"
  }

  case class FieldError(fieldId: String, errors: NEL[FormError])

  sealed trait FormError
  object FormError {
    case class InvalidFormat(input: String)        extends FormError
    case class NotSupported(input: String)         extends FormError
    case class TooSmall(inputLength: Int)          extends FormError
    case class InvalidCharacters(char: List[Char]) extends FormError
  }

}
