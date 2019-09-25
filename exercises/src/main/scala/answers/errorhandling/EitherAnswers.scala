package answers.errorhandling

import exercises.errorhandling.EitherExercises.CountryError.{InvalidFormat, UnsupportedCountry}
import exercises.errorhandling.EitherExercises.UsernameError._
import exercises.errorhandling.EitherExercises.{CountryError, GetOrderError, Order, UsernameError}
import exercises.errorhandling.{Country, User, Username}
import toimpl.errorhandling.EitherToImpl

object EitherAnswers extends EitherToImpl {

  ////////////////////////
  // 1. Use cases
  ////////////////////////
  def getOrder(id: Int, users: List[Order]): Either[GetOrderError, Order] =
    users.filter(_.id == id) match {
      case Nil      => Left(GetOrderError.OrderNotFound)
      case x :: Nil => Right(x)
      case _ :: _   => Left(GetOrderError.NonUniqueOrderId)
    }

  def validateUsernameSize(username: String): Either[TooSmall.type, Unit] =
    if (username.length < 3) Left(TooSmall) else Right(())

  def validateUsernameCharacter(c: Char): Either[InvalidCharacter, Unit] =
    if (OptionAnswers.isValidUsernameCharacter(c)) Right(())
    else Left(InvalidCharacter(c))

  def validateUsernameContent(username: String): Either[InvalidCharacter, Unit] =
    username.toList.foldRight[Either[InvalidCharacter, Unit]](Right(()))(
      (c, acc) =>
        (validateUsernameCharacter(c), acc) match {
          case (Right(_), Right(_)) => Right(())
          case (Left(e), Right(_))  => Left(e)
          case (Right(_), Left(e))  => Left(e)
          case (Left(e), Left(_))   => Left(e)
      }
    )

  def validateUsername(username: String): Either[UsernameError, Username] = {
    val trimmed = username.trim
    (validateUsernameSize(trimmed), validateUsernameContent(trimmed)) match {
      case (Right(_), Right(_)) => Right(Username(trimmed))
      case (Left(e), Right(_))  => Left(e)
      case (Right(_), Left(e))  => Left(e)
      case (Left(e), Left(_))   => Left(e)
    }
  }

  def validateCountry(country: String): Either[CountryError, Country] =
    if (country.length == 3 && country.forall(c => c.isLetter && c.isUpper))
      OptionAnswers.validateCountry(country).toRight(UnsupportedCountry)
    else Left(InvalidFormat)

  ////////////////////////
  // 2. Composing Either
  ////////////////////////

  def leftMap[E, A, B](fa: Either[E, A])(f: E => B): Either[B, A] =
    fa match {
      case Left(e)  => Left(f(e))
      case Right(a) => Right(a)
    }

  def tuple2[E, A, B](fa: Either[E, A], fb: Either[E, B]): Either[E, (A, B)] =
    (fa, fb) match {
      case (Right(a), Right(b)) => Right((a, b))
      case (Left(e), Right(_))  => Left(e)
      case (Right(_), Left(e))  => Left(e)
      case (Left(e), Left(_))   => Left(e)
    }

  def tuple3[E, A, B, C](fa: Either[E, A], fb: Either[E, B], fc: Either[E, C]): Either[E, (A, B, C)] =
    tuple2(tuple2(fa, fb), fc).map { case ((a, b), c) => (a, b, c) }

  def map2[E, A1, A2, B](fa: Either[E, A1], fb: Either[E, A2])(f: (A1, A2) => B): Either[E, B] =
    (fa, fb) match {
      case (Right(a), Right(b)) => Right(f(a, b))
      case (Left(e), Right(_))  => Left(e)
      case (Right(_), Left(e))  => Left(e)
      case (Left(e), Left(_))   => Left(e)
    }

  def validateUsername_v2(username: String): Either[UsernameError, Username] = {
    val trimmed = username.trim
    map2(validateUsernameSize(trimmed), validateUsernameContent(username))((_, _) => Username(trimmed))
  }

  def validateUsername_v3(username: String): Either[UsernameError, Username] = {
    val trimmed = username.trim
    for {
      _ <- validateUsernameSize(trimmed)
      _ <- validateUsernameContent(username)
    } yield Username(trimmed)
  }

  def sequence[E, A](fa: List[Either[E, A]]): Either[E, List[A]] =
    fa.foldRight[Either[E, List[A]]](Right(Nil))((a, acc) => map2(a, acc)(_ :: _))

  def validateUsernameContent_v2(username: String): Either[InvalidCharacter, Unit] =
    sequence(username.toList.map(validateUsernameCharacter)).map(_ => ())

  def traverse[E, A, B](fa: List[A])(f: A => Either[E, B]): Either[E, List[B]] =
    fa.foldRight[Either[E, List[B]]](Right(Nil))((a, acc) => map2(f(a), acc)(_ :: _))

  def validateUsernameContent_v3(username: String): Either[InvalidCharacter, Unit] =
    traverse(username.toList)(validateUsernameCharacter).map(_ => ())

  def traverse_[E, A, B](fa: List[A])(f: A => Either[E, B]): Either[E, Unit] =
    fa.foldRight[Either[E, Unit]](Right(()))((a, acc) => map2(f(a), acc)((_, _) => ()))

  def validateUsernameContent_v4(username: String): Either[InvalidCharacter, Unit] =
    traverse_(username.toList)(validateUsernameCharacter)

  ////////////////////////
  // 3. Error message
  ////////////////////////

  def validateUserMessage(username: String, country: String): String =
    (validateUsername(username), validateCountry(country)) match {
      case (Right(x), Right(y)) => User(x, y).toString
      case (x, y) =>
        leftMap(
          sequence(
            List(
              leftMap(x)(e => s"Invalid username, error: $e"),
              leftMap(y)(e => s"Invalid country, error: $e")
            )
          )
        )(es => s"Invalid User: ${es.mkString(", ")}").fold(identity, _ => "")
    }
}
