package answers.action.v3

import answers.action.v3.LazyAction.{delay, fail}

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

  def retry(attempts: Int): LazyAction[A] =
    if (attempts <= 0) fail(new IllegalArgumentException("Failed too many times"))
    else if (attempts == 1) this
    else
      delay {
        Try(this.execute()) match {
          case Success(value) => value
          case Failure(_)     => retry(attempts - 1).execute()
        }
      }

}

object LazyAction {
  def delay[A](block: => A): LazyAction[A] =
    new LazyAction[A] {
      def execute(): A = block
    }

  def fail[A](error: Throwable): LazyAction[A] =
    new LazyAction[A] {
      def execute(): A = throw error
    }
}
