package exercises.action.fp

import org.scalacheck.Gen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.util.{Failure, Success, Try}

class IOTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("apply is lazy and repeatable") {
    var counter = 0

    val action = IO { counter += 1 }
    assert(counter == 0) // nothing happened

    action.unsafeRun()
    assert(counter == 1)

    action.unsafeRun()
    assert(counter == 2)
  }

  // replace `ignore` by `test` to enable this test
  ignore("andThen") {
    var counter = 0

    val first  = IO { counter += 1 }
    val second = IO { counter *= 2 }

    val action = first.andThen(second)
    assert(counter == 0) // nothing happened before unsafeRun

    action.unsafeRun()
    assert(counter == 2) // first and second were executed in the expected order
  }

  ignore("map") {
    var counter = 0

    val first  = IO { counter += 1 }
    val action = first.map(_ => 1)
    assert(counter == 0) // nothing happened before unsafeRun

    action.unsafeRun()
    assert(counter == 1) // first was executed
  }

  ignore("flatMap") {
    var counter = 0

    val first  = IO { counter += 1 }
    val second = IO { counter *= 2 }

    val action = first.flatMap(_ => second)
    assert(counter == 0) // nothing happened before unsafeRun

    action.unsafeRun()
    assert(counter == 2) // first and second were executed
  }

  //////////////////////////////////////////////
  // PART 3: Error handling
  //////////////////////////////////////////////

  ignore("onError success") {
    var counter = 0

    val action = IO { counter += 1; "" }.onError(_ => IO { counter *= 2 })
    assert(counter == 0) // nothing happened before unsafeRun

    val result = Try(action.unsafeRun())
    assert(counter == 1) // first action was executed but not the callback
    assert(result == Success(""))
  }

  ignore("onError failure") {
    var counter = 0
    val error1  = new Exception("Boom 1")

    val action = IO { throw error1 }.onError(_ => IO { counter += 1 })
    assert(counter == 0) // nothing happened before unsafeRun

    val result = Try(action.unsafeRun())
    assert(counter == 1) // callback was executed
    assert(result == Failure(error1))
  }

  ignore("retry, maxAttempt must be greater than 0") {
    val retryAction = IO(1).retry(0)
    val result      = Try(retryAction.unsafeRun())

    assert(result.isFailure)
  }

  ignore("retry until action succeeds") {
    var counter = 0
    val error   = new Exception("Boom")
    val action = IO {
      counter += 1
      if (counter >= 3) "Hello"
      else throw error
    }

    val retryAction = action.retry(5)
    assert(counter == 0)

    val result = Try(retryAction.unsafeRun())
    assert(result == Success("Hello"))
    assert(counter == 3)
  }

  ignore("retry fails if maxAttempt is too low") {
    var counter = 0
    val error   = new Exception("Boom")
    val action = IO {
      counter += 1
      if (counter >= 3) "Hello"
      else throw error
    }

    val retryAction = action.retry(2)
    assert(counter == 0)

    val result = Try(retryAction.unsafeRun())
    assert(result == Failure(error))
    assert(counter == 2)
  }

  //////////////////////////////////////////////
  // PART 4: IO clean-up
  //////////////////////////////////////////////

  ignore("attempt success") {
    var counter = 0

    val action = IO { counter += 1 }.attempt
    assert(counter == 0) // nothing happened before unsafeRun

    val result = action.unsafeRun()
    assert(counter == 1) // action was executed only once
    assert(result.isSuccess)
  }

  ignore("attempt failure") {
    var counter = 0

    val exception = new Exception("Boom")
    val action    = IO { counter += 1; throw exception }.attempt
    assert(counter == 0) // nothing happened before unsafeRun

    val result = action.unsafeRun()
    assert(counter == 1) // action was executed only once
    assert(result == Failure(exception))
  }

}
