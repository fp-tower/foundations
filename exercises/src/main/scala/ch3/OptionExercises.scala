package ch3

import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._

import scala.util.Try

object OptionExercises {


  // 1a. Implement getUser such as it returns the first user matching the id
  case class User(id: Int, name: String)
  def getUser(id: Int, users: List[User]): Option[User] = ???



  // 2a. Implement charToDigit such as it returns 0 for '0', 1 for '1', ..., 9 for '9'
  def charToDigit(c: Char): Option[Int] = ???


  // 2b. Implement charToDigit_v2
  def charToDigit_v2(c: Char): Option[Int Refined Interval.Closed[W.`0`.T, W.`9`.T]] = ???


  // 2c. Implement charToDigit in terms of charToDigit_v2


  // 3. Form is a Sum type, currently it is either a Rectangle or a Circle
  sealed trait Form
  object Form {
    case class Rectangle(width: Int, height: Int) extends Form
    case class Circle(radius: Int) extends Form
  }

  import Form._

  // 3a. Implement asRectangle using pattern matching
  def asRectangle(form: Form): Option[Rectangle] = ???

  // 3b. Implement asRectangle using pattern matching
  def asCircle(form: Form): Option[Circle] = ???


  // 4. The goal of this exercise is to implement parseForm such as
  // parseForm("Rectangle,10,2") == Some(Rectangle(10, 2))
  // parseForm("Circle,5")       == Some(Circle(5))
  def parseForm(s: String): Option[Form] = ???


  // 4a. Assume parseForm is implemented
  // implement parseRectangle and parseCircle using pattern matching
  def parseRectangle(s: String): Option[Rectangle] = ???

  def parseCircle(s: String): Option[Circle] = ???


  // 4b. Assume flatMap is implemented
  // re-implement parseRectangle and parseCircle using flatMap
  def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] = ???


  // 4c. Implement flatMap


  // bonus question: how many possible implementation of flatMap exist?
  // what does it mean it terms of unit testing?


  // 4d. Implement _parseCircle using parseInt and pattern matching
  def parseInt(s: String): Option[Int] =
    Try(s.toInt).toOption

  def _parseCircle(radius: String): Option[Circle] = ???

  // 4e. Assume map is implemented
  // re-implement _parseCircle using map
  def map[A, B](fa: Option[A])(f: A => B): Option[B] = ???


  // 4f. Implement map using pattern matching


  // 4g. Implement map in terms of flatMap


  // 4h. Implement _parseRectangle using parseInt and pattern matching
  def _parseRectangle(width: String, height: String): Option[Rectangle] = ???


  // 4h. Assume map2 is implemented
  // re-implement _parseRectangle using map2
  def map2[A, B, C](fa: Option[A], fb: Option[B])(f: (A, B) => C): Option[C] = ???



  // 4i. Implement map2 using pattern matching



  // 4j. Re-implement map2 using flatMap



  // 4a. Implement using pattern matching
  def asRectangles(f1: Form, f2: Form): Option[(Rectangle, Rectangle)] = ???


  // 4b. Implement map2
//  def map2[A, B, C](fa: Option[A], fb: Option[B])(f: (A, B) => C): Option[C] = ???


  // 4c. Implement asRectangles using map2


  // 4d. Implement tuple2 using map2
  def tuple2[A, B](fa: Option[A], fb: Option[B]): Option[(A, B)] = ???


  // 4e. Implement asRectangles using tuple2


  // 4f. Implement map and flatMap
//  def map    [A, B](fa: Option[A])(f: A =>        B ): Option[B] = ???
//  def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] = ???


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
