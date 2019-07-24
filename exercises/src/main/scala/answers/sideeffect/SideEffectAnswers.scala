package answers.sideeffect

import exercises.sideeffect.{Console, IO, IORef}
import toimpl.sideeffect.SideEffectToImpl
import exercises.sideeffect.SideEffectExercises.{stringToInt, User}

object SideEffectAnswers extends SideEffectToImpl {
  def map[A, B](fa: IO[A])(f: A => B): IO[B] =
    new IO(() => f(fa.unsafeRun()))

  def flatMap[A, B](fa: IO[A])(f: A => IO[B]): IO[B] =
    IO {
      val a = fa.unsafeRun()
      f(a).unsafeRun()
    }

  def readNameProgram: IO[String] =
    for {
      _    <- Console.writeLine("What's your name?")
      name <- Console.readLine
      _    <- Console.writeLine(s"Your name is $name")
    } yield name

  def readInt: IO[Int] =
    for {
      s <- Console.readLine
      i <- IO(stringToInt(s).getOrElse(sys.error(s"Invalid number $s")))
    } yield i

  def void[A](fa: IO[A]): IO[Unit] =
    fa.map(_ => ())

  def insertUser(ref: IORef[Map[String, User]], user: User): IO[Unit] =
    ref.modify(_.updated(user.name, user)).void

  def geAllUsers(ref: IORef[Map[String, User]]): IO[List[User]] =
    ref.get.map(_.values.toList)

  def userDbProgram: IO[Map[String, User]] = ???
}
