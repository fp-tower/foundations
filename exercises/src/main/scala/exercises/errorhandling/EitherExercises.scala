package exercises.errorhandling

import cats.data.NonEmptyList
import exercises.errorhandling.OptionExercises.User

import scala.io.Source

object EitherExercises {


  // 5a. Implement getUser_v2 such as it returns the first user matching the id
  // or an error signaling if there is no user matching or several users matching
  sealed trait GetUserError
  def getUser_v2(id: Int, users: List[User]): Either[GetUserError, User] = ???



  Source.fromURL("http://google.com").take(100).mkString

  // 6a. Create PasswordError such as it handle the following cases, a password must have:
  // * has at least 8 characters long
  // * contains at least one upper/lower case letter
  // * contains at least a digit
  sealed trait PasswordError
  def validatePassword(s: String): Either[PasswordError, Unit] = ???


  // 6b. Implement one helper method for each type of error



  // 6c. Implement flatMap
  def flatMap[E, A, B](fa: Either[E, A])(f: A => Either[E, B]): Either[E, B] = ???



  // 6d. Implement validatePassword using flatMap and helper methods created above




  // 6e. Assume map4 is implemented, implement validatePassword using map4
  // you may need to review the definition of your helper methods
  def map4[E, A1, A2, A3, A4, B](
    fa: Either[E, A1],
    fb: Either[E, A2],
    fc: Either[E, A3],
    fd: Either[E, A4])(
    f: (A1, A2, A3, A4) => B
  ): Either[E, B] = ???



  // 6f. Implement map2 using pattern matching
  def map2[E, A1, A2, B](fa: Either[E, A1], fb: Either[E, A2])(f: (A1, A2) => B): Either[E, B] = ???



  // 6g. Implement tuple2 using pattern matching
  def tuple2[E, A1, A2, B](fa: Either[E, A1], fb: Either[E, A2]): Either[E, (A1, A2)] = ???


  // 6h. Implement map3 using map2 and eventually tuple2
  def map3[E, A1, A2, A3, B](fa: Either[E, A1], fb: Either[E, A2], fc: Either[E, A3])(f: (A1, A2, A3) => B): Either[E, B] = ???



  // 6i. Implement map4



}
