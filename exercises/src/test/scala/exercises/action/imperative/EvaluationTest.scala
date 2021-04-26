package exercises.action.imperative

import org.scalatest.funsuite.AnyFunSuite

// Run the test using the green arrow next to class name (if using IntelliJ)
// or run `sbt` in the terminal to open it in shell mode, then type:
// testOnly exercises.action.imperative.EvaluationTest
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
