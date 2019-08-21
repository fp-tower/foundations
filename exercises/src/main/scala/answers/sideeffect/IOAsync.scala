package answers.sideeffect

import java.util.concurrent.CountDownLatch

import scala.concurrent.duration.FiniteDuration
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
sealed trait IOAsync[+A] { self =>
  import IOAsync._

  def map[B](f: A => B): IOAsync[B] =
    effect(f(unsafeRun()))

  def flatMap[B](f: A => IOAsync[B]): IOAsync[B] =
    effect(f(unsafeRun()).unsafeRun())

  def attempt: IOAsync[Either[Throwable, A]] =
    effect(Try(unsafeRun()).toEither)

  def tuple[B](other: IOAsync[B]): IOAsync[(A, B)] =
    map2(other)((_, _))

  def map2[B, C](other: IOAsync[B])(f: (A, B) => C): IOAsync[C] =
    for {
      a <- this
      b <- other
    } yield f(a, b)

  def parTuple[B](other: IOAsync[B]): IOAsync[(A, B)] =
    parMap2(other)((_, _))

  def parMap2[B, C](other: IOAsync[B])(f: (A, B) => C): IOAsync[C] =
    for {
      fa <- this.start
      fb <- other.start
      c  <- fa.map2(fb)(f)
    } yield c

  def *>[B](other: IOAsync[B]): IOAsync[B] =
    flatMap(_ => other)

  def <*[B](other: IOAsync[B]): IOAsync[A] =
    other.*>(this)

  def start: IOAsync[IOAsync[A]] =
    IOAsync.effect {
      val future = self.unsafeToFuture
      IOAsync.fromFuture(future)
    }

  def evalOn(ec: ExecutionContext): IOAsync[A] =
    async(
      cb =>
        ec.execute(new Runnable {
          def run(): Unit = cb(Right(unsafeRun()))
        })
    )

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

  case class Thunk[+A](underlying: () => A) extends IOAsync[A]

  case class Async[A](cb: (Either[Throwable, A] => Unit) => Unit) extends IOAsync[A]

  def succeed[A](value: A): IOAsync[A] =
    Thunk(() => value)

  def fail(error: Exception): IOAsync[Nothing] =
    Thunk(() => throw error)

  def effect[A](value: => A): IOAsync[A] =
    Thunk(() => value)

  def sleep(duration: FiniteDuration): IOAsync[Unit] =
    effect(Thread.sleep(duration.toMillis))

  val notImplemented: IOAsync[Nothing] =
    effect(???)

  def printLine(message: String): IOAsync[Unit] =
    effect(println(message))

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
        def execute(r: Runnable): Unit        = r.run()
        def reportFailure(e: Throwable): Unit = println(s"Failed with $e")
      })
    }

  val threadName: IOAsync[String] =
    IOAsync.effect(Thread.currentThread().getName)

  val printThreadName: IOAsync[Unit] =
    threadName.map(println)

  def sequence[A](xs: List[IOAsync[A]]): IOAsync[List[A]] =
    traverse(xs)(identity)

  def traverse[A, B](xs: List[A])(f: A => IOAsync[B]): IOAsync[List[B]] =
    xs.foldLeft(succeed(List.empty[B]))(
        (facc, a) =>
          for {
            acc <- facc
            a   <- f(a)
          } yield a :: acc
      )
      .map(_.reverse)

  def parTraverse[A, B](xs: List[A])(f: A => IOAsync[B]): IOAsync[List[B]] =
    traverse(xs)(f(_).start).flatMap(sequence)

}
