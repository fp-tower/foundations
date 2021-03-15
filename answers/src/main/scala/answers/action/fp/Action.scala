package answers.action.fp

import scala.util.{Failure, Success, Try}

sealed trait Action[A] {

  def execute(): A

  def retry(maxAttempt: Int): Action[A] =
    if (maxAttempt <= 0) Action.fail(new IllegalArgumentException("maxAttempt must be > 0"))
    else if (maxAttempt == 1) this
    else
      attempt.flatMap {
        case Failure(_)     => retry(maxAttempt - 1)
        case Success(value) => Action(value)
      }

  def onError[Other](callback: Throwable => Action[Other]): Action[A] =
    attempt.flatMap {
      case Failure(e)     => callback(e).attempt *> Action.fail(e)
      case Success(value) => Action(value)
    }

  def andThen[Next](callBack: A => Action[Next]): Action[Next] =
    flatMap(callBack)

  def flatMap[Next](callBack: A => Action[Next]): Action[Next] =
    Action {
      val result: A                = execute()
      val nextAction: Action[Next] = callBack(result)

      nextAction.execute()
    }

  def *>[Next](next: Action[Next]): Action[Next] =
    this.flatMap(_ => next)

  def map[Next](callBack: A => Next): Action[Next] =
    Action {
      callBack(execute())
    }

  def attempt: Action[Try[A]] =
    Action {
      Try(execute())
    }
}

object Action {
  def apply[A](block: => A): Action[A] =
    new Action[A] {
      def execute(): A = block
    }

  def fail[A](error: Throwable): Action[A] =
    Action(throw error)

}
