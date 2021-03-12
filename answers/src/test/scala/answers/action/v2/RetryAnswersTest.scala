package answers.action.v2

import answers.action.v2.UserCreationAnswers.{retry, retryWithError}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.util.{Failure, Try}

class RetryAnswersTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("retry when block always succeeds") {
    var counter = 0
    val result = retry(1) { () =>
      counter += 1
      2 + 2
    }
    assert(result == 4)
    assert(counter == 1)
  }

  test("retry when block always fails") {
    forAll { (error: Exception) =>
      var counter = 0
      def exec(): Int = {
        counter += 1
        throw error
      }
      val result = Try(retry(5)(exec))

      assert(result == Failure(error))
      assert(counter == 5)
    }
  }

  test("retry when block fails and then succeeds") {
    var counter = 0
    def exec(): String = {
      counter += 1
      if (counter < 3) throw new Exception("Boom!")
      else "Hello"
    }
    val result = retry(5)(exec)

    assert(result == "Hello")
    assert(counter == 3)
  }

  test("retryWithError when block always succeeds") {
    var counter = 0
    val result = retryWithError(1)(
      block = 2 + 2,
      onError = _ => counter += 1
    )
    assert(result == 4)
    assert(counter == 0)
  }

  test("retryWithError when block always fails") {
    var counter = 0
    val result  = Try(retryWithError(5)(block = throw new Exception("boom"), onError = _ => counter += 1))

    assert(result.isFailure)
    assert(counter == 5)
  }

  test("retryWithError when block fails and then succeeds") {
    var counter = 0
    val result = retryWithError(5)(
      block = if (counter >= 3) "" else throw new Exception("boom"),
      onError = _ => counter += 1
    )

    assert(result == "")
    assert(counter == 3)
  }

}
