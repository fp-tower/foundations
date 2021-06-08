package answers.errorhandling.either

import answers.errorhandling.either.EitherExercise2.CountryError._
import answers.errorhandling.either.EitherExercise2.UsernameError._

object EitherExercise2 {

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

  def validateCountry(country: String): Either[CountryError, Country] =
    if (country.length == 3 && country.forall(c => c.isLetter && c.isUpper))
      Country.all
        .find(_.code == country)
        .toRight(NotSupported(country))
    else
      Left(InvalidFormat(country))

  def validateUsername(username: String): Either[UsernameError, Username] = {
    val trimmed = username.trim
    for {
      _ <- validateUsernameSize(trimmed)
      _ <- validateUsernameCharacters(trimmed)
    } yield Username(trimmed)
  }

  def validateUsernameSize(username: String): Either[TooSmall, Unit] =
    if (username.length < 3) Left(TooSmall(username.length)) else Right(())

  def validateUsernameCharacters(username: String): Either[InvalidCharacters, Unit] =
    username.toList.filterNot(isValidUsernameCharacter) match {
      case Nil        => Right(())
      case characters => Left(InvalidCharacters(characters))
    }

  def isValidUsernameCharacter(c: Char): Boolean =
    c.isLetter || c.isDigit || c == '_' || c == '-'

  // scatsie union type https://scastie.scala-lang.org/k169tqu3TrSGuRb5HTSG3w
  def validateUser(username: String, country: String): Either[UserError, User] =
    for {
      username <- validateUsername(username)
      country  <- validateCountry(country)
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

  case class SingleFieldError(field: String, reason: String)
  case class AllFieldErrors(byFields: Map[String, List[String]])

  //////////////////////////////////
  // Accumulate errors
  //////////////////////////////////

  def validateUserAcc(username: String, country: String): EitherNel[UserError, User] =
    zipAccWith(
      validateUsernameAcc(username),
      validateCountry(country).toEitherNel
    )(User.apply)

  def validateUsernameAcc(username: String): EitherNel[UsernameError, Username] = {
    val trimmed = username.trim
    zipAccWith(
      validateUsernameSize(trimmed).toEitherNel,
      validateUsernameCharacters(trimmed).toEitherNel
    )((_, _) => Username(trimmed))
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
        zipAccWith(state, either)((list, value) => value :: list)
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
