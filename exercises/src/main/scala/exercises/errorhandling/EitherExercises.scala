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

  // 1a. Implement `getUserEmail` which looks up a user using its id, then it returns the user's email if it exists.
  // val userMap = Map(
  //   222 -> User(222, "john" , "j@x.com"),
  //   123 -> User(123, "elisa", "e@y.com"),
  //   444 -> User(444, "bob")
  // )
  // getUserEmail(123, userMap) == Right("e@y.com")
  // getUserEmail(111, userMap) == Left(UserNotFound(444))
  // getUserEmail(444, userMap) == Left(EmailNotFound(111))
  def getUserEmail(id: UserId, users: Map[UserId, OptionExercises.User]): Either[UserEmailError, Email] = ???

  sealed trait UserEmailError
  object UserEmailError {
    case class UserNotFound(userId: UserId)  extends UserEmailError
    case class EmailNotFound(userId: UserId) extends UserEmailError
  }

  // 1b. Implement `checkout` which encodes the order transition between `Draft` to `Checkout`.
  // `checkout` is successful if the order has a `Draft` status and the basket is not empty.
  // If `checkout` succeeds, it moves the status from `Draft` to `Checkout`.
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
  // `submit` is successful if the order has a `Checkout` status and an address.
  // If `checkout` succeeds, it moves the status from `Checkout` to `Submitted` and stores the submitted timestamp.
  // Bonus: encode the error with an enum.
  def submit(order: Order, now: Instant): Either[String, Order] = ???

  // 1d. Implement `deliver` which encodes the order transition between `Submitted` to `Delivered`.
  // If `deliver` succeeds, it returns a new `Order` with a `Delivered` status and the time it took to deliver the order.
  // Try to find out all error scenarios and eventually encode them with an enum.
  def deliver(order: Order, now: Instant): Either[String, (Order, Duration)] = ???

  //////////////////////////////////
  // 2. Import code with Exception
  //////////////////////////////////

  // 2a. Implement `parseUUID` which safely parses a String into UUID using `UUID.fromString`.
  // `UUID.fromString` is unsafe, it throws an `Exception` if the input string is invalid.
  // Note: You can capture an `Exception` using `try { ... } catch { case t: Throwable => ... }`
  // or using `Try(...)` from `scala.util`
  def parseUUID(uuidStr: String): Either[Throwable, UUID] = ???

  //////////////////////////////////
  // 3. Error ADT
  //////////////////////////////////

  // 3a. Implement `validateUsername` by trimming an input String and then applying the following two validations:
  // `validateUsernameSize` and `validateUsernameCharacters`
  // such as validateUsername("foo")    == Right(Username("foo"))
  //         validateUsername("  foo ") == Right(Username("foo"))
  //         validateUsername("a!bc@£") == Left(InvalidCharacters("!@£"))
  //         validateUsername(" yo")    == Left(TooSmall)
  //         validateUsername(" !o")    == Left(TooSmall)
  // Note: you can use " foo ".trim to remove white spaces at the beginning and at the end of a String.
  def validateUsername(username: String): Either[UsernameError, Username] = ???

  case class Username(value: String)

  // 3b. Implement `validateUsernameSize` which checks if a username is at least 3 characters long
  // such as validateUsernameSize("moreThan3Char") == Right(())
  //         validateUsernameSize("foo") == Right(())
  //         validateUsernameSize("fo") == Left(TooSmall)
  // Note: we assume username has already been trimmed.
  def validateUsernameSize(username: String): Either[TooSmall, Unit] = ???

  // 3c. Implement `validateUsernameCharacters` such as it accepts:
  // * lower and upper case letters
  // * digits
  // * special characters '-' and '_'
  // For example:
  // validateUsernameCharacters("abcABC123-_") == Right(())
  // validateUsernameCharacters("foo!~23}AD") == Left(InvalidCharacters(List('!', '~', '}')))
  // Note: you can use `isValidUsernameCharacter` to check if a character is valid.
  // Note: we assume username has already been trimmed.
  def validateUsernameCharacters(username: String): Either[InvalidCharacters, Unit] = ???

  def isValidUsernameCharacter(c: Char): Boolean =
    c.isLetter || c.isDigit || c == '_' || c == '-'

  sealed trait UsernameError
  object UsernameError {
    case class TooSmall(length: Int)               extends UsernameError
    case class InvalidCharacters(char: List[Char]) extends UsernameError
  }

  // 3d. Implement `validateUser` which validates both username and country using
  // `validateUsername` and `validateCountry`.
  // If both username and country are invalid, only return the username error.
  // What should be the return type of `validateUser`? You may need to create or modify some types.
  def validateUser(username: String, country: String) = ???

  def validateCountry(country: String): Either[CountryError, Country] =
    if (country.length == 3 && country.forall(c => c.isLetter && c.isUpper))
      country match {
        case "FRA" => Right(Country.France)
        case "DEU" => Right(Country.Germany)
        case "CHE" => Right(Country.Switzerland)
        case "GBR" => Right(Country.UnitedKingdom)
        case _     => Left(UnsupportedCountry(country))
      } else Left(InvalidFormat(country))

  case class User(username: Username, country: Country)

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

  //////////////////////////////////
  // 4. Advanced API
  //////////////////////////////////

  // 4a. Implement `map2Acc` which behaves similarly to `map2` but if the two `Either` fail, `map2Acc` accumulates the errors
  // such as map2Acc(Right(1), Right(1))(_ + _) == Right(2)
  // but     map2Acc(Left(List("error 1", "error 2")), Left(List("error a"))) == Left(List("error 1", "error 2", "error a"))
  def map2Acc[E, A, B, C](fa: Either[List[E], A], fb: Either[List[E], B])(f: (A, B) => C): Either[List[E], C] = ???

  def map2[E, A, B, C](fa: Either[E, A], fb: Either[E, B])(f: (A, B) => C): Either[E, C] =
    for {
      a <- fa
      b <- fb
    } yield f(a, b)

  def toListError[E, A](fa: Either[E, A]): Either[List[E], A] =
    fa.left.map(List(_))

  // 4b. Implement `validateUserAcc` which behaves similarly to `validateUser` but this time we should
  // return all errors that occur. For example, we want to know if both username and country are invalid.
  // What should be the return type of `validateUserAcc`?
  // Note: try to use concurrentMap2
  def validateUserAcc(username: String, country: String) = ???

  // 4c. Implement `sequenceAcc` which accumulates successes if all `Either` are `Right` or accumulates
  // failures if at least one `Either` is `Left`.
  // sequenceAcc(List(Right(1), Right(2), Right(3))) == Right(List(1,2,3))
  // sequenceAcc(List(Left(List("e1", "e2")), Right(1), Left(List("e3")))) == Left(List("e1", "e2", "e3"))
  // Note: you may find it useful to reuse `map2Acc`
  def sequenceAcc[E, A](xs: List[Either[List[E], A]]): Either[List[E], List[A]] = ???

}
