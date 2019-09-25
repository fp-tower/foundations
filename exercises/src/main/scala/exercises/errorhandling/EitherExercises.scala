package exercises.errorhandling

import exercises.errorhandling.EitherExercises.UsernameError.{InvalidCharacter, TooSmall}
import toimpl.errorhandling.EitherToImpl

import scala.util.Try

object EitherExercises extends EitherToImpl {

  ////////////////////////
  // 1. Use cases
  ////////////////////////

  // 1a. Implement getOrder
  // such as getOrder(123, List(Order(222, "paul"), Order(123, "john"))) == Right(Order(123, "john"))
  // but getOrder(111, List(Order(222, "paul"), Order(123, "john"))) == Left(OrderNotFound)
  //     getOrder(123, List(Order(123, "paul"), Order(123, "john"))) == Left(NonUniqueOrderId)
  case class Order(id: Int, name: String)

  sealed trait GetOrderError
  object GetOrderError {
    case object OrderNotFound    extends GetOrderError
    case object NonUniqueOrderId extends GetOrderError
  }

  def getOrder(id: Int, users: List[Order]): Either[GetOrderError, Order] = ???

  // 1b. Implement validateUsernameSize (at least 3 characters long)
  // such as validateUsernameSize("moreThan3Char") == Right(())
  //         validateUsernameSize("foo") == Right(())
  //         validateUsernameSize("fo") == Left(TooSmall)
  sealed trait UsernameError
  object UsernameError {
    case object TooSmall                    extends UsernameError
    case class InvalidCharacter(char: Char) extends UsernameError
  }

  def validateUsernameSize(username: String): Either[TooSmall.type, Unit] = ???

  // 1c. Implement validateUsernameCharacter such as it accepts:
  // * lower and upper case letters
  // * digits
  // * special characters '-' and '_'
  def validateUsernameCharacter(c: Char): Either[InvalidCharacter, Unit] = ???

  // 1d. Implement validateUsernameContent such as:
  // * it returns Right(()) if all characters are valid
  //    e.g. validateUsernameContent("Foo1-2_") == Right(())
  // * it returns a Left of the first invalid character
  //   e.g. validateUsernameContent("!(Foo)") == Left(InvalidCharacter('!'))
  def validateUsernameContent(username: String): Either[InvalidCharacter, Unit] =
    username.toList.foldRight[Either[InvalidCharacter, Unit]](Right(()))(
      (c: Char, acc: Either[InvalidCharacter, Unit]) => ???
    )

  // 1e. Implement validateUsername which trims username and then validate size and content
  // such as validateUsername("foo") == Right(Username("foo"))
  //         validateUsername("  foo ") == Right(Username("foo"))
  //         validateUsername("abc!@£") == Left(InvalidCharacter('!'))
  //         validateUsername(" yo")    == Left(TooSmall)
  // Use pattern matching
  def validateUsername(username: String): Either[UsernameError, Username] = ???

  // 1f. Implement validate Country such as it parses a 3 letter country code into a Country enumeration
  // see https://www.iban.com/country-codes
  // e.g. validateCountry("FRA") == Right(France)
  //      validateCountry("foo") == Left(InvalidFormat)
  //      validateCountry("FRANCE") == Left(InvalidFormat)
  //      validateCountry("DZA") == Left(UnsupportedCountry)
  sealed trait CountryError
  object CountryError {
    case object InvalidFormat      extends CountryError
    case object UnsupportedCountry extends CountryError
  }
  def validateCountry(country: String): Either[CountryError, Country] = ???

  ////////////////////////
  // 2. Composing Either
  ////////////////////////

  def map[E, A, B](fa: Either[E, A])(f: A => B): Either[E, B] =
    fa.map(f)

  // 2a. Implement leftMap
  // leftMap(Left(List(1,2,3)))(xs => 0 :: xs) == Left(List(0,1,2,3))
  def leftMap[E, A, B](fa: Either[E, A])(f: E => B): Either[B, A] = ???

  // 2b. Implement tuple2 using pattern matching
  // such as tuple2(Right(1), Right("foo")) == Right((1, "foo"))
  // but     tuple2(Left("error1"), Left("error2")) == Left("error1")
  def tuple2[E, A, B](fa: Either[E, A], fb: Either[E, B]): Either[E, (A, B)] = ???

  // 2c. Implement map2
  // such as map2(Right(1), Right("foo"))(_.toString + _) == Right("1foo")
  //         map2(Left("error1"), Right("error2"))(_.toString + _) == Left("error1")
  def map2[E, A1, A2, B](fa: Either[E, A1], fb: Either[E, A2])(f: (A1, A2) => B): Either[E, B] = ???

  // 2d. Implement validateUsername_v2 using tuple2 or map2
  def validateUsername_v2(s: String): Either[UsernameError, Username] = ???

  // 2e. Implement validateUsername_v3 using flatMap
  def flatMap[E, A, B](fa: Either[E, A])(f: A => Either[E, B]): Either[E, B] = fa.flatMap(f)

  def validateUsername_v3(s: String): Either[UsernameError, Username] = ???

  // 2e. Implement sequence
  // such as sequence(List(Right(1), Right(5), Right(12))) == Right(List(1,5,12))
  // but     sequence(List(Right(1), Left("error"), Right(12))) == Left("error")
  def sequence[E, A](fa: List[Either[E, A]]): Either[E, List[A]] = ???

  // 2k. Implement validateUsernameContent_v2 using traverse
  def validateUsernameContent_v2(username: String): Either[InvalidCharacter, Unit] = ???

  // 2g. Implement traverse
  // such as traverse(List("1", "23", "54"))(parseStringToInt) == Right(List(1,23,54))
  //         traverse(List.empty[String])(parseStringToInt) == Right(Nil)
  // but     traverse(List("1", "hello", "54"))(parseStringToInt) == Left(...)
  def parseStringToInt(x: String): Either[Throwable, Int] =
    Try(x.toInt).toEither

  def traverse[E, A, B](fa: List[A])(f: A => Either[E, B]): Either[E, List[B]] = ???

  // 2k. Implement validateUsernameContent_v3 using traverse
  def validateUsernameContent_v3(username: String): Either[InvalidCharacter, Unit] = ???

  // 2l. Implement traverse_, a version of traverse that discards success values
  // such as traverse_(List("FooBar12", "FooBar34"))(validateUsername) == Right(())
  // but     traverse_(List("FooBar12", "123"))(validateUsername) == Left(...)
  def traverse_[E, A, B](fa: List[A])(f: A => Either[E, B]): Either[E, Unit] = ???

  // 2m. Implement validateUsernameContent_v4 using traverse_
  def validateUsernameContent_v4(username: String): Either[InvalidCharacter, Unit] = ???

  ////////////////////////
  // 3. Error message
  ////////////////////////

  // 3a. Implement validateUserMessage such as:
  // * if the inputs are valid, it display the User (e.g. user.toString)
  // * if the inputs are invalid, it display an error message
  def validateUserMessage(username: String, country: String): String = ???

  // 3b. What is the problem with validateUserMessage? How would you fix it?

}
