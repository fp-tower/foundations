package answers.action.v2

import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import UserCreation._

import scala.util.{Failure, Try}

class UserCreationTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("retry when block always succeeds") {
    val result = retry(1)(2 + 2)(_ => ())
    assert(result == 4)
  }

  test("retry when block always fails") {
    forAll { (error: Exception) =>
      var counter = 0
      val result  = Try(retry(5)(throw error)(_ => counter += 1))

      assert(result == Failure(error))
      assert(counter == 5)
    }
  }

  test("retry when block fails and then succeeds") {
    var counter = 0
    val result  = retry(5) { require(counter >= 3); "" }(_ => counter += 1)

    assert(result == "")
    assert(counter == 3)
  }

  test("retryWithError when block always succeeds") {
    val result = retryWithError(1)(Right(2 + 2))((_: Nothing) => ())
    assert(result == 4)
  }

  test("retryWithError when block always fails") {
    var counter = 0
    val result  = Try(retryWithError(5)(Left(""))(_ => counter += 1))

    assert(result.isFailure)
    assert(counter == 5)
  }

  test("retryWithError when block fails and then succeeds") {
    var counter = 0
    val result  = retryWithError(5) { Either.cond(counter >= 3, "", ()) }(_ => counter += 1)

    assert(result == "")
    assert(counter == 3)
  }

}
