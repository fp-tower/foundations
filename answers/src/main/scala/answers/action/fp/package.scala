package answers.action

package object fp {

  implicit class ListExtension[A](values: List[A]) {
    def traverse[B](action: A => IO[B]): IO[List[B]] =
      IO.traverse(values)(action)
  }

  implicit class ListIOExtension[A](values: List[IO[A]]) {
    def sequence: IO[List[A]] =
      IO.sequence(values)
  }

}
