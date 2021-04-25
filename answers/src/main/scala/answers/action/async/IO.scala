package answers.action.async

import java.time.Duration
import java.util.concurrent.CountDownLatch

import scala.concurrent.{ExecutionContext, Promise, TimeoutException}
import scala.util.{Failure, Success, Try}

/** Action supporting async evaluation in a different thread pool.
  * This implementation can cause deadlocks because `execute` is used
  * to implement many internal functions such as `map`, `flatMap` or `attempt`.
  */
sealed trait IO[A] {
  import IO._

  def map[To](next: A => To): IO[To] =
    flatMap(a => IO(next(a)))

  def flatMap[To](next: A => IO[To]): IO[To] =
    FlatMap(this, next)

  def *>[Other](other: IO[Other]): IO[Other] =
    for {
      _      <- this
      result <- other
    } yield result

  def *<[Other](other: IO[Other]): IO[A] =
    for {
      result <- this
      _      <- other
    } yield result

  def attempt: IO[Try[A]] =
    Attempt(this)

  def handleErrorWith(callback: Throwable => IO[A]): IO[A] =
    attempt.flatMap {
      case Success(value)     => IO(value)
      case Failure(exception) => callback(exception)
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

  def timeout(duration: Duration)(ec: ExecutionContext): IO[A] =
    race(sleep(duration) *> fail[Unit](new TimeoutException(s"Action timeout after $duration")))(ec)
      .map {
        case Left(value) => value
        case Right(_)    => sys.error("Impossible")
      }

  // no cancellation
  def race[Other](other: IO[Other])(ec: ExecutionContext): IO[Either[A, Other]] =
    Async[Either[A, Other]] { cb =>
      val promise = Promise[Either[A, Other]]()
      ec.execute(() => this.unsafeRunAsync(tryA => promise.tryComplete(tryA.map(Left(_)))))
      ec.execute(() => other.unsafeRunAsync(tryOther => promise.tryComplete(tryOther.map(Right(_)))))
      promise.future.onComplete(cb)(ec)
    }

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
    runLoop(this)(cb)
}

object IO {
  case class Thunk[A](block: () => A)                                   extends IO[A]
  case class Async[A](onComplete: CallBack[A] => Unit)                  extends IO[A]
  case class FlatMap[From, To](current: IO[From], next: From => IO[To]) extends IO[To]
  case class Attempt[A](current: IO[A])                                 extends IO[Try[A]]

  def runLoop[A](action: IO[A])(cb: CallBack[A]): Unit =
    action match {
      case Thunk(block)    => cb(Try(block()))
      case Async(register) => register(cb)
      case FlatMap(current, next) =>
        runLoop(current) {
          case Failure(e) => cb(Failure(e))
          case Success(x) => runLoop(next(x))(cb)
        }
      case Attempt(current) => runLoop(current)(a => cb(Success(a)))
    }

  def apply[A](action: => A): IO[A] =
    Thunk(() => action)

  val unit: IO[Unit] = apply(())

  def println(message: String): IO[Unit] =
    IO(Predef.println(message))

  def log(message: String): IO[Unit] =
    IO(Predef.println(s"[${Thread.currentThread().getName}] " + message))

  def fail[A](error: Throwable): IO[A] =
    IO(throw error)

  def sleep(duration: Duration): IO[Unit] =
    IO(Thread.sleep(duration.toMillis))

  def sequence[A](values: List[IO[A]]): IO[List[A]] =
    values
      .foldLeft(IO(List.empty[A])) { (state, action) =>
        state.zip(action).map { case (list, a) => a :: list }
      }
      .map(_.reverse)

  def traverse[A, B](values: List[A])(action: A => IO[B]): IO[List[B]] =
    sequence(values.map(action))

  def sequence_[A](values: List[IO[A]]): IO[Unit] =
    values
      .foldLeft(IO.unit) { (state, action) =>
        state.zip(action).void
      }

  def parSequence[A](values: List[IO[A]])(ec: ExecutionContext): IO[List[A]] =
    sequence(values.map(_.start(ec))).flatMap(sequence)

  def parTraverse[A, B](values: List[A])(action: A => IO[B])(ec: ExecutionContext): IO[List[B]] =
    parSequence(values.map(action))(ec)

}
