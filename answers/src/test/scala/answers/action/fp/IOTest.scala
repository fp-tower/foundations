package answers.action.fp

import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.util.{Failure, Success, Try}

class IOTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("apply is lazy and repeatable") {
    var counter = 0

    val action = IO { counter += 1 }
    assert(counter == 0)

    action.unsafeRun()
    assert(counter == 1)

    action.unsafeRun()
    assert(counter == 2)
  }

  test("andThen") {
    var counter = 0

    val first  = IO { counter += 1 }
    val second = IO { counter *= 2 }

    val action = first.andThen(second)
    assert(counter == 0) // nothing happened before unsafeRun

    action.unsafeRun()
    assert(counter == 2) // first and second were executed in the expected order
  }

  test("onError success") {
    var counter = 0

    val action = IO { counter += 1; "" }.onError(_ => IO { counter *= 2 })
    assert(counter == 0) // nothing happened before unsafeRun

    val result = Try(action.unsafeRun())
    assert(counter == 1) // first action was executed but not the callback
    assert(result == Success(""))
  }

  test("onError failure") {
    var counter = 0

    val error1 = new Exception("Boom 1")
    val error2 = new Exception("Boom 2")

    val action = IO { throw error1 }
      .onError(_ => IO { counter += 1 }.andThen(IO { throw error2 }))
    assert(counter == 0) // nothing happened before unsafeRun

    val result = Try(action.unsafeRun())
    assert(counter == 1)              // callback was executed
    assert(result == Failure(error1)) // callback error was swallowed
  }

  test("flatMap") {
    var counter = 0

    val first  = IO { counter += 1 }
    val second = IO { counter *= 2 }

    val action = first.flatMap(_ => second)
    assert(counter == 0) // nothing happened before unsafeRun

    action.unsafeRun()
    assert(counter == 2) // first and second were executed
  }

  test("map") {
    var counter = 0

    val first  = IO { counter += 1 }
    val action = first.map(_ => 1)
    assert(counter == 0) // nothing happened before unsafeRun

    action.unsafeRun()
    assert(counter == 1) // first was executed
  }

}
