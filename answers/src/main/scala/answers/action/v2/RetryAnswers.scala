package answers.action.v2

import scala.util.{Failure, Success, Try}

object RetryAnswers {

  def retry[A](maxAttempt: Int)(block: () => A): A = {
    var error: Throwable  = new IllegalArgumentException("Failed too many times")
    var result: Option[A] = None
    var remaining: Int    = maxAttempt

    while (result.isEmpty && remaining > 0) {
      remaining -= 1
      Try(block()) match {
        case Failure(e)     => error = e
        case Success(value) => result = Some(value)
      }
    }

    result.getOrElse(throw error)
  }

  def retryWithError[A](maxAttempt: Int)(block: () => A, onError: Throwable => Any): A = {
    var error: Throwable  = new IllegalArgumentException("Failed too many times")
    var result: Option[A] = None
    var remaining: Int    = maxAttempt

    while (result.isEmpty && remaining > 0) {
      remaining -= 1
      Try(block()) match {
        case Failure(e)     => onError(e); error = e
        case Success(value) => result = Some(value)
      }
    }

    result.getOrElse(throw error)
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
