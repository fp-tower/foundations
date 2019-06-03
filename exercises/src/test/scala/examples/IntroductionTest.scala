package examples

import org.scalatest.{FunSuite, Matchers}
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import examples.Introduction._

class IntroductionTest extends FunSuite with Matchers with ScalaCheckDrivenPropertyChecks {

  test("reverseString Imperative") {
    forAll((x: String) => x.reverse shouldEqual reverseStringImperative(x))
  }

  test("reverseString Functional") {
    forAll((x: String) => x.reverse shouldEqual reverseStringFunctional(x))
  }

  test("reverseString2 Functional") {
    forAll((x: String) => x.reverse shouldEqual reverseStringFunctional2(x))
  }

}
