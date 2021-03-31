package exercises.action.fp

trait IO[A] {
  def unsafeRun(): A

  def andThen[Next](callBack: A => IO[Next]): IO[Next] =
    ???

  def onError[Other](callback: Throwable => IO[Other]): IO[A] =
    ???

  def retry(maxAttempt: Int): IO[A] =
    ???
}

object IO {
  def apply[A](block: => A): IO[A] =
    new IO[A] {
      def unsafeRun(): A = block
    }
}
