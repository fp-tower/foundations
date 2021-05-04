package exercises.action.async

import java.time.Duration
import java.util.concurrent.CountDownLatch
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}

sealed trait IO[+A] {
  import IO._

  // Executes the action asynchronously.
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

  // Runs the current IO (`this`), discards its result and runs the second IO (`other`).
  // For example,
  // val action1: IO[Unit] = IO(println("Fetching user"))
  // val action2: IO[User] = db.getUser(1234)
  // val action3: IO[User] = action1.andThen(action2)
  // action3.unsafeRun()
  // prints "Fetching user", fetches user 1234 from db and returns it.
  // Note: There is a test for `andThen` in `exercises.action.fp.IOTest`.
  def andThen[Other](other: IO[Other]): IO[Other] =
    flatMap(_ => other)

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
    handleErrorWith(e => cleanup(e) andThen fail(e))

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
    if (maxAttempt <= 0) fail(new IllegalArgumentException("maxAttempt must be greater than 0"))
    else if (maxAttempt == 1) this
    else handleErrorWith(_ => retry(maxAttempt = maxAttempt - 1))

  // Checks if the current IO is a failure or a success.
  // For example,
  // val action: IO[User] = db.getUser(1234)
  // action.attempt.unsafeRun()
  // returns either:
  // 1. Success(User(1234, "Bob", ...)) if `action` was successful or
  // 2. Failure(new Exception("User 1234 not found")) if `action` throws an exception
  def attempt: IO[Try[A]] =
    IO {
      Try(unsafeRun())
    }

  //////////////////////////////////////////////
  // Bonus question (not covered by the videos)
  //////////////////////////////////////////////

  // If the current IO is a success, do nothing.
  // If the current IO is a failure, execute `callback` and keep its result.
  // For example,
  // val user: User = ...
  // val action: IO[Unit] = closeAccount(user.id).handleErrorWith(e =>
  //   logError(e).andThen(emailClient.send(user.email, "Sorry something went wrong"))
  // )
  def handleErrorWith[AA >: A](callback: Throwable => IO[AA]): IO[AA] =
    attempt.flatMap {
      case Success(value)     => IO(value)
      case Failure(exception) => callback(exception)
    }

  // Executes the current IO and `other` sequentially,
  // then returns both results together in a tuple.
  // For example,
  // val action: IO[(User, User)] = db.getUser(1234).zip(db.getUser(5555))
  // action.unsafeRun()
  // It will query the database for user 1234 and then when the data has been received,
  // repeat the same process for user 5555.
  def zip[Other](other: IO[Other]): IO[(A, Other)] =
    ???

  // Executes the current IO and `other` concurrently,
  // then returns both results together in a tuple.
  // For example,
  // val action: IO[(User, User)] = db.getUser(1234).parZip(db.getUser(5555))(ec)
  // action.unsafeRun()
  // It will query the database for both users in parallel.
  def parZip[Other](other: IO[Other])(ec: ExecutionContext): IO[(A, Other)] =
    ???

  def fork(ec: ExecutionContext): IO[Fiber[A]] =
    ???

  def timeout(duration: Duration)(ec: ExecutionContext): IO[A] =
    ???

  def race[Other](other: IO[Other])(ec: ExecutionContext): IO[Either[A, Other]] =
    ???

}

object IO {
  // Constructor for synchronous IO. For example,
  // val greeting: IO[Unit] = IO { println("Hello") }
  // greeting.unsafeRun()
  // prints "Hello"
  def apply[A](action: => A): IO[A] =
    async { callback =>
      val result: Try[A] = Try(action)
      callback(result)
    }

  // Construct an IO which throws `error` everytime it is called.
  def fail(error: Throwable): IO[Nothing] =
    async { callback =>
      callback(Failure(error))
    }

  // Constructor for asynchronous IO.
  def async[A](onComplete: (Try[A] => Unit) => Unit): IO[A] =
    new IO[A] {
      def unsafeRunAsync(callback: Try[A] => Unit): Unit =
        onComplete(callback)
    }

  // prints a debug message to the command line with the name of the current thread
  def debug(message: String): IO[Unit] =
    IO(Predef.println(s"[${Thread.currentThread().getName}] " + message))

  def sleep(duration: Duration): IO[Unit] =
    apply(Thread.sleep(duration.toMillis))

  // executes all IOs in the List sequentially and returns results in the same order.
  def sequence[A](values: List[IO[A]]): IO[List[A]] =
    ???

  // executes all IOs in the List concurrently and returns results in the same order.
  def parSequence[A](values: List[IO[A]])(ec: ExecutionContext): IO[List[A]] =
    ???

  // convenient shortcut for map + sequence (similar to flatMap = map + flatten)
  def traverse[A, B](values: List[A])(action: A => IO[B]): IO[List[B]] =
    sequence(values.map(action))

  // convenient shortcut for map + parSequence (similar to flatMap = map + flatten)
  def parTraverse[A, B](values: List[A])(action: A => IO[B])(ec: ExecutionContext): IO[List[B]] =
    parSequence(values.map(action))(ec)

}
