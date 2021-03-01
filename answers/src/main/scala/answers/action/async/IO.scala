package answers.action.async

import java.util.concurrent.CountDownLatch

import scala.concurrent.{ExecutionContext, Promise}
import scala.concurrent.duration.FiniteDuration
import scala.util.Try

/**
  * IO supporting async evaluation in a different thread pool.
  * This implementation can cause deadlocks because `execute` is used
  * to implement many internal functions such as `map`, `flatMap` or `attempt`.
  */
sealed trait IO[A] {
  import IO._

  def map[To](next: A => To): IO[To] =
    delay {
      next(execute())
    }

  def flatMap[To](next: A => IO[To]): IO[To] =
    delay {
      next(execute()).execute()
    }

  def *>[Other](other: IO[Other]): IO[Other] =
    flatMap(_ => other)

  def attempt: IO[Try[A]] =
    delay {
      Try(execute())
    }

  def evalOn(ec: ExecutionContext): IO[A] =
    EvalOn(this, ec)

  def start(ec: ExecutionContext): IO[IO[A]] =
    delay {
      val promise = Promise[A]()
      evalOn(ec).executeAsync(promise.complete)
      Async(cb => promise.future.onComplete(cb)(ec))
    }

  def parMap2[B, C](other: IO[B])(combine: (A, B) => C)(ec: ExecutionContext): IO[C] =
    for {
      startA <- this.start(ec)
      startB <- other.start(ec)
      a      <- startA
      b      <- startB
    } yield combine(a, b)

  // no cancellation
  def race[Other](other: IO[Other])(ec: ExecutionContext): IO[Either[A, Other]] =
    Async[Either[A, Other]](cb => {
      this.evalOn(ec).executeAsync(tryA => cb(tryA.map(Left(_))))
      other.evalOn(ec).executeAsync(tryB => cb(tryB.map(Right(_))))
    })

  def repeat(iteration: Int): IO[Unit] =
    if (iteration > 1) flatMap(_ => repeat(iteration - 1))
    else map(_ => ())

  def execute(): A = {
    val latch          = new CountDownLatch(1)
    var result: Try[A] = null

    executeAsync { res =>
      result = res
      latch.countDown() // release the latch
    }

    latch.await() // await until `countDown` is called

    result.get
  }

  def executeAsync(cb: CallBack[A]): Unit =
    this match {
      case Thunk(block)   => cb(Try(block()))
      case Async(runCB)   => runCB(cb)
      case EvalOn(io, ec) => ec.execute(() => io.executeAsync(cb))
    }
}

object IO {
  def println(message: String): IO[Unit] =
    delay(Predef.println(message))

  def log(message: String): IO[Unit] =
    delay(Predef.println(s"[${Thread.currentThread().getName}]" + message))

  def delay[A](block: => A): IO[A] =
    Thunk(() => block)

  def fail[A](error: Throwable): IO[A] =
    delay(throw error)

  def sleep(duration: FiniteDuration): IO[Unit] =
    delay {
      Thread.sleep(duration.toMillis)
    }

  case class Thunk[A](block: () => A)                           extends IO[A]
  case class Async[A](runCB: CallBack[A] => Unit)               extends IO[A]
  case class EvalOn[A](underlying: IO[A], ec: ExecutionContext) extends IO[A]

}
