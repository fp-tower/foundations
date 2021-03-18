package answers.action.imperative

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

object RetryAnswers {

  @tailrec
  def retry[A](maxAttempt: Int)(action: () => A): A = {
    require(maxAttempt > 0, "maxAttempt must be greater than 0")

    Try(action()) match {
      case Success(value) => value
      case Failure(error) =>
        if (maxAttempt == 1) throw error
        else retry(maxAttempt - 1)(action)
    }
  }

  @tailrec
  def retryWithError[A](maxAttempt: Int)(action: () => A, onError: Throwable => Any): A = {
    require(maxAttempt > 0, "maxAttempt must be greater than 0")

    Try(action()) match {
      case Success(value) => value
      case Failure(error) =>
        onError(error)
        if (maxAttempt == 1) throw error
        else retryWithError(maxAttempt - 1)(action, onError)
    }
  }

  def onError[A](action: () => A, callback: Throwable => Any): A =
    Try(action()) match {
      case Failure(exception) =>
        Try(callback(exception)) // catch failure
        throw exception
      case Success(value) =>
        value
    }

}
