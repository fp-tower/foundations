package examples

import org.scalatest.{FunSuite, Matchers}
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import examples.Introduction._

class IntroductionTest extends FunSuite with Matchers with ScalaCheckDrivenPropertyChecks {

  reverseString("imperative")(reverseStringImperative)
  reverseString("functional")(reverseStringFunctional)
  reverseString("functional 2")(reverseStringFunctional2)

  def reverseString(name: String)(f: String => String) =
    test("reverseString " + name) {
      forAll((x: String) => x.reverse shouldEqual f(x))
    }

  validateUserNames("imperative")(validateUsernamesImperative)
  validateUserNames("functional")(validateUsernamesFunctional)

  def validateUserNames(name: String)(f: List[String] => String) =
    test("validate usernames  " + name) {
      f(Nil) shouldEqual "no username"
      f(List("foo", "bar")) shouldEqual "all username are valid"
      f(List("foo", "a", "abc123", "bar", "@)01223")) shouldEqual s"Found 3 invalid username"
    }

}
