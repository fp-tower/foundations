package ch3

import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._

object OptionExercises {


  // 1a. Implement getUser such as it returns the first user matching the id
  case class User(id: Int, name: String)
  def getUser(id: Int, users: List[User]): Option[User] = ???



  // 2a. Implement charToDigit such as it returns 0 for '0', 1 for '1', ..., 9 for '9'
  def charToDigit(c: Char): Option[Int] = ???


  // 2b. Implement charToDigit_v2
  def charToDigit_v2(c: Char): Option[Int Refined Interval.Closed[W.`0`.T, W.`9`.T]] = ???


  // 2c. Implement charToDigit in terms of charToDigit_v2


  // 3a. Implement asRectangle and asCircle
  sealed trait Form
  object Form {
    case class Rectangle(width: Int, height: Int) extends Form
    case class Circe(radius: Int) extends Form
  }

  import Form._

  def asRectangle(form: Form): Option[Rectangle] = ???


  def asCircle(form: Form): Option[Circe] = ???


  // 4a. Implement using pattern matching
  def asRectangles(f1: Form, f2: Form): Option[(Rectangle, Rectangle)] = ???


  // 4b. Implement map2
  def map2[A, B, C](fa: Option[A], fb: Option[B])(f: (A, B) => C): Option[C] = ???


  // 4c. Implement asRectangles using map2


  // 4d. Implement tuple2 using map2
  def tuple2[A, B](fa: Option[A], fb: Option[B]): Option[(A, B)] = ???


  // 4e. Implement asRectangles using tuple2


  // 4f. Implement map and flatMap
  def map    [A, B](fa: Option[A])(f: A =>        B ): Option[B] = ???
  def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] = ???


  // 4g. Implement asRectangles using map and flatMap


  // 4h. Implement map using flatMap


  // 4i. Implement map2 using flatMap


  // 4j. Implement map using map2


  // 5a. Implement getUser_v2 such as it returns the first user matching the id
  // or an error signaling if there is no user matching or several users matching
  sealed trait GetUserError
  def getUser_v2(id: Int, users: List[User]): Either[GetUserError, User] = ???





  // a. Create PasswordError such as it handle the following cases, a password must have:
  // * has at least 8 characters long
  // * contains at least one upper/lower case letter
  // * contains at least a digit
  sealed trait PasswordError
  def validatePassword(s: String): Either[PasswordError, Unit] = ???


  // b. Implement one helper method for each type of error


  // c. Use helper methods to implement


  // b. how would change validatePassword such as it describes the kind of error




  // c. change validatePassword such as it returns a NonEmptyList of error




}
