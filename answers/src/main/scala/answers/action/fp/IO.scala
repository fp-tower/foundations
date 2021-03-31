package answers.action.fp

import scala.util.{Failure, Success, Try}

sealed trait IO[A] {

  def unsafeRun(): A

  def retry(maxAttempt: Int): IO[A] =
    if (maxAttempt <= 0) IO.fail(new IllegalArgumentException("maxAttempt must be > 0"))
    else if (maxAttempt == 1) this
    else
      attempt.flatMap {
        case Failure(_)     => retry(maxAttempt - 1)
        case Success(value) => IO(value)
      }

  def onError[Other](callback: Throwable => IO[Other]): IO[A] =
    attempt.flatMap {
      case Failure(e)     => callback(e).attempt *> IO.fail(e)
      case Success(value) => IO(value)
    }

  def andThen[Next](callBack: A => IO[Next]): IO[Next] =
    flatMap(callBack)

  def flatMap[Next](callBack: A => IO[Next]): IO[Next] =
    IO {
      val result: A            = unsafeRun()
      val nextAction: IO[Next] = callBack(result)

      nextAction.unsafeRun()
    }

  def *>[Next](next: IO[Next]): IO[Next] =
    this.flatMap(_ => next)

  def map[Next](callBack: A => Next): IO[Next] =
    IO {
      callBack(unsafeRun())
    }

  def attempt: IO[Try[A]] =
    IO {
      Try(unsafeRun())
    }
}

object IO {
  def apply[A](block: => A): IO[A] =
    new IO[A] {
      def unsafeRun(): A = block
    }

  def fail[A](error: Throwable): IO[A] =
    IO(throw error)

}
