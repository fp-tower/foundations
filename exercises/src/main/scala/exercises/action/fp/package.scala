package exercises.action

import scala.concurrent.ExecutionContext

package object fp {

  // extend the List API
  implicit class ListExtension[A](values: List[A]) {
    def traverse[B](action: A => IO[B]): IO[List[B]] =
      IO.traverse(values)(action)

    def parTraverse[B](action: A => IO[B])(ec: ExecutionContext): IO[List[B]] =
      IO.parTraverse(values)(action)(ec)
  }

  // extend the List API when the List contains IO
  implicit class ListIOExtension[A](values: List[IO[A]]) {
    def sequence: IO[List[A]] =
      IO.sequence(values)

    def parSequence(ec: ExecutionContext): IO[List[A]] =
      IO.parSequence(values)(ec)
  }

  // extend the Tuple2 API when both elements are IO
  implicit class Tuple2IOExtension[A, B](tuple: (IO[A], IO[B])) {
    def zip: IO[(A, B)] =
      tuple._1.zip(tuple._2)

    def parZip(ec: ExecutionContext): IO[(A, B)] =
      tuple._1.parZip(tuple._2)(ec)
  }

}
