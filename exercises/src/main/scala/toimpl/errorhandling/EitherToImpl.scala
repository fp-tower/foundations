package toimpl.errorhandling

import exercises.errorhandling.EitherExercises.UsernameError.{InvalidCharacter, TooSmall}
import exercises.errorhandling.EitherExercises.{CountryError, GetOrderError, Order, UsernameError}
import exercises.errorhandling.{Country, Username}

trait EitherToImpl {

  ////////////////////////
  // 1. Use cases
  ////////////////////////

  def getOrder(id: Int, users: List[Order]): Either[GetOrderError, Order]

  def validateUsernameSize(username: String): Either[TooSmall.type, Unit]

  def validateUsernameCharacter(c: Char): Either[InvalidCharacter, Unit]

  def validateUsernameContent(username: String): Either[InvalidCharacter, Unit]

  def validateUsername(username: String): Either[UsernameError, Username]

  def validateCountry(country: String): Either[CountryError, Country]

  ////////////////////////
  // 2. Composing Either
  ////////////////////////

  def leftMap[E, A, B](fa: Either[E, A])(f: E => B): Either[B, A]

  def tuple2[E, A, B](fa: Either[E, A], fb: Either[E, B]): Either[E, (A, B)]

  def map2[E, A1, A2, B](fa: Either[E, A1], fb: Either[E, A2])(f: (A1, A2) => B): Either[E, B]

  def validateUsername_v2(username: String): Either[UsernameError, Username]

  def validateUsername_v3(username: String): Either[UsernameError, Username]

  def sequence[E, A](fa: List[Either[E, A]]): Either[E, List[A]]

  def validateUsernameContent_v2(username: String): Either[InvalidCharacter, Unit]

  def traverse[E, A, B](fa: List[A])(f: A => Either[E, B]): Either[E, List[B]]

  def validateUsernameContent_v3(username: String): Either[InvalidCharacter, Unit]

  def traverse_[E, A, B](fa: List[A])(f: A => Either[E, B]): Either[E, Unit]

  def validateUsernameContent_v4(username: String): Either[InvalidCharacter, Unit]

  ////////////////////////
  // 3. Error message
  ////////////////////////

  def validateUserMessage(username: String, country: String): String

}
