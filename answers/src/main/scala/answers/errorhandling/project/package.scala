package answers.errorhandling
import answers.action.async.IO

package object project {

  implicit class IOOptionExtension[A](self: IO[Option[A]]) {
    def getOrFail(error: Throwable): IO[A] =
      self.flatMap {
        case Some(value) => IO(value)
        case None        => IO.fail(error)
      }
  }

  implicit class EitherExtension[A](self: Either[Throwable, A]) {
    def getOrFail: IO[A] =
      self match {
        case Left(value)  => IO.fail(value)
        case Right(value) => IO(value)
      }
  }

}
