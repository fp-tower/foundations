package answers.errorhandling.either

import answers.errorhandling.either.EitherAnswers2Bis.CountryError._
import answers.errorhandling.either.EitherAnswers2Bis.UsernameError._

object EitherAnswers2Bis {

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

  def validateCountry(country: String): Either[CountryError, Country] =
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

  def validateUsername(username: String): Either[UsernameError, Username] =
    for {
      _ <- checkUsernameSize(username)
      _ <- checkUsernameCharacters(username)
    } yield Username(username)

  // scatsie union type https://scastie.scala-lang.org/k169tqu3TrSGuRb5HTSG3w
  def validateUser(usernameStr: String, countryStr: String): Either[UserError, User] =
    for {
      username <- validateUsername(usernameStr)
      country  <- validateCountry(countryStr)
    } yield User(username, country)

  sealed trait UserError

  sealed trait UsernameError extends UserError
  object UsernameError {
    case class TooSmall(length: Int)               extends UsernameError
    case class InvalidCharacters(char: List[Char]) extends UsernameError
  }

  sealed trait CountryError extends UserError
  object CountryError {
    case class InvalidFormat(country: String) extends CountryError
    case class NotSupported(country: String)  extends CountryError
  }

  //////////////////////////////////
  // Accumulate errors
  //////////////////////////////////

  def validateUserAcc(username: String, country: String): EitherNel[UserError, User] =
    (
      validateUsernameAcc(username),
      validateCountry(country).toEitherNel
    ).zipAccWith(User.apply)

  def validateUsernameAcc(username: String): EitherNel[UsernameError, Username] = {
    val trimmed = username.trim
    (
      checkUsernameSize(trimmed).toEitherNel,
      checkUsernameCharacters(trimmed).toEitherNel
    ).zipAccWith((_, _) => Username(trimmed))
  }

  def sequence[E, A](eithers: List[Either[E, A]]): Either[E, List[A]] =
    eithers
      .foldLeft[Either[E, List[A]]](Right(Nil)) { (state, either) =>
        for {
          list  <- state
          value <- either
        } yield value :: list
      }
      .map(_.reverse)

  def traverse[E, A, B](values: List[A])(transform: A => Either[E, B]): Either[E, List[B]] =
    sequence(values.map(transform))

  def parSequence[E, A](eithers: List[EitherNel[E, A]]): EitherNel[E, List[A]] =
    eithers
      .foldLeft[EitherNel[E, List[A]]](Right(Nil)) { (state, either) =>
        (state, either).zipAccWith((list, value) => value :: list)
      }
      .map(_.reverse)

  def parTraverse[E, A, B](values: List[A])(transform: A => EitherNel[E, B]): EitherNel[E, List[B]] =
    parSequence(values.map(transform))

  def zipAccWith[E, A, B, C](eitherA: EitherNel[E, A], eitherB: EitherNel[E, B])(update: (A, B) => C): EitherNel[E, C] =
    (eitherA, eitherB) match {
      case (Right(a), Right(b))   => Right(update(a, b))
      case (Left(es), Right(_))   => Left(es)
      case (Right(_), Left(es))   => Left(es)
      case (Left(es1), Left(es2)) => Left(es1 ++ es2)
    }

}
