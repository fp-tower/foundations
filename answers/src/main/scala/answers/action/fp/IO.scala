package answers.action.fp

import java.time.Duration
import scala.util.{Failure, Success, Try}

sealed trait IO[A] {

  def unsafeRun(): A

  def andThen[Other](other: IO[Other]): IO[Other] =
    for {
      _   <- this
      res <- other
    } yield res

  def onError[Other](callback: Throwable => IO[Other]): IO[A] =
    handleErrorWith(e => callback(e).attempt *> IO.fail(e))

  def flatMap[Other](callBack: A => IO[Other]): IO[Other] =
    IO {
      val result: A             = this.unsafeRun()
      val nextAction: IO[Other] = callBack(result)

      nextAction.unsafeRun()
    }

  def map[Other](callBack: A => Other): IO[Other] =
    flatMap(a => IO(callBack(a)))

  def retry(maxAttempt: Int): IO[A] =
    IO {
      var remaining      = maxAttempt
      var result: Try[A] = Failure(new IllegalArgumentException("maxAttempt must be > 0"))

      while (remaining > 0 && result.isFailure) {
        remaining -= 1
        result = attempt.unsafeRun()
      }

      result.get
    }

  def retryRecursive(maxAttempt: Int): IO[A] =
    if (maxAttempt <= 0)
      IO.fail(new IllegalArgumentException("maxAttempt must be > 0"))
    else if (maxAttempt == 1)
      this
    else
      handleErrorWith(_ => retryRecursive(maxAttempt - 1))

  def *>[Next](next: IO[Next]): IO[Next] =
    flatMap(_ => next)

  def attempt: IO[Try[A]] =
    IO {
      Try(this.unsafeRun())
    }

  def handleErrorWith(callback: Throwable => IO[A]): IO[A] =
    attempt.flatMap {
      case Success(value)     => IO(value)
      case Failure(exception) => callback(exception)
    }
}

object IO {
  def apply[A](block: => A): IO[A] =
    new IO[A] {
      def unsafeRun(): A = block
    }

  def fail[A](error: Throwable): IO[A] =
    IO(throw error)

  def sleep(duration: Duration): IO[Unit] =
    IO {
      Thread.sleep(duration.toMillis)
    }

  def sequence[A](actions: List[IO[A]]): IO[List[A]] =
    IO {
      actions.map(_.unsafeRun())
    }

  def traverse[A, B](values: List[A])(action: A => IO[B]): IO[List[B]] =
    sequence(values.map(action))

}
