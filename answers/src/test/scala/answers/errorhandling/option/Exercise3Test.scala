package answers.errorhandling.option

import answers.errorhandling.option.Exercise3._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class Exercise3Test extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("filterDigits example") {
    assert(filterDigits("a83mdp2mf845,s=3".toList) == List(8, 3, 2, 8, 4, 5, 3))
  }

  test("checkAllDigits example") {
    assert(checkAllDigits("a83mdp2mf845,s=3".toList) == None)
    assert(checkAllDigits("123456789".toList) == Some(List(1, 2, 3, 4, 5, 6, 7, 8, 9)))
    assert(checkAllDigits("".toList) == Some(Nil))
  }

  test("sequence") {
    forAll { (values: List[Option[Int]]) =>
      if (values.forall(_.isDefined))
        assert(sequence(values) == Some(values.map(_.get)))
      else
        assert(sequence(values) == None)
    }
  }

}
