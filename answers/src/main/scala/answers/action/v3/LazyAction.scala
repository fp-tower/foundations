package answers.action.v3

import answers.action.v3.LazyAction._

import scala.util.{Failure, Success, Try}

trait LazyAction[A] {

  def execute(): A

  def map[Next](update: A => Next): LazyAction[Next] =
    delay {
      update(execute())
    }

  def andThen[Next](next: A => LazyAction[Next]): LazyAction[Next] =
    delay {
      next(this.execute()).execute()
    }

  def flatMap[Next](next: A => LazyAction[Next]): LazyAction[Next] =
    andThen(next)

  def *>[Other](other: LazyAction[Other]): LazyAction[Other] =
    this.flatMap(_ => other)

  def attempt: LazyAction[Try[A]] =
    delay {
      Try(this.execute())
    }

  def retry(remainingAttempts: Int): LazyAction[A] =
    if (remainingAttempts <= 0) fail(new IllegalArgumentException("Failed too many times"))
    else if (remainingAttempts == 1) this
    else
      attempt.flatMap {
        case Success(value) => cache(value)
        case Failure(_)     => retry(remainingAttempts - 1)
      }
}

object LazyAction {
  def delay[A](block: => A): LazyAction[A] =
    new LazyAction[A] {
      def execute(): A = block
    }

  /** alias for delay */
  def apply[A](block: => A): LazyAction[A] =
    delay(block)

  def fail[A](error: Throwable): LazyAction[A] =
    new LazyAction[A] {
      def execute(): A = throw error
    }

  def cache[A](constant: A): LazyAction[A] =
    new LazyAction[A] {
      def execute(): A = constant
    }
}
