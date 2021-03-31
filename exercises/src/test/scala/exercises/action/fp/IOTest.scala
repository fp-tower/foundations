package exercises.action.fp

import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class IOTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("IO.apply is lazy") {
    var counter = 0

    val action = IO { counter += 1 }
    assert(counter == 0)

    action.unsafeRun()
    assert(counter == 1)
  }

}
