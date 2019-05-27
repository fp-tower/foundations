package exercises.errorhandling

import exercises.errorhandling.OptionExercises.User
import toimpl.errorhandling.EitherToImpl

import scala.util.Try

object EitherExercises extends EitherToImpl {

  ////////////////////////
  // 1. Error ADT
  ////////////////////////

  // 1a. Implement getUser
  // such as getUser(123, List(User(222, "paul"), User(123, "john"))) == Right(User(123, "john"))
  // but getUser(111, List(User(222, "paul"), User(123, "john"))) == Left(UserNotFound)
  //     getUser(123, List(User(123, "paul"), User(123, "john"))) == Left(NonUniqueUserId)
  sealed trait GetUserError
  object GetUserError {
    case object UserNotFound    extends GetUserError
    case object NonUniqueUserId extends GetUserError
  }

  def getUser(id: Int, users: List[User]): Either[GetUserError, User] = ???

  // 1b. Implement validatePassword such as a password must:
  // * be at least 8 characters long
  // * contains at least one upper/lower case letter
  // * contains at least a digit
  sealed trait PasswordError
  object PasswordError {
    case object TooSmall    extends PasswordError
    case object NoUpperCase extends PasswordError
    case object NoLowerCase extends PasswordError
    case object NoDigit     extends PasswordError
  }
  def validatePassword(s: String): Either[PasswordError, Unit] = ???

  ////////////////////////
  // 2. Composing errors
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

  // 2c. Implement tuple3, tuple4 using tuple2
  def tuple3[E, A, B, C](fa: Either[E, A], fb: Either[E, B], fc: Either[E, C]): Either[E, (A, B, C)] = ???
  def tuple4[E, A, B, C, D](fa: Either[E, A],
                            fb: Either[E, B],
                            fc: Either[E, C],
                            fd: Either[E, D]): Either[E, (A, B, C, D)] = ???

  // 2d. Implement validatePassword_v2 using tuple4
  def validatePassword_v2(s: String): Either[PasswordError, Unit] = ???

  // 2e. Implement map2
  // such as map2(Right(1), Right("foo"))(_.toString + _) == Right("1foo")
  //         map2(Left("error1"), Right("error2"))(_.toString + _) == Left("error1")
  def map2[E, A1, A2, B](fa: Either[E, A1], fb: Either[E, A2])(f: (A1, A2) => B): Either[E, B] = ???

  // 2f. TODO map2 example

  // 2g. Implement tuple2_v2 using map2
  def tuple2_v2[E, A, B](fa: Either[E, A], fb: Either[E, B]): Either[E, (A, B)] = ???

  // 2h. Implement sequence
  // such as sequence(List(Right(1), Right(5), Right(12))) == Right(List(1,5,12))
  // but     sequence(List(Right(1), Left("error"), Right(12))) == Left("error")
  def sequence[E, A](fa: List[Either[E, A]]): Either[E, List[A]] = ???

  // 2i. Implement validatePassword_v3 using sequence
  def validatePassword_v3(s: String): Either[PasswordError, Unit] = ???

  // 2j. Implement traverse
  // such as traverse(List("1", "23", "54"))(parseStringToInt) == Right(List(1,23,54))
  //         traverse(List.empty[String])(parseStringToInt) == Right(Nil)
  // but     traverse(List("1", "hello", "54"))(parseStringToInt) == Left(...)
  def parseStringToInt(x: String): Either[Throwable, Int] =
    Try(x.toInt).toEither

  def traverse[E, A, B](fa: List[A])(f: A => Either[E, B]): Either[E, List[B]] = ???

  // 2k. Implement validatePassword_v4 using traverse
  def validatePassword_v4(s: String): Either[PasswordError, Unit] = ???

  // 2l. Implement traverse_
  // such as traverse_(List("FooBar12", "FooBar34"))(validatePassword) == Right(())
  // but     traverse_(List("FooBar12", "123"))(validatePassword) == Left(...)
  def traverse_[E, A, B](fa: List[A])(f: A => Either[E, B]): Either[E, Unit] = ???

  // 2m. Implement validatePassword_v5 using traverse_
  def validatePassword_v5(s: String): Either[PasswordError, Unit] = ???

}
