package exercises.sideeffect

import java.util.concurrent.{CountDownLatch, ExecutorService}

import exercises.sideeffect.IOAsyncExercises.IO.succeed

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.{Failure, Success, Try}

object IOAsyncExercisesApp extends App {
  import IOAsyncExercises.IO._
  import IOAsyncExercises._

  val es: ExecutorService  = ThreadPoolUtil.fixedSize(2, "IOAsyncExercises")
  val ec: ExecutionContext = ExecutionContext.fromExecutorService(es)

  parMap2(printThreadName, printThreadName)((_, _) => ())(ec).unsafeRun()
}

object IOAsyncExercises {

  ////////////////////////
  // 1. Concurrent API
  ////////////////////////

  // 1a. Implement parMap2 such as both IO are executed concurrently
  def parMap2[A, B, C](fa: IO[A], fb: IO[B])(f: (A, B) => C)(ec: ExecutionContext): IO[C] =
    IO.notImplemented

  // 1b. Implement parMap3 such as all 3 IO are executed concurrently
  // try to reuse parMap2
  def parMap3[A, B, C, D](fa: IO[A], fb: IO[B], fc: IO[B])(f: (A, B, C) => D)(ec: ExecutionContext): IO[D] =
    IO.notImplemented

  // 1c. Implement parSequence such as each IO within the list is executed concurrently
  def sequence[A](xs: List[IO[A]]): IO[List[A]] =
    xs.foldLeft(succeed(List.empty[A]))(
        (facc: IO[List[A]], fa: IO[A]) =>
          for {
            acc <- facc
            a   <- fa
          } yield a :: acc
      )
      .map(_.reverse)

  def parSequence[A](xs: List[IO[A]])(ec: ExecutionContext): IO[List[A]] =
    IO.notImplemented

  // 1d. Implement never such as it is never returns when executed
  // try to implement it without sleep or other blocking operation
  val never: IO[Nothing] =
    IO.notImplemented

  /////////////////////////
  // 2. Concurrent Program
  /////////////////////////

  case class UserId(value: String)
  case class OrderId(value: String)
  case class User(userId: UserId, name: String, orderIds: List[OrderId])

  trait UserOrderApi {
    def getUser(userId: UserId): IO[User]
    def deleteOrder(orderId: OrderId): IO[Unit]
  }

  // 2a. Implement deleteAllUserOrders such as it fetches a user: User and delete all orders concurrently
  // e.g. if getUser returns User(UserId("1234"), "Rob", List(OrderId("1111"), OrderId("5555")))
  //      then we would call deleteOrder(OrderId("1111")) and deleteOrder(OrderId("5555")) concurrently
  def deleteAllUserOrders(api: UserOrderApi)(userId: UserId): IO[Unit] =
    IO.notImplemented

  /**
    *    Basic IO implementation with Thunk and Async primitive
    *   This implementation is not fully async, map, flatMap and attempt are blocking
    */
  sealed trait IO[+A] { self =>
    import IO._

    def map[B](f: A => B): IO[B] =
      effect {
        val a = unsafeRun()
        f(a)
      }

    def flatMap[B](f: A => IO[B]): IO[B] =
      effect {
        val a = unsafeRun()
        f(a).unsafeRun()
      }

    def attempt: IO[Either[Throwable, A]] =
      effect {
        Try(unsafeRun()).toEither
      }

    def tuple[B](other: IO[B]): IO[(A, B)] =
      map2(other)((_, _))

    def map2[B, C](other: IO[B])(f: (A, B) => C): IO[C] =
      for {
        a <- this
        b <- other
      } yield f(a, b)

    def *>[B](other: IO[B]): IO[B] =
      flatMap(_ => other)

    def <*[B](other: IO[B]): IO[A] =
      other.*>(this)

    def start(ec: ExecutionContext): IO[IO[A]] =
      effect {
        val future = evalOn(ec).unsafeToFuture
        IO.fromFuture(future)
      }

    def evalOn(ec: ExecutionContext): IO[A] =
      async(unsafeRunAsync)(ec)

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

    def unsafeRunAsync(cb: Callback[A]): Unit =
      this match {
        case Thunk(x) =>
          cb(Try(x()).toEither)
        case Async(f, ec) =>
          ec.execute(new Runnable {
            def run(): Unit = f(cb)
          })
      }
  }

  object IO {

    type Callback[-A] = Either[Throwable, A] => Unit

    case class Thunk[+A](underlying: () => A) extends IO[A]

    case class Async[+A](cb: Callback[A] => Unit, ec: ExecutionContext) extends IO[A]

    def succeed[A](value: A): IO[A] =
      Thunk(() => value)

    def fail(error: Throwable): IO[Nothing] =
      Thunk(() => throw error)

    def effect[A](value: => A): IO[A] =
      Thunk(() => value)

    def apply[A](value: => A): IO[A] =
      effect(value)

    def sleep(duration: FiniteDuration): IO[Unit] =
      effect(Thread.sleep(duration.toMillis))

    val notImplemented: IO[Nothing] =
      effect(???)

    def printLine(message: String): IO[Unit] =
      effect(println(message))

    def async[A](f: Callback[A] => Unit)(ec: ExecutionContext): IO[A] =
      Async(f, ec)

    val immediateEC: ExecutionContext = new ExecutionContext {
      def execute(r: Runnable): Unit        = r.run()
      def reportFailure(e: Throwable): Unit = ExecutionContext.defaultReporter(e)
    }

    // adapted from cats-effect
    def fromFuture[A](fa: => Future[A]): IO[A] =
      async[A] { cb =>
        fa.onComplete(
          r =>
            cb(r match {
              case Success(a) => Right(a)
              case Failure(e) => Left(e)
            })
        )(immediateEC)
      }(immediateEC) // Future is already running on an ExecutionContext

    val threadName: IO[String] =
      IO.effect(Thread.currentThread().getName)

    val printThreadName: IO[Unit] =
      threadName.map(println)
  }

}
