package answers.sideeffect

import java.util.concurrent.CountDownLatch

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.{Failure, Success, Try}

/**
  * IO implementation encoded as a Thunk or Async
  *
  * This encoding is made to illustrate how can we incorporate asynchronous computation
  * within a thunk based IO implementation (e.g. IOAnswers.IO)
  *
  * However, it is not stack nor efficient
  */
sealed trait IOAsync[+A] {
  import IOAsync._

  def map[B](f: A => B): IOAsync[B] =
    effect(f(unsafeRun()))

  def flatMap[B](f: A => IOAsync[B]): IOAsync[B] =
    effect(f(unsafeRun()).unsafeRun())

  def attempt: IOAsync[Either[Throwable, A]] =
    effect(Try(unsafeRun()).toEither)

  def unsafeToFuture: Future[A] = {
    val promise = Promise[A]()

    unsafeRunAsync {
      case Left(e)  => promise.failure(e)
      case Right(a) => promise.success(a)
    }

    promise.future
  }

  // adapated from cats-effect
  def unsafeRun(): A = {
    val latch                     = new CountDownLatch(1)
    var res: Either[Throwable, A] = null

    unsafeRunAsync { eOrA =>
      res = eOrA
      latch.countDown()
    }

    latch.await() // await until the latch is opened within unsafeRunAsync callBack

    res.fold(throw _, identity)
  }

  def unsafeRunAsync(cb: Either[Throwable, A] => Unit): Unit =
    this match {
      case Thunk(x) => cb(Try(x()).toEither)
      case Async(f) => f(cb)
    }

}

object IOAsync {

  def succeed[A](value: A): IOAsync[A] =
    Thunk(() => value)

  def fail(error: Exception): IOAsync[Nothing] =
    Thunk(() => throw error)

  def effect[A](value: => A): IOAsync[A] =
    Thunk(() => value)

  val notImplemented: IOAsync[Nothing] =
    effect(???)

  def async[A](k: (Either[Throwable, A] => Unit) => Unit): IOAsync[A] =
    Async(k)

  val never: IOAsync[Nothing] =
    async(_ => ())

  // copied from cats-effect
  def fromFuture[A](fa: => Future[A]): IOAsync[A] =
    async { cb =>
      fa.onComplete(
        r =>
          cb(r match {
            case Success(a) => Right(a)
            case Failure(e) => Left(e)
          })
      )(new ExecutionContext {
        def execute(r: Runnable): Unit = r.run()
        def reportFailure(e: Throwable): Unit =
          println(s"Failed with $e")
      })
    }

  case class Thunk[+A](underlying: () => A) extends IOAsync[A]

  case class Async[A](cb: (Either[Throwable, A] => Unit) => Unit) extends IOAsync[A]

}
