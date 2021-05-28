package answers.action.async

import java.util.concurrent.CountDownLatch
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Promise, TimeoutException}
import scala.util.{Failure, Success, Try}

sealed trait IO[+A] {
  import IO._

  def unsafeRunAsync(callback: Try[A] => Unit): Unit

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

  def andThen[Other](other: IO[Other]): IO[Other] =
    flatMap(_ => other)

  def *>[Other](other: IO[Other]): IO[Other] = andThen(other)
  def *<[Other](other: IO[Other]): IO[A]     = flatMap(a => other.map(_ => a))

  def map[Next](callBack: A => Next): IO[Next] =
    flatMap(value => IO(callBack(value)))

  def flatMap[Next](callBack: A => IO[Next]): IO[Next] =
    async { cb =>
      unsafeRunAsync {
        case Failure(exception) => cb(Failure(exception))
        case Success(value)     => callBack(value).unsafeRunAsync(cb)
      }
    }

  def onError[Other](cleanup: Throwable => IO[Other]): IO[A] =
    handleErrorWith(e => cleanup(e).attempt andThen fail(e))

  def retry(maxAttempt: Int): IO[A] =
    if (maxAttempt <= 0) fail(new IllegalArgumentException("maxAttempt must be greater than 0"))
    else if (maxAttempt == 1) this
    else handleErrorWith(_ => retry(maxAttempt - 1))

  def attempt: IO[Try[A]] =
    async { cb =>
      unsafeRunAsync(result => cb(Success(result)))
    }

  def handleErrorWith[AA >: A](callback: Throwable => IO[AA]): IO[AA] =
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
    async { callback =>
      val promise1 = Promise[A]()
      val promise2 = Promise[Other]()
      ec.execute(() => this.unsafeRunAsync(promise1.complete))
      ec.execute(() => other.unsafeRunAsync(promise2.complete))

      promise1.future.zip(promise2.future).onComplete(callback)(ec)
    }

  // other implementation of `parZip` using `Fiber`
  def parZip2[Other](other: IO[Other])(ec: ExecutionContext): IO[(A, Other)] =
    for {
      fiber1 <- this.fork(ec)
      fiber2 <- other.fork(ec)
      first  <- fiber1.join
      second <- fiber2.join
    } yield (first, second)

  def fork(ec: ExecutionContext): IO[Fiber[A]] =
    IO {
      val promise = Promise[A]()
      ec.execute(() => this.unsafeRunAsync(promise.complete))
      Fiber.fromPromise(promise)(ec)
    }

  def evalOn(ec: ExecutionContext): IO[A] =
    fork(ec).flatMap(_.join)

  def timeout(duration: FiniteDuration)(ec: ExecutionContext): IO[A] =
    race(sleep(duration) *> fail(new TimeoutException("Timeout")))(ec)
      .map {
        case Left(value) => value
        case Right(_)    => sys.error("Impossible")
      }

  def race[Other](other: IO[Other])(ec: ExecutionContext): IO[Either[A, Other]] =
    async { callback =>
      val promise = Promise[Either[A, Other]]()
      ec.execute(() => this.map(Left(_)).unsafeRunAsync(promise.tryComplete))
      ec.execute(() => other.map(Right(_)).unsafeRunAsync(promise.tryComplete))
      promise.future.onComplete(callback)(ec)
    }

}

object IO {
  def apply[A](action: => A): IO[A] =
    async { callback =>
      val result: Try[A] = Try(action)
      callback(result)
    }

  def fail(error: Throwable): IO[Nothing] =
    async { callback =>
      callback(Failure(error))
    }

  def async[A](onComplete: (Try[A] => Unit) => Unit): IO[A] =
    new IO[A] {
      def unsafeRunAsync(callback: Try[A] => Unit): Unit =
        onComplete(callback)
    }

  def debug(message: String): IO[Unit] =
    IO(Predef.println(s"[${Thread.currentThread().getName}] " + message))

  def sleep(duration: FiniteDuration): IO[Unit] =
    IO(Thread.sleep(duration.toMillis))

  def sequence[A](actions: List[IO[A]]): IO[List[A]] =
    actions
      .foldLeft(IO(List.empty[A])) { (state, action) =>
        state.zip(action).map { case (list, a) => a :: list }
      }
      .map(_.reverse)

  // copy-paste `sequence` with `parZip` instead of `zip`
  def parSequence[A](values: List[IO[A]])(ec: ExecutionContext): IO[List[A]] =
    values
      .foldLeft(IO(List.empty[A])) { (state, action) =>
        state.parZip(action)(ec).map { case (list, a) => a :: list }
      }
      .map(_.reverse)

  // Other implementation of `parSequence` using `fork` and `join`
  def parSequence2[A](values: List[IO[A]])(ec: ExecutionContext): IO[List[A]] =
    for {
      fibers  <- values.traverse(_.fork(ec))
      results <- fibers.traverse(_.join)
    } yield results

  def traverse[A, B](values: List[A])(action: A => IO[B]): IO[List[B]] =
    sequence(values.map(action))

  def parTraverse[A, B](values: List[A])(action: A => IO[B])(ec: ExecutionContext): IO[List[B]] =
    parSequence(values.map(action))(ec)

}
