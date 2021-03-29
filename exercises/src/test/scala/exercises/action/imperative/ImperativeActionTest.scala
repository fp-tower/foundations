package exercises.action.imperative

import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.util.{Failure, Success, Try}

class ImperativeActionTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  ignore("retry when action fails") {
    var counter = 0
    val error   = new Exception("Boom")

    val result = Try(retry(5) {
      counter += 1
      throw error
    })

    assert(result == Failure(error))
    assert(counter == 5)
  }

  ignore("retry until action succeeds") {
    var counter = 0
    val result = Try(retry(5) {
      counter += 1
      require(counter >= 3, "Counter is too low")
      "Hello"
    })
    assert(result == Success("Hello"))
    assert(counter == 3)
  }

  ignore("onError success") {
    var counter = 0
    val result  = onError("Hello", _ => counter += 1)

    assert(result == "Hello")
    assert(counter == 0)
  }

  ignore("onError failure") {
    var counter = 0
    val result  = Try(onError(throw new Exception("Boom"), _ => counter += 1))

    assert(result.isFailure)
    assert(counter == 1)
  }

  ignore("onError failure rethrow the initial error") {
    val result = Try(onError(throw new Exception("Boom"), _ => throw new Exception("BadaBoom")))

    assert(result.isFailure)
    assert(result.failed.get.getMessage == "Boom")
  }
}
