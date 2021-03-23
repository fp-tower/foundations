package answers.action.async

import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class ActionTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("apply is lazy") {
    var counter = 0
    val action  = Action(counter += 1)

    assert(counter == 0)
    action.execute()
    assert(counter == 1)
  }

}
