package answers.action.async

import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class IOTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("apply is lazy") {
    var counter = 0
    val action  = IO(counter += 1)

    assert(counter == 0)
    action.unsafeRun()
    assert(counter == 1)
  }

}
