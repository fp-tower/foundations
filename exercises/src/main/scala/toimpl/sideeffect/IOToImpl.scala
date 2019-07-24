package toimpl.sideeffect

import exercises.sideeffect.{IO, IOExercises}
import exercises.sideeffect.IOExercises.User

trait IOToImpl { self =>

  val consoleProgram: IO[String]

  def map[A, B](fa: IO[A])(f: A => B): IO[B]

  val readLength: IO[Int]

  val readInt: IO[Int]

  val userConsoleProgram: IO[User]

  def map2[A, B, C](fa: IO[A], fb: IO[B])(f: (A, B) => C): IO[C]

  val consoleProgram2: IO[String]

  def flatMap[A, B](fa: IO[A])(f: A => IO[B]): IO[B]

  val userConsoleProgram3: IO[User]

  // Implicit classes are use to add methods on IO
  implicit class IOOps[A](fa: IO[A]) {
    def map[B](f: A => B): IO[B]         = self.map(fa)(f)
    def flatMap[B](f: A => IO[B]): IO[B] = self.flatMap(fa)(f)
  }

  implicit class IOOps2[A, B](fab: (IO[A], IO[B])) {
    def map2[C](f: (A, B) => C): IO[C] = self.map2(fab._1, fab._2)(f)
  }

  implicit class IOOps4[A, B, C, D](fabcd: (IO[A], IO[B], IO[C], IO[D])) {
    def map4[E](f: (A, B, C, D) => E): IO[E] = IOExercises.map4(fabcd._1, fabcd._2, fabcd._3, fabcd._4)(f)
  }

}
