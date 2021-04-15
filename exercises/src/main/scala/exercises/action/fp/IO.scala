package exercises.action.fp

import scala.util.{Failure, Success, Try}

trait IO[A] {
  // Executes the action.
  // This is the ONLY abstract method of the `IO` trait.
  def unsafeRun(): A

  // Runs the current IO (`this`), discards its result and runs the second IO (`other`).
  // For example,
  // val action1: IO[Unit] = IO(println("Fetching user"))
  // val action2: IO[User] = db.getUser(1234)
  // val action3: IO[User] = action1.andThen(action2)
  // action3.unsafeRun()
  // prints "Fetching user", fetches user 1234 from db and returns it.
  // Note: There is a test for `andThen` in `exercises.action.fp.IOTest`.
  def andThen[Other](other: IO[Other]): IO[Other] =
    ???

  // Popular alias for `andThen` (cat-effect, Monix, ZIO).
  // For example,
  // action1 *> action2 *> action3
  // Note: The arrow head points toward the result we keep.
  //       Another popular symbol is <* so that `action1 <* action2`
  //       executes `action1` and then `action2` but returns the result of `action1`
  def *>[Other](other: IO[Other]): IO[Other] =
    andThen(other)

  // Runs the current action, if it fails it executes the `callback` and rethrows the original error.
  // If the current action is a success, it will return the result.
  // For example,
  // def logError(e: Throwable): IO[Unit] =
  //   IO{ println("Got an error: ${e.getMessage}") }
  //
  // IO(1).onError(logError).unsafeRun()
  // returns 1 and nothing is printed to the console
  //
  // IO(throw new Exception("Boom!")).onError(logError).unsafeRun()
  // prints "Got an error: Boom!" and throws new Exception("Boom!")
  //
  // Note: if the IO produced by `callback` throws an exception, then
  // we have two errors: one from the current IO, and one from the callback.
  // In this case, `onError` should rethrow the former and swallow the error from `callback`.
  def onError[Other](callback: Throwable => IO[Other]): IO[A] =
    ???

  // Runs the current action (`this`) and update the result with `callback`.
  // For example,
  // IO(1).map(x => x + 1).unsafeRun()
  // returns 2
  // db.getUser(1234).map(_.name).unsafeRun()
  // fetches the user 1234 from the database and returns its name
  def map[Next](callBack: A => Next): IO[Next] =
    ???

  // Runs the current action (`this`), if it succeeds passes the result to callback and
  // runs the second action.
  // For example,
  // IO(1).flatMap(x => IO(x + 1)).unsafeRun()
  // returns 2
  def flatMap[Next](callBack: A => IO[Next]): IO[Next] =
    ???

  // Retries this action until either:
  // * It succeeds.
  // * Or the number of attempts have been exhausted.
  // For example,
  // var counter = 0
  // val action: IO[String] = {
  //   counter += 1
  //   require(counter >= 3, "Counter is too low")
  //   "Hello"
  // }
  // action.retry(maxAttempt = 5).unsafeRun()
  // Returns "Hello" because `action` fails twice and then succeeds when counter reaches 3.
  // Note: `maxAttempt` must be greater than 0, otherwise the `IO` should fail.
  // Note: `retry` is a no-operation when `maxAttempt` is equal to 1.
  def retry(maxAttempt: Int): IO[A] =
    ???

  //////////////////////////////////////////////
  // Bonus question (not covered by the video)
  //////////////////////////////////////////////

  // Checks if the current IO is a failure or a success.
  // For example,
  // IO(throw exception) == IO(Failure(exception))
  // IO(1).attempt == IO(Success(1))
  def attempt: IO[Try[A]] =
    ???

  // If the current IO is a success, do nothing.
  // If the current IO is a failure, use `callback`.
  // For example,
  // val action: IO[Int] = IO(throw exception).handleErrorWith(e => IO(1))
  // action.unsafeRun()
  // returns 1
  def handleErrorWith(callback: Throwable => IO[A]): IO[A] =
    ???
}

object IO {
  // Constructor for IO. For example,
  // val greeting: IO[Unit] = IO { println("Hello") }
  // greeting.unsafeRun()
  // prints "Hello"
  def apply[A](action: => A): IO[A] =
    new IO[A] {
      def unsafeRun(): A = action
    }
}
