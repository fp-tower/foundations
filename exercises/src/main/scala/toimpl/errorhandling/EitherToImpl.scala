package toimpl.errorhandling

import exercises.errorhandling.EitherExercises.{GetUserError, PasswordError}
import exercises.errorhandling.OptionExercises.User

trait EitherToImpl {

  ////////////////////////
  // 1. Error ADT
  ////////////////////////

  def getUser(id: Int, users: List[User]): Either[GetUserError, User]

  def validatePassword(s: String): Either[PasswordError, Unit]

  ////////////////////////
  // 2. Composing errors
  ////////////////////////

  def leftMap[E, A, B](fa: Either[E, A])(f: E => B): Either[B, A]

  def tuple2[E, A, B](fa: Either[E, A], fb: Either[E, B]): Either[E, (A, B)]

  def tuple3[E, A, B, C](fa: Either[E, A], fb: Either[E, B], fc: Either[E, C]): Either[E, (A, B, C)]

  def tuple4[E, A, B, C, D](fa: Either[E, A],
                            fb: Either[E, B],
                            fc: Either[E, C],
                            fd: Either[E, D]): Either[E, (A, B, C, D)]

  def validatePassword_v2(s: String): Either[PasswordError, Unit]

  def map2[E, A1, A2, B](fa: Either[E, A1], fb: Either[E, A2])(f: (A1, A2) => B): Either[E, B]

  def tuple2_v2[E, A, B](fa: Either[E, A], fb: Either[E, B]): Either[E, (A, B)]

  def sequence[E, A](fa: List[Either[E, A]]): Either[E, List[A]]

  def validatePassword_v3(s: String): Either[PasswordError, Unit]

  def traverse[E, A, B](fa: List[A])(f: A => Either[E, B]): Either[E, List[B]]

  def validatePassword_v4(s: String): Either[PasswordError, Unit]

  def traverse_[E, A, B](fa: List[A])(f: A => Either[E, B]): Either[E, Unit]

  def validatePassword_v5(s: String): Either[PasswordError, Unit]

}
