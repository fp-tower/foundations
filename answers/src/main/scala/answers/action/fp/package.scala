package answers.action

import scala.concurrent.ExecutionContext

package object fp {

  implicit class ListExtension[A](values: List[A]) {
    def traverse[B](action: A => IO[B]): IO[List[B]] =
      IO.traverse(values)(action)

    def parTraverse[B](action: A => IO[B])(ec: ExecutionContext): IO[List[B]] =
      IO.parTraverse(values)(action)(ec)
  }

  implicit class ListIOExtension[A](values: List[IO[A]]) {
    def sequence: IO[List[A]] =
      IO.sequence(values)

    def parSequence(ec: ExecutionContext): IO[List[A]] =
      IO.parSequence(values)(ec)
  }

  implicit class Tuple2IOExtension[A, B](tuple: (IO[A], IO[B])) {
    def zip: IO[(A, B)] =
      tuple._1.zip(tuple._2)

    def parZip(ec: ExecutionContext): IO[(A, B)] =
      tuple._1.parZip(tuple._2)(ec)
  }

}
