package toimpl.sideeffect

import exercises.sideeffect.{IO, IORef}
import exercises.sideeffect.SideEffectExercises.User

trait SideEffectToImpl { self =>

  def map[A, B](fa: IO[A])(f: A => B): IO[B]

  def flatMap[A, B](fa: IO[A])(f: A => IO[B]): IO[B]

  def readNameProgram: IO[String]

  def readInt: IO[Int]

  def void[A](fa: IO[A]): IO[Unit]

  def insertUser(ref: IORef[Map[String, User]], user: User): IO[Unit]

  def geAllUsers(ref: IORef[Map[String, User]]): IO[List[User]]

  def userDbProgram: IO[Map[String, User]]

  implicit class IOOps[A](fa: IO[A]){
    def map[B](f: A => B): IO[B] = self.map(fa)(f)
    def flatMap[B](f: A => IO[B]): IO[B] = self.flatMap(fa)(f)
    def void: IO[Unit] = self.void(fa)
  }
}
