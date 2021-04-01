package exercises.action.fp

import scala.util.{Failure, Success, Try}

trait IO[A] {
  def unsafeRun(): A

  // `andThen` creates a new IO which executes the current IO (this)
  // and `other` one after another.
  // `andThen` discards the result of the first IO and returns the result of `other`.
  // For example,
  // val newAction: IO[Int] = IO(println("Hello")).andThen(IO(1))
  // newAction.unsafeRun()
  // prints "Hello" and returns 1.
  // Note that at the creation of `newAction`, none of the IO were executed.
  // We only a message printed on the command line after calling `unsafeRun`.
  // Note: You can find a test for `andThen` in `exercises.action.fp.IOTest`.
  def andThen[Other](other: IO[Other]): IO[Other] =
    ???

  // Runs the current action, if it fails, execute the `callback` and rethrow the original error.
  // If the current action is a success, return the result.
  // For example,
  // def logError(e: Throwable): IO[Unit] =
  //   IO{ println("Got an error: ${e.getMessage}") }
  //
  // IO(1).onError(logError).unsafeRun()
  // returns 1 and nothing is printed on the console
  //
  // IO(throw new Exception("Boom!")).onError(logError).unsafeRun()
  // prints "Got an error: Boom!" and throws new Exception("Boom!")
  //
  // Note: if the IO produced by `callback` throws an exception, then
  // we have two errors: one from the original IO, and one from the callback.
  // In this case, `onError` should rethrow the former and swallow the latter.
  def onError[Other](callback: Throwable => IO[Other]): IO[A] =
    ???

  // Runs the current action and passes the result to callback.
  // For example,
  // IO(1).flatMap(x => IO(x + 1)).unsafeRun()
  // returns 2
  def flatMap[Other](callBack: A => IO[Other]): IO[Other] =
    ???

  def retry(maxAttempt: Int): IO[A] =
    ???

  // Checks if the current is a failure or a success. For example,
  // IO(1).attempt == IO(Success(1))
  // IO(throw exception) == IO(Failure(exception))
  // Note: `io.attempt.unsafeRun()` cannot throw an exception.
  def attempt: IO[Try[A]] =
    ???

}

object IO {
  def apply[A](action: => A): IO[A] =
    new IO[A] {
      def unsafeRun(): A = action
    }
}
