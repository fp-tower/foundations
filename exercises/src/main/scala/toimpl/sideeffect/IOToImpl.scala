package toimpl.sideeffect

import exercises.sideeffect.IO
import exercises.sideeffect.IOExercises.{Console, User}

import scala.collection.mutable.ListBuffer

trait IOToImpl { self =>

  val consoleProgram: IO[String]

  def map[A, B](fa: IO[A])(f: A => B): IO[B]

  val readLength: IO[Int]

  val readInt: IO[Int]

  val userConsoleProgram: IO[User]

  def map2[A, B, C](fa: IO[A], fb: IO[B])(f: (A, B) => C): IO[C]

  def map4[A, B, C, D, E](fa: IO[A], fb: IO[B], fc: IO[C], fd: IO[D])(f: (A, B, C, D) => E): IO[E]

  val consoleProgram2: IO[String]

  def flatMap[A, B](fa: IO[A])(f: A => IO[B]): IO[B]

  val userConsoleProgram3: IO[User]

  def testConsole(in: List[String], out: ListBuffer[String]): Console

  def userConsoleProgram4(console: Console): IO[User]

  implicit class IOOps2[A, B](fab: (IO[A], IO[B])) {
    def map2[C](f: (A, B) => C): IO[C] = self.map2(fab._1, fab._2)(f)
  }

  implicit class IOOps4[A, B, C, D](fabcd: (IO[A], IO[B], IO[C], IO[D])) {
    def map4[E](f: (A, B, C, D) => E): IO[E] = self.map4(fabcd._1, fabcd._2, fabcd._3, fabcd._4)(f)
  }

}
