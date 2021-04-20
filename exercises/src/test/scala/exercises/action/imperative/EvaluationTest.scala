package exercises.action.imperative

import org.scalatest.funsuite.AnyFunSuite

class EvaluationTest extends AnyFunSuite {

  test("a lazy val is only evaluated when first accessed") {
    var counter     = 0
    lazy val action = counter += 1
    assert(counter == 0)
    action
    assert(counter == 1)
    action
    assert(counter == 1)
  }

  test("a def is evaluated every time it is called") {
    var counter  = 0
    def action() = counter += 1
    assert(counter == 0)
    action()
    assert(counter == 1)
    action()
    assert(counter == 2)
  }

}
