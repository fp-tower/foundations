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
sealed trait Action[A] {
  import Action._

  def map[To](next: A => To): Action[To] =
    Action {
      next(execute())
    }

  def flatMap[To](next: A => Action[To]): Action[To] =
    Action {
      next(execute()).execute()
    }

  def *>[Other](other: Action[Other]): Action[Other] =
    flatMap(_ => other)

  def attempt: Action[Try[A]] =
    Action {
      Try(execute())
    }

  def start(ec: ExecutionContext): Action[Action[A]] =
    Action {
      val promise = Promise[A]()
      ec.execute(() => this.executeAsync(promise.complete))
      Async(cb => promise.future.onComplete(cb)(ec))
    }

  def zip[Other](other: Action[Other]): Action[(A, Other)] =
    for {
      a <- this
      b <- other
    } yield (a, b)

  def parZip[Other](other: Action[Other])(ec: ExecutionContext): Action[(A, Other)] =
    for {
      startA <- this.start(ec)
      startB <- other.start(ec)
      a      <- startA
      other  <- startB
    } yield (a, other)

  def timeout(duration: FiniteDuration)(ec: ExecutionContext): Action[A] =
    race(sleep(duration) *> fail[Unit](new TimeoutException(s"Action timeout after $duration")))(ec)
      .map {
        case Left(value) => value
        case Right(_)    => sys.error("Impossible")
      }

  // no cancellation
  def race[Other](other: Action[Other])(ec: ExecutionContext): Action[Either[A, Other]] =
    Async[Either[A, Other]](cb => {
      val promise = Promise[Either[A, Other]]()
      ec.execute(() => this.executeAsync(tryA => promise.complete(tryA.map(Left(_)))))
      ec.execute(() => other.executeAsync(tryA => promise.complete(tryA.map(Right(_)))))
      promise.future.onComplete(cb)(ec)
    })

  def repeat(iteration: Int): Action[Unit] =
    if (iteration > 1) flatMap(_ => repeat(iteration - 1))
    else map(_ => ())

  def void: Action[Unit] =
    map(_ => ())

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
      case Thunk(block)    => cb(Try(block()))
      case Async(register) => register(cb)
    }
}

object Action {
  case class Thunk[A](block: () => A)                extends Action[A]
  case class Async[A](register: CallBack[A] => Unit) extends Action[A]

  def apply[A](block: => A): Action[A] =
    Thunk(() => block)

  val unit: Action[Unit] = apply(())

  def println(message: String): Action[Unit] =
    Action(Predef.println(message))

  def log(message: String): Action[Unit] =
    Action(Predef.println(s"[${Thread.currentThread().getName}] " + message))

  def fail[A](error: Throwable): Action[A] =
    Action(throw error)

  def sleep(duration: FiniteDuration): Action[Unit] =
    Action {
      Thread.sleep(duration.toMillis)
    }

  def sequence[A](values: List[Action[A]]): Action[List[A]] =
    values
      .foldLeft(Action(List.empty[A])) { (state, action) =>
        state.zip(action).map { case (list, a) => a :: list }
      }
      .map(_.reverse)

  def sequence_[A](values: List[Action[A]]): Action[Unit] =
    values
      .foldLeft(Action.unit) { (state, action) =>
        state.zip(action).void
      }

  def parSequence[A](values: List[Action[A]])(ec: ExecutionContext): Action[List[A]] =
    sequence(values.map(_.start(ec))).flatMap(sequence)

}
