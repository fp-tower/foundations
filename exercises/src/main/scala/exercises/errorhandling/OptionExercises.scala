package exercises.errorhandling

import exercises.errorhandling.Country._
import toimpl.errorhandling.OptionToImpl

object OptionExercises extends OptionToImpl {

  ////////////////////////
  // 1. Use cases
  ////////////////////////

  // 1a. Implement getUser such as it returns the first user matching the id
  // such as getUser(123, List(User(222, "paul"), User(123, "john"))) == Some(User(123, "john"))
  // but getUser(111, List(User(222, "paul"), User(123, "john"))) == None
  case class Order(id: Int, name: String)
  def getOrder(id: Int, users: List[Order]): Option[Order] = ???

  // 1b. Implement charToDigit such as it returns 0 for '0', 1 for '1', ..., 9 for '9'
  def charToDigit(c: Char): Option[Int] = ???

  // 1c. Implement isValidateUsername such as a userName:
  // * is at least 3 characters long
  // * contains only letter, digits or the following special characters: "_-"
  def isValidateUsername(userName: String): Boolean = ???

  // 1d. Implement validateUsername such as it trims the input username and then validate it
  // such as validateUsername("foo") == Some(UserName("foo"))
  //         validateUsername("  foo ") == Some(UserName("foo"))
  // but     validateUsername("abc!@£") == None
  //         validateUsername(" yo")    == None
  def validateUsername(userName: String): Option[UserName] = ???

  // 1e. Implement validateCountry such as it parses a 3 letter country code into a Country enumeration
  // see https://www.iban.com/country-codes
  // e.g. validateCountry("FRA") == Some(France)
  //      validateCountry("foo") == None
  //      validateCountry("FRANCE") == None
  //      validateCountry("DZA") a valid alpha 3 but not supported
  def validateCountry(country: String): Option[Country] = ???

  // 1f. Implement validateUser that validates both username and country
  // Use pattern matching for this implementation
  def validateUser(username: String, country: String): Option[User] = ???

  ////////////////////////
  // 2. Composing Option
  ////////////////////////

  // 2a. Implement tuple2 using pattern matching such as
  // tuple2(Some(1), Some("hello")) == Some((1, "hello"))
  // tuple2(Some(1), None) == None
  // bonus: how many implementations of tuple2 would compile?
  def tuple2[A, B](fa: Option[A], fb: Option[B]): Option[(A, B)] = ???

  // 2b. Implement map2 using pattern matching such as
  // map2(Some(1), Some(2))(_ + _) == Some(3)
  // map2(Some(1), Option.empty[Int])(_ + _) == None
  def map2[A, B, C](fa: Option[A], fb: Option[B])(f: (A, B) => C): Option[C] = ???

  // 2c. Re-implement map2 using tuple2 and tuple2 using map2
  // which would you prefer? Why?
  def map2FromTuple2[A, B, C](fa: Option[A], fb: Option[B])(f: (A, B) => C): Option[C] = ???
  def tuple2FromMap2[A, B](fa: Option[A], fb: Option[B]): Option[(A, B)]               = ???

  // 2d. Re-implement validateUser using tuple2 or map2
  def validateUser_v2(username: String, country: String): Option[User] = ???

  // 2e. Re-implement validateUser using flatMap
  // which way do you prefer? Why?
  def validateUser_v3(username: String, country: String): Option[User] = ???

  // 2f. Implement validateUsernames such as it returns a list of Username if all inputs are valid
  // e.g. validateUsernames(List("  foo", "Foo123", "Foo1-2_3")) == Some(List(UserName("foo"), UserName("Foo123"), UserName("Foo1-2_3")))
  // e.g. validateUsernames(List("  foo", "x", "Foo1-2_3")) == None
  // Use recursion or fold
  def validateUsernames(userNames: List[String]): Option[List[UserName]] = ???

  // 2g. Implement sequence using recursion or fold
  // such as sequence(List(Some(1), Some(5), Some(8))) == Some(List(1, 5, 8))
  //         sequence(Nil) == Some(Nil)
  // but     sequence(List(Some(1), None, Some(8))) == None
  def sequence[A](fa: List[Option[A]]): Option[List[A]] = ???

  // 2h. Re-implement validateUsernames using sequence
  def validateUsernames_v2(userNames: List[String]): Option[List[UserName]] = ???

  // 2i. Implement traverse using recursion or fold
  // such as traverse(List(1, 5, 9))(x => if(isEven(x)) Some(x) else None) == Some(List(1, 5, 9))
  //         traverse(List.empty[Int])(x => if(isEven(x)) Some(x) else None) == Some(Nil)
  // but     traverse(List(1, 4, 9))(x => if(isEven(x)) Some(x) else None) == None
  def traverse[A, B](fa: List[A])(f: A => Option[B]): Option[List[B]] = ???

  // 2j. Re-implement validateUsernames using traverse
  def validateUsernames_v3(userNames: List[String]): Option[List[UserName]] = ???

  // 2k. Re-implement traverse using sequence and sequence using traverse
  // which would you prefer? Why?
  def traverseFromSequence[A, B](fa: List[A])(f: A => Option[B]): Option[List[B]] = ???
  def sequenceFromTraverse[A](fa: List[Option[A]]): Option[List[A]]               = ???

  ////////////////////////
  // 3. Error message
  ////////////////////////

  // 3a. Implement validateUserMessage such as:
  // * if the inputs are valid, it display the User (e.g. user.toString)
  // * if the inputs are invalid, it display an error message
  def validateUserMessage(username: String, country: String): String = ???

  // 3b. What is the problem with validateUserMessage? How would you fix it?

}
