package answers.action

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

package object imperative {

  @tailrec
  def retry[A](maxAttempt: Int)(action: => A): A = {
    require(maxAttempt > 0, "maxAttempt must be greater than 0")

    Try(action) match {
      case Success(value) => value
      case Failure(error) =>
        if (maxAttempt == 1) throw error
        else retry(maxAttempt - 1)(action)
    }
  }

  def onError[A](action: => A, cleanup: Throwable => Any): A =
    Try(action) match {
      case Failure(exception) =>
        Try(cleanup(exception)) // catch failure
        throw exception
      case Success(value) =>
        value
    }

}
