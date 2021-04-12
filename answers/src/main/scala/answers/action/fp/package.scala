package answers.action

package object fp {

  implicit class ListIOExtension[A](values: List[A]) {
    def traverse[B](action: A => IO[B]): IO[List[B]] =
      IO.traverse(values)(action)
  }

}
