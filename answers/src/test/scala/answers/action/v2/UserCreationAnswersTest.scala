package answers.action.v2

import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import UserCreationAnswers._

import scala.util.{Failure, Try}

class UserCreationAnswersTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

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

  test("retryWithErrorV2 when block always succeeds") {
    val result = retryWithErrorV2(1)(Right(2 + 2))((_: Nothing) => ())
    assert(result == 4)
  }

  test("retryWithErrorV2 when block always fails") {
    var counter = 0
    val result  = Try(retryWithErrorV2(5)(Left(""))(_ => counter += 1))

    assert(result.isFailure)
    assert(counter == 5)
  }

  test("retryWithErrorV2 when block fails and then succeeds") {
    var counter = 0
    val result  = retryWithErrorV2(5) { Either.cond(counter >= 3, "", ()) }(_ => counter += 1)

    assert(result == "")
    assert(counter == 3)
  }

}
