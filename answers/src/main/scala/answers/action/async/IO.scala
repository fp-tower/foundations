package answers.action.async

import java.util.concurrent.CountDownLatch

import scala.concurrent.{ExecutionContext, Promise, TimeoutException}
import scala.concurrent.duration.FiniteDuration
import scala.util.Try

/**
  * Action supporting async evaluation in a different thread pool.
  * This implementation can cause deadlocks because `execute` is used
  * to implement many internal functions such as `map`, `flatMap` or `attempt`.
  */
sealed trait IO[A] {
  import IO._

  def map[To](next: A => To): IO[To] =
    IO {
      next(unsafeRun())
    }

  def flatMap[To](next: A => IO[To]): IO[To] =
    IO {
      next(unsafeRun()).unsafeRun()
    }

  def *>[Other](other: IO[Other]): IO[Other] =
    flatMap(_ => other)

  def attempt: IO[Try[A]] =
    IO {
      Try(unsafeRun())
    }

  def start(ec: ExecutionContext): IO[IO[A]] =
    IO {
      val promise = Promise[A]()
      ec.execute(() => this.unsafeRunAsync(promise.complete))
      Async(cb => promise.future.onComplete(cb)(ec))
    }

  def zip[Other](other: IO[Other]): IO[(A, Other)] =
    for {
      a <- this
      b <- other
    } yield (a, b)

  def parZip[Other](other: IO[Other])(ec: ExecutionContext): IO[(A, Other)] =
    for {
      startA <- this.start(ec)
      startB <- other.start(ec)
      a      <- startA
      other  <- startB
    } yield (a, other)

  def timeout(duration: FiniteDuration)(ec: ExecutionContext): IO[A] =
    race(sleep(duration) *> fail[Unit](new TimeoutException(s"Action timeout after $duration")))(ec)
      .map {
        case Left(value) => value
        case Right(_)    => sys.error("Impossible")
      }

  // no cancellation
  def race[Other](other: IO[Other])(ec: ExecutionContext): IO[Either[A, Other]] =
    Async[Either[A, Other]](cb => {
      val promise = Promise[Either[A, Other]]()
      ec.execute(() => this.unsafeRunAsync(tryA => promise.complete(tryA.map(Left(_)))))
      ec.execute(() => other.unsafeRunAsync(tryA => promise.complete(tryA.map(Right(_)))))
      promise.future.onComplete(cb)(ec)
    })

  def repeat(iteration: Int): IO[Unit] =
    if (iteration > 1) flatMap(_ => repeat(iteration - 1))
    else map(_ => ())

  def void: IO[Unit] =
    map(_ => ())

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

  def unsafeRunAsync(cb: CallBack[A]): Unit =
    this match {
      case Thunk(block)    => cb(Try(block()))
      case Async(register) => register(cb)
    }
}

object IO {
  case class Thunk[A](block: () => A)                extends IO[A]
  case class Async[A](register: CallBack[A] => Unit) extends IO[A]

  def apply[A](block: => A): IO[A] =
    Thunk(() => block)

  val unit: IO[Unit] = apply(())

  def println(message: String): IO[Unit] =
    IO(Predef.println(message))

  def log(message: String): IO[Unit] =
    IO(Predef.println(s"[${Thread.currentThread().getName}] " + message))

  def fail[A](error: Throwable): IO[A] =
    IO(throw error)

  def sleep(duration: FiniteDuration): IO[Unit] =
    IO {
      Thread.sleep(duration.toMillis)
    }

  def sequence[A](values: List[IO[A]]): IO[List[A]] =
    values
      .foldLeft(IO(List.empty[A])) { (state, action) =>
        state.zip(action).map { case (list, a) => a :: list }
      }
      .map(_.reverse)

  def sequence_[A](values: List[IO[A]]): IO[Unit] =
    values
      .foldLeft(IO.unit) { (state, action) =>
        state.zip(action).void
      }

  def parSequence[A](values: List[IO[A]])(ec: ExecutionContext): IO[List[A]] =
    sequence(values.map(_.start(ec))).flatMap(sequence)

}
