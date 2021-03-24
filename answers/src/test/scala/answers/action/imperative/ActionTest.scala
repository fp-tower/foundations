package answers.action.imperative

import org.scalacheck.Gen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.util.{Failure, Success, Try}

class ActionTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("retry until action succeeds") {
    var counter = 0
    val result = retry(5) {
      counter += 1
      require(counter >= 3, "Counter is too low")
      "Hello"
    }
    assert(result == "Hello")
    assert(counter == 3)
  }

  test("retry when block action fails") {
    forAll { (error: Exception) =>
      var counter = 0
      val result = Try(retry(5) {
        counter += 1
        throw error
      })

      assert(result == Failure(error))
      assert(counter == 5)
    }
  }

  test("retry until action succeeds PBT") {
    forAll(
      Gen.choose(1, 20),
      Gen.choose(0, 20)
    ) { (maxAttempt: Int, numberOfError: Int) =>
      var counter = 0
      def myMethod(): String =
        if (counter < numberOfError) {
          counter += 1
          throw new Exception("Boom")
        } else "Hello"

      val result = Try(retry(maxAttempt)(myMethod()))

      if (maxAttempt > numberOfError)
        assert(result == Success("Hello"))
      else {
        assert(result.isFailure)
        assert(result.failed.get.getMessage == "Boom")
      }
    }
  }

  test("onError success") {
    var counter = 0
    val result  = onError("Hello", _ => counter += 1)

    assert(result == "Hello")
    assert(counter == 0)
  }

  test("onError failure") {
    var counter = 0
    val result  = Try(onError(throw new Exception("Boom"), _ => counter += 1))

    assert(result.isFailure)
    assert(counter == 1)
  }

  test("onError failure rethrow the initial error") {
    val result = Try(onError(throw new Exception("Boom"), _ => throw new Exception("BadaBoom")))

    assert(result.isFailure)
    assert(result.failed.get.getMessage == "Boom")
  }

  test("onError") {
    forAll { (tryInt: Try[Int]) =>
      var counter = 0
      val result  = Try(onError(tryInt.get, _ => counter += 1))

      assert(result == tryInt)
      tryInt match {
        case Failure(_) => assert(counter == 1)
        case Success(_) => assert(counter == 0)
      }
    }
  }

}
