package answers.sideeffect

import exercises.sideeffect.{Console, IO, IORef}
import toimpl.sideeffect.SideEffectToImpl
import exercises.sideeffect.SideEffectExercises.{stringToInt, User}

object SideEffectAnswers extends SideEffectToImpl {
  def map[A, B](fa: IO[A])(f: A => B): IO[B] =
    IO(() =>
      fa.unsafeRun().map(f)
    )

  def flatMap[A, B](fa: IO[A])(f: A => IO[B]): IO[B] =
    IO(() =>
      fa.unsafeRun().flatMap(f(_).unsafeRun())
    )

  def readNameProgram: IO[String] =
    for {
      _    <- Console.writeLine("What's your name?")
      name <- Console.readLine
      _    <- Console.writeLine(s"Your name is $name")
    } yield name

  def readInt: IO[Int] =
    for {
      s <- Console.readLine
      i <- IO.fromTry(stringToInt(s))
    } yield i

  def void[A](fa: IO[A]): IO[Unit] =
    fa.map(_ => ())


  def insertUser(ref: IORef[Map[String, User]], user: User): IO[Unit] =
    ref.modify(_.updated(user.name, user))

  def geAllUsers(ref: IORef[Map[String, User]]): IO[List[User]] =
    ref.get.map(_.values.toList)

  def userDbProgram: IO[Map[String, User]] = ???
}
