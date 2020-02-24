package examples

import cats.data.NonEmptyList
import examples.Introduction._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class IntroductionTest extends AnyFunSuite with Matchers with ScalaCheckDrivenPropertyChecks {

  reverseString("imperative")(reverseStringImperative)
  reverseString("functional")(reverseStringFunctional)
  reverseString("functional 2")(reverseStringFunctional2)

  def reverseString(name: String)(f: String => String) =
    test("reverseString " + name) {
      forAll((x: String) => x.reverse shouldEqual f(x))
    }

  test("validate usernames imperative") {
    validateUsernamesImperative(Nil) shouldEqual "no username"
    validateUsernamesImperative(List("foo", "bar")) shouldEqual "all username are valid"
    validateUsernamesImperative(List("foo", "a", "abc123", "bar", "@)01223")) shouldEqual s"Found 3 invalid username"
  }

  test("validate usernames functional") {
    validateUsernamesFunctional(NonEmptyList.of("foo", "bar")) shouldEqual "all username are valid"
    validateUsernamesFunctional(NonEmptyList.of("foo", "a", "abc123", "bar", "@)01223")) shouldEqual s"Found 3 invalid username"
  }

}
