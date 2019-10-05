package exercises.errorhandling

import java.time.{Duration, Instant}
import java.util.UUID

import exercises.errorhandling.EitherExercises.CountryError.{InvalidFormat, UnsupportedCountry}
import exercises.errorhandling.EitherExercises.UsernameError.{InvalidCharacters, TooSmall}
import exercises.errorhandling.EitherExercises.UserEmailError.{EmailNotFound, UserNotFound}
import exercises.errorhandling.OptionExercises.{Email, UserId}

import scala.util.Try

object EitherExercises {

  ////////////////////////
  // 1. Use cases
  ////////////////////////

  // 1a. Implement `getUserEmail` which looks a user by its id then return user's email if it exists
  // val userMap = Map(
  //   222 -> User(222, "john" , "j@x.com"),
  //   123 -> User(123, "elisa", "e@y.com"),
  //   444 -> User(444, "bob")
  // )
  // getUserEmail(123, userMap) == Right("e@y.com")
  // getUserEmail(444, userMap) == Left(UserNotFound(444))
  // getUserEmail(111, userMap) == Left(EmailNotFound(111))
  def getUserEmail(id: UserId, users: Map[UserId, OptionExercises.User]): Either[UserEmailError, Email] = ???

  sealed trait UserEmailError
  object UserEmailError {
    case class UserNotFound(userId: UserId)  extends UserEmailError
    case class EmailNotFound(userId: UserId) extends UserEmailError
  }

  // 1b. Implement `checkout` which encodes the order transition between `Draft` to `Checkout`.
  // For `checkout` to be successful it needs to have a `Draft` status and have at least one item in its basket.
  // If `checkout` succeeds, it move the status from `Draft` to `Checkout`.
  // Bonus: encode the error with an enum.
  def checkout(order: Order): Either[String, Order] = ???

  case class Item(id: String, quantity: Int, unitPrice: Double)
  case class Order(
    id: String,
    status: String, // Draft | Checkout | Submitted | Delivered
    basket: List[Item],
    deliveryAddress: Option[String],
    submittedAt: Option[Instant],
    deliveredAt: Option[Instant]
  )

  // 1c. Implement `submit` which encodes the order transition between `Checkout` to `Submitted`.
  // For `submit` to be successful it needs to have a `Checkout` status and an address.
  // If `checkout` succeeds, it move the status from `Checkout` to `Submitted` and stores the submitted timestamp.
  // Bonus: encode the error with an enum.
  def submit(order: Order, now: Instant): Either[String, Order] = ???

  // 1d. Implement `deliver` which encodes the order transition between `Submitted` to `Delivered`.
  // If `deliver` succeeds, it returns a new `Order` with a `Delivered` status and the time it took to deliver it.
  // Try to find out all error scenarios and eventually encode them with an enum.
  def deliver(order: Order, now: Instant): Either[String, (Order, Duration)] = ???

  //////////////////////////////////
  // 2. Import code with Exception
  //////////////////////////////////

  // 2a. Implement `parseUUID` which safely parse a String into UUID using `UUID.fromString`.
  // `UUID.fromString` is unsafe, it throws Exception if the input string is invalid.
  // Note: You can capture Exception using try { ... } catch { case t: Throwable => ... }
  // or using `Try(...)` from `scala.util`
  def parseUUID(uuidStr: String): Either[Throwable, UUID] = ???

  //////////////////////////////////
  // 3. Advanced API
  //////////////////////////////////

  // 3a. Implement `validateUsername` by trimming username and then applying both
  // `validateUsernameSize` and `validateUsernameCharacters`
  // such as validateUsername("foo")    == Right(Username("foo"))
  //         validateUsername("  foo ") == Right(Username("foo"))
  //         validateUsername("a!bc@£") == Left(InvalidCharacters("!@£"))
  //         validateUsername(" yo")    == Left(TooSmall)
  //         validateUsername(" !o")    == Left(TooSmall)
  // Note: you can use " foo ".trim to remove white spaces at the beginning and at the end
  def validateUsername(username: String): Either[UsernameError, Username] = ???

  case class Username(value: String)

  // 3b. Implement `validateUsernameSize` (at least 3 characters long)
  // such as validateUsernameSize("moreThan3Char") == Right(())
  //         validateUsernameSize("foo") == Right(())
  //         validateUsernameSize("fo") == Left(TooSmall)
  def validateUsernameSize(username: String): Either[TooSmall, Unit] = ???

  // 3c. Implement `validateUsernameCharacters` such as it accepts:
  // * lower and upper case letters
  // * digits
  // * special characters '-' and '_'
  // For example:
  // validateUsernameCharacters("abcABC123-_") == Right(())
  // validateUsernameCharacters("foo!~23}AD") == Left(InvalidCharacters(List('!', '~', '}')))
  // Note: you can use `isValidUsernameCharacter` to check if a character is valid
  def validateUsernameCharacters(username: String): Either[InvalidCharacters, Unit] = ???

  def isValidUsernameCharacter(c: Char): Boolean =
    c.isLetter || c.isDigit || c == '_' || c == '-'

  sealed trait UsernameError
  object UsernameError {
    case class TooSmall(length: Int)               extends UsernameError
    case class InvalidCharacters(char: List[Char]) extends UsernameError
  }

  // 3d. Implement `validateUser` which validates both username and country using
  // `validateUsername` and `validateCountry` (defined below).
  // If both username and country are invalid, only return the username error.
  // What should be the return type of `validateUser`? You may need to create or modify some types.
  def validateUser(username: String, country: String) = ???

  case class User(userName: Username, country: Country)

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

  sealed trait CountryError
  object CountryError {
    case class InvalidFormat(country: String)      extends CountryError
    case class UnsupportedCountry(country: String) extends CountryError
  }

  // 3e. Implement `validateUserPar` which behaves similarly to `validateUser` but this time we should
  // return all errors that occur. For example, we want to know if both username and country is invalid.
  // What should be the return type of `validateUserPar`?
  def validateUserPar(username: String, country: String) = ???

}
