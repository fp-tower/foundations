package answers.action.imperative

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

object RetryAnswers {

  @tailrec
  def retry[A](maxAttempt: Int)(block: () => A): A = {
    require(maxAttempt > 0, "maxAttempt must be greater than 0")

    Try(block()) match {
      case Success(value) => value
      case Failure(error) =>
        if (maxAttempt == 1) throw error
        else retry(maxAttempt - 1)(block)
    }
  }

  @tailrec
  def retryWithError[A](maxAttempt: Int)(block: () => A, onError: Throwable => Any): A = {
    require(maxAttempt > 0, "maxAttempt must be greater than 0")

    Try(block()) match {
      case Success(value) => value
      case Failure(error) =>
        onError(error)
        if (maxAttempt == 1) throw error
        else retryWithError(maxAttempt - 1)(block, onError)
    }
  }

  def onError[A](block: () => A, callback: Throwable => Any): A =
    Try(block()) match {
      case Failure(exception) =>
        Try(callback(exception)) // catch failure
        throw exception
      case Success(value) =>
        value
    }

}
