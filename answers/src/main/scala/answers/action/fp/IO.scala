package answers.action.fp

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

sealed trait IO[A] {

  def unsafeRun(): A

  def andThen[Other](other: IO[Other]): IO[Other] =
    for {
      _   <- this
      res <- other
    } yield res

  def onError[Other](cleanup: Throwable => IO[Other]): IO[A] =
    handleErrorWith(e => cleanup(e).attempt *> IO.fail(e))

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

  def zip[Other](other: IO[Other]): IO[(A, Other)] =
    for {
      first  <- this
      second <- other
    } yield (first, second)

  def parZip[Other](other: IO[Other])(ec: ExecutionContext): IO[(A, Other)] =
    IO {
      val future1: Future[A]     = Future(this.unsafeRun())(ec)
      val future2: Future[Other] = Future(other.unsafeRun())(ec)

      val zipped: Future[(A, Other)] = future1.zip(future2)

      Await.result(zipped, scala.concurrent.duration.Duration.Inf)
    }
}

object IO {
  def apply[A](block: => A): IO[A] =
    new IO[A] {
      def unsafeRun(): A = block
    }

  def fail[A](error: Throwable): IO[A] =
    IO(throw error)

  def debug(message: String): IO[Unit] =
    IO(Predef.println(s"[${Thread.currentThread().getName}] " + message))

  def sleep(duration: FiniteDuration): IO[Unit] =
    IO {
      Thread.sleep(duration.toMillis)
    }

  def sequence[A](actions: List[IO[A]]): IO[List[A]] =
    actions
      .foldLeft(IO(List.empty[A])) { (state, action) =>
        state.zip(action).map { case (list, a) => a :: list }
      }
      .map(_.reverse)

  def parSequence[A](actions: List[IO[A]])(ec: ExecutionContext): IO[List[A]] =
    actions
      .foldLeft(IO(List.empty[A])) { (state, action) =>
        state.parZip(action)(ec).map { case (list, a) => a :: list }
      }
      .map(_.reverse)

  def traverse[A, B](values: List[A])(action: A => IO[B]): IO[List[B]] =
    sequence(values.map(action))

  def parTraverse[A, B](values: List[A])(action: A => IO[B])(ec: ExecutionContext): IO[List[B]] =
    parSequence(values.map(action))(ec)

}
