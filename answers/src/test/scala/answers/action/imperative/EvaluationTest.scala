package answers.action.imperative

import org.scalatest.funsuite.AnyFunSuite

class EvaluationTest extends AnyFunSuite {

  test("a val is only evaluated when created") {
    var counter = 0
    val action  = counter += 1
    assert(counter == 1)
    action
    assert(counter == 1)
    action
    assert(counter == 1)
  }

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

  sealed trait Eval

  case object Never extends Eval
  case object Once  extends Eval
  case object Twice extends Eval

  def testEval(eval: Eval, param: => Unit): Unit =
    eval match {
      case Never => () // do nothing
      case Once  => param
      case Twice =>
        param
        param
    }

  test("by-name parameter never evaluated") {
    var counter = 0
    testEval(Never, counter += 1)
    assert(counter == 0)
  }
  test("by-name parameter evaluated once") {
    var counter = 0
    testEval(Once, counter += 1)
    assert(counter == 1)
  }
  test("by-name parameter evaluated twice") {
    var counter = 0
    testEval(Twice, counter += 1)
    assert(counter == 2)
  }

}
