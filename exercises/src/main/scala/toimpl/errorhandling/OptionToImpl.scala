package toimpl.errorhandling

import exercises.errorhandling.OptionExercises.Order
import exercises.errorhandling.{Country, User, Username}

trait OptionToImpl {

  ////////////////////////
  // 1. Use cases
  ////////////////////////

  def getOrder(id: Int, orders: List[Order]): Option[Order]

  def charToDigit(c: Char): Option[Int]

  def isValidUsername(username: String): Boolean

  def validateUsername(username: String): Option[Username]

  def validateCountry(country: String): Option[Country]

  def validateUser(username: String, country: String): Option[User]

  ////////////////////////
  // 2. Composing Option
  ////////////////////////

  def tuple2[A, B](fa: Option[A], fb: Option[B]): Option[(A, B)]

  def map2[A, B, C](fa: Option[A], fb: Option[B])(f: (A, B) => C): Option[C]

  def map2FromTuple2[A, B, C](fa: Option[A], fb: Option[B])(f: (A, B) => C): Option[C]
  def tuple2FromMap2[A, B](fa: Option[A], fb: Option[B]): Option[(A, B)]

  def validateUser_v2(username: String, country: String): Option[User]

  def validateUser_v3(username: String, country: String): Option[User]

  def validateUsernames(usernames: List[String]): Option[List[Username]]

  def sequence[A](fa: List[Option[A]]): Option[List[A]]

  def validateUsernames_v2(usernames: List[String]): Option[List[Username]]

  def traverse[A, B](fa: List[A])(f: A => Option[B]): Option[List[B]]

  def validateUsernames_v3(usernames: List[String]): Option[List[Username]]

  def traverseFromSequence[A, B](fa: List[A])(f: A => Option[B]): Option[List[B]]
  def sequenceFromTraverse[A](fa: List[Option[A]]): Option[List[A]]

  ////////////////////////
  // 3. Error message
  ////////////////////////

  def validateUserMessage(username: String, country: String): String

}
