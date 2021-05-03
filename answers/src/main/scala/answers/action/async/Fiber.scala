package answers.action.async

import scala.concurrent.{ExecutionContext, Promise}

trait Fiber[+A] {
  def join: IO[A]
}

object Fiber {
  def fromPromise[A](promise: Promise[A])(ec: ExecutionContext): Fiber[A] =
    new Fiber[A] {
      def join: IO[A] =
        IO.Async(promise.future.onComplete(_)(ec))
    }
}
