package answers.action.imperative

import answers.action.imperative.RetryAnswers.{onError, retry, retryWithError}
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.util.{Failure, Try}

class RetryAnswersTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  val thunkGen: Gen[() => Int] =
    Gen.oneOf(
      Arbitrary.arbitrary[Int].map(() => _),
      Gen.const(() => throw new Exception("Boom"))
    )

  val retryGen: Gen[Int] =
    Gen.choose(1, 20)

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
      action = () => 2 + 2,
      onError = _ => counter += 1
    )
    assert(result == 4)
    assert(counter == 0)
  }

  test("retryWithError when block always fails") {
    var counter = 0
    val result = Try {
      retryWithError(5)(
        action = () => throw new Exception("boom"),
        onError = _ => counter += 1
      )
    }

    assert(result.isFailure)
    assert(counter == 5)
  }

  test("retryWithError when block fails and then succeeds") {
    var counter = 0
    val result = retryWithError(5)(
      action = () => if (counter >= 3) "" else throw new Exception("boom"),
      onError = _ => counter += 1
    )

    assert(result == "")
    assert(counter == 3)
  }

  test("onError") {
    var counter = 0
    onError(() => 1, _ => counter += 1)

  }

  test("retryWithError is consistent with retry + onError") {
    forAll(Gen.listOf(thunkGen), retryGen) { (thunks, maxAttempt) =>
      val it1      = thunks.iterator
      var counter1 = 0
      val result1 = Try(
        retryWithError(maxAttempt)(
          action = () => it1.next().apply(),
          onError = _ => counter1 += 1
        )
      )

      val it2      = thunks.iterator
      var counter2 = 0
      val result2 = Try(
        retry(maxAttempt)(
          action = () =>
            onError(
              action = () => it2.next().apply(),
              callback = _ => counter2 += 1
          )
        )
      )

      assert(result1.toEither.left.map(_.getMessage) == result2.toEither.left.map(_.getMessage))
      assert(counter1 == counter2)
    }
  }

}
