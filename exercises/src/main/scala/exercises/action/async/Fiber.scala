package exercises.action.async

import scala.concurrent.{ExecutionContext, Promise}

// Fibers in cats-effect, Monix or ZIO also support cancellation
trait Fiber[+A] {
  def join: IO[A]
}

object Fiber {
  def fromPromise[A](promise: Promise[A])(ec: ExecutionContext): Fiber[A] =
    new Fiber[A] {
      def join: IO[A] =
        IO.async(promise.future.onComplete(_)(ec))
    }
}
