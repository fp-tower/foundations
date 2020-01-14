package answers.errorhandling

import java.time.{Duration, Instant}
import java.util.UUID

import answers.errorhandling.EitherAnswers.CountryError.{InvalidFormat, UnsupportedCountry}
import answers.errorhandling.EitherAnswers.UserEmailError.{EmailNotFound, UserNotFound}
import answers.errorhandling.EitherAnswers.UsernameError.{InvalidCharacters, TooSmall}
import answers.errorhandling.OptionAnswers.{Email, UserId}

import scala.util.Try

object EitherAnswers {

  ////////////////////////
  // 1. Use cases
  ////////////////////////

  def getUserEmail(id: UserId, users: Map[UserId, OptionAnswers.User]): Either[UserEmailError, Email] =
    for {
      user  <- users.get(id).toRight(UserNotFound(id))
      email <- user.email.toRight(EmailNotFound(id))
    } yield email

  sealed trait UserEmailError
  object UserEmailError {
    case class UserNotFound(userId: UserId)  extends UserEmailError
    case class EmailNotFound(userId: UserId) extends UserEmailError
  }

  def checkout(order: Order): Either[OrderError, Order] =
    order.status match {
      case "Draft" =>
        if (order.basket.isEmpty) Left(OrderError.EmptyBasket)
        else Right(order.copy(status = "Checkout"))
      case other =>
        Left(OrderError.InvalidStatus("checkout", other))
    }

  case class Item(id: String, quantity: Int, unitPrice: Double)
  case class Order(
    id: String,
    status: String, // Draft | Checkout | Submitted | Delivered
    basket: List[Item],
    deliveryAddress: Option[String],
    submittedAt: Option[Instant],
    deliveredAt: Option[Instant]
  )

  def submit(order: Order, now: Instant): Either[OrderError, Order] =
    order.status match {
      case "Checkout" =>
        if (order.deliveryAddress.isEmpty) Left(OrderError.MissingDeliveryAddress)
        else Right(order.copy(status = "Submitted", submittedAt = Some(now)))
      case other =>
        Left(OrderError.InvalidStatus("submit", other))
    }

  def deliver(order: Order, now: Instant): Either[OrderError, (Order, Duration)] =
    order.status match {
      case "Submitted" =>
        order.submittedAt match {
          case Some(submittedTimestamp) =>
            val deliveryDuration = Duration.between(submittedTimestamp, now)
            val newOrder         = order.copy(status = "Delivered", deliveredAt = Some(now))
            Right((newOrder, deliveryDuration))
          case None => Left(OrderError.MissingSubmittedTimestamp)
        }
      case other =>
        Left(OrderError.InvalidStatus("deliver", other))
    }

  sealed trait OrderError
  object OrderError {
    case object EmptyBasket                                         extends OrderError
    case object MissingDeliveryAddress                              extends OrderError
    case object MissingSubmittedTimestamp                           extends OrderError
    case class InvalidStatus(action: String, currentStatus: String) extends OrderError
  }

  //////////////////////////////////
  // 2. Import code with Exception
  //////////////////////////////////

  def parseUUID(uuidStr: String): Either[Throwable, UUID] =
    Try(UUID.fromString(uuidStr)).toEither

  //////////////////////////////////
  // 3. Error ADT
  //////////////////////////////////

  def validateUsername(username: String): Either[UsernameError, Username] = {
    val trimmed = username.trim
    for {
      _ <- validateUsernameSize(trimmed)
      _ <- validateUsernameCharacters(trimmed)
    } yield Username(trimmed)
  }

  case class Username(value: String)

  def validateUsernameSize(username: String): Either[TooSmall, Unit] =
    if (username.length < 3) Left(TooSmall(username.length)) else Right(())

  def validateUsernameCharacters(username: String): Either[InvalidCharacters, Unit] =
    username.toList.filterNot(isValidUsernameCharacter) match {
      case Nil => Right(())
      case xs  => Left(InvalidCharacters(xs))
    }

  def isValidUsernameCharacter(c: Char): Boolean =
    c.isLetter || c.isDigit || c == '_' || c == '-'

  sealed trait UserError

  sealed trait UsernameError extends UserError
  object UsernameError {
    case class TooSmall(length: Int)               extends UsernameError
    case class InvalidCharacters(char: List[Char]) extends UsernameError
  }

  sealed trait CountryError extends UserError
  object CountryError {
    case class InvalidFormat(country: String)      extends CountryError
    case class UnsupportedCountry(country: String) extends CountryError
  }

  def validateUser(username: String, country: String): Either[UserError, User] =
    for {
      username <- validateUsername(username)
      country  <- validateCountry(country)
    } yield User(username, country)

  case class User(username: Username, country: Country)

  def validateCountry(country: String): Either[CountryError, Country] =
    if (country.length == 3 && country.forall(c => c.isLetter && c.isUpper))
      country match {
        case "FRA" => Right(Country.France)
        case "DEU" => Right(Country.Germany)
        case "CHE" => Right(Country.Switzerland)
        case "GBR" => Right(Country.UnitedKingdom)
        case _     => Left(UnsupportedCountry(country))
      } else Left(InvalidFormat(country))

  sealed trait Country
  object Country {
    case object France        extends Country
    case object Germany       extends Country
    case object Switzerland   extends Country
    case object UnitedKingdom extends Country
  }

  //////////////////////////////////
  // 4. Advanced API
  //////////////////////////////////

  def map2Acc[E, A, B, C](fa: Either[List[E], A], fb: Either[List[E], B])(f: (A, B) => C): Either[List[E], C] =
    (fa, fb) match {
      case (Right(a), Right(b))   => Right(f(a, b))
      case (Left(es), Right(_))   => Left(es)
      case (Right(_), Left(es))   => Left(es)
      case (Left(es1), Left(es2)) => Left(es1 ++ es2)
    }

  def validateUserAcc(username: String, country: String): Either[List[UserError], User] =
    map2Acc(
      validateUsernameAcc(username),
      validateCountry(country).left.map(List(_))
    )(User)

  def validateUsernameAcc(username: String): Either[List[UsernameError], Username] = {
    val trimmed = username.trim
    map2Acc(
      validateUsernameSize(trimmed).left.map(List(_)),
      validateUsernameCharacters(trimmed).left.map(List(_))
    )((_, _) => Username(trimmed))
  }

  def sequenceAcc[E, A](xs: List[Either[List[E], A]]): Either[List[E], List[A]] =
    xs.foldLeft[Either[List[E], List[A]]](Right(Nil))(map2Acc(_, _)((acc, a) => a :: acc)).map(_.reverse)

}
