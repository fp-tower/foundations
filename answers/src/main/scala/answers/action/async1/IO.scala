package answers.action.async1

import java.util.concurrent.CountDownLatch
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

trait IO[A] {
  // Executes the action asynchronously.
  // This method is equivalent to `onComplete` for `Future`
  // This is the ONLY abstract method of the `IO` trait.
  def unsafeRunAsync(callback: Try[A] => Unit): Unit

  // Executes the action synchronously.
  def unsafeRun(): A = {
    val latch          = new CountDownLatch(1)
    var result: Try[A] = null

    unsafeRunAsync { res =>
      result = res
      latch.countDown() // release the latch
    }

    latch.await() // await until `countDown` is called

    result.get
  }

  def zip[Other](other: IO[Other]): IO[(A, Other)] =
    for {
      first  <- this
      second <- other
    } yield (first, second)

  def parZip[Other](other: IO[Other])(ec: ExecutionContext): IO[(A, Other)] =
    for {
      futureFirst  <- this.start(ec)
      futureSecond <- other.start(ec)
      first        <- IO.fromStartedFuture(futureFirst)(ec)
      second       <- IO.fromStartedFuture(futureSecond)(ec)
    } yield (first, second)

  def start(ec: ExecutionContext): IO[Future[A]] =
    IO {
      Future {
        unsafeRun()
      }(ec)
    }

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

  // Runs the current action (`this`) and update the result with `callback`.
  // For example,
  // IO(1).map(x => x + 1).unsafeRun()
  // returns 2
  // db.getUser(1234).map(_.name).unsafeRun()
  // Fetches the user with id 1234 from the database and returns its name.
  // Note: `callback` is expected to be an FP function (total, deterministic, no action).
  //       Use `flatMap` if `callBack` is not an FP function.
  def map[Next](callBack: A => Next): IO[Next] =
    flatMap(value => IO(callBack(value)))

  // Runs the current action (`this`), if it succeeds passes the result to `callback` and
  // runs the second action.
  // For example,
  // val action = db.getUser(1234).flatMap{ user =>
  //   emailClient.send(user.email, "Welcome to the FP Tower!")
  // }
  // action.unsafeRun()
  // Fetches the user with id 1234 from the database and send them an email using the email
  // address found in the database.
  def flatMap[Next](callBack: A => IO[Next]): IO[Next] =
    IO {
      val result: A = unsafeRun()
      callBack(result).unsafeRun()
    }

  // Runs the current action, if it fails it executes `cleanup` and rethrows the original error.
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
  def onError[Other](cleanup: Throwable => IO[Other]): IO[A] =
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

  // Checks if the current IO is a failure or a success.
  // For example,
  // IO(throw exception) == IO(Failure(exception))
  // IO(1).attempt == IO(Success(1))
  def attempt: IO[Try[A]] =
    ???

  //////////////////////////////////////////////
  // Bonus question (not covered by the videos)
  //////////////////////////////////////////////

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
      def unsafeRunAsync(callback: Try[A] => Unit): Unit =
        callback(Try(action))
    }

  // Construct an IO which throws `error` everytime it is called.
  def fail[A](error: Throwable): IO[A] =
    IO(throw error)

  def fromStartedFuture[A](future: Future[A])(ec: ExecutionContext): IO[A] =
    new IO[A] {
      def unsafeRunAsync(callback: Try[A] => Unit): Unit =
        future.onComplete(callback)(ec)
    }

}
