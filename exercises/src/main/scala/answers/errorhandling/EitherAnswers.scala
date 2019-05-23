package answers.errorhandling

import exercises.errorhandling.EitherExercises.PasswordError.{NoDigit, NoLowerCase, NoUpperCase, TooSmall}
import exercises.errorhandling.EitherExercises.{GetUserError, PasswordError}
import exercises.errorhandling.OptionExercises.User
import toimpl.errorhandling.EitherToImpl

object EitherAnswers extends EitherToImpl {

  def getUser(id: Int, users: List[User]): Either[GetUserError, User] =
    users.filter(_.id == id) match {
      case Nil      => Left(GetUserError.UserNotFound)
      case x :: Nil => Right(x)
      case _ :: _   => Left(GetUserError.NonUniqueUserId)
    }

  def checkSize(s: String): Either[TooSmall.type, Unit] =
    if (s.size < 8) Left(TooSmall) else Right(())

  def contains[E](s: String)(p: Char => Boolean, error: => E): Either[E, Unit] =
    s.find(p).toRight(error).map(_ => ())

  def containsUpperCase(s: String): Either[NoUpperCase.type, Unit] =
    contains(s)(_.isUpper, NoUpperCase)

  def containsLowerCase(s: String): Either[NoLowerCase.type, Unit] =
    contains(s)(_.isLower, NoLowerCase)

  def containsDigit(s: String): Either[NoDigit.type, Unit] =
    contains(s)(_.isDigit, NoDigit)

  def validatePassword(s: String): Either[PasswordError, Unit] =
    for {
      _ <- checkSize(s)
      _ <- containsUpperCase(s)
      _ <- containsLowerCase(s)
      _ <- containsDigit(s)
    } yield ()

  def leftMap[E, A, B](fa: Either[E, A])(f: E => B): Either[B, A] =
    fa match {
      case Left(e)  => Left(f(e))
      case Right(a) => Right(a)
    }

  def tuple2[E, A, B](fa: Either[E, A], fb: Either[E, B]): Either[E, (A, B)] =
    (fa, fb) match {
      case (Right(a), Right(b)) => Right((a, b))
      case (Left(e), Right(_))  => Left(e)
      case (Right(_), Left(e))  => Left(e)
      case (Left(e), Left(_))   => Left(e)
    }

  def tuple3[E, A, B, C](fa: Either[E, A], fb: Either[E, B], fc: Either[E, C]): Either[E, (A, B, C)] =
    tuple2(tuple2(fa, fb), fc).map { case ((a, b), c) => (a, b, c) }

  def tuple4[E, A, B, C, D](fa: Either[E, A],
                            fb: Either[E, B],
                            fc: Either[E, C],
                            fd: Either[E, D]): Either[E, (A, B, C, D)] =
    tuple2(tuple2(fa, fb), tuple2(fc, fd)).map { case ((a, b), (c, d)) => (a, b, c, d) }

  def validatePassword_v2(s: String): Either[PasswordError, Unit] =
    tuple4(checkSize(s), containsUpperCase(s), containsLowerCase(s), containsDigit(s)).map(_ => ())

  def map2[E, A1, A2, B](fa: Either[E, A1], fb: Either[E, A2])(f: (A1, A2) => B): Either[E, B] =
    (fa, fb) match {
      case (Right(a), Right(b)) => Right(f(a, b))
      case (Left(e), Right(_))  => Left(e)
      case (Right(_), Left(e))  => Left(e)
      case (Left(e), Left(_))   => Left(e)
    }

  def tuple2_v2[E, A, B](fa: Either[E, A], fb: Either[E, B]): Either[E, (A, B)] =
    map2(fa, fb)((_, _))

  def sequence[E, A](fa: List[Either[E, A]]): Either[E, List[A]] =
    fa.foldRight[Either[E, List[A]]](Right(Nil))((a, acc) => map2(a, acc)(_ :: _))

  def validatePassword_v3(s: String): Either[PasswordError, Unit] =
    sequence(
      List(
        checkSize(s),
        containsUpperCase(s),
        containsLowerCase(s),
        containsDigit(s)
      )
    ).map(_ => ())

  def traverse[E, A, B](fa: List[A])(f: A => Either[E, B]): Either[E, List[B]] =
    fa.foldRight[Either[E, List[B]]](Right(Nil))((a, acc) => map2(f(a), acc)(_ :: _))

  def validatePassword_v4(s: String): Either[PasswordError, Unit] =
    traverse(List(checkSize _, containsUpperCase _, containsLowerCase _, containsDigit _))(_(s)).map(_ => ())

  def traverse_[E, A, B](fa: List[A])(f: A => Either[E, B]): Either[E, Unit] =
    fa.foldRight[Either[E, Unit]](Right(()))((a, acc) => map2(f(a), acc)((_, _) => ()))

  def validatePassword_v5(s: String): Either[PasswordError, Unit] =
    traverse_(List(checkSize _, containsUpperCase _, containsLowerCase _, containsDigit _))(_(s))
}
