package errorhandling

import exercises.errorhandling.OptionExercises._
import exercises.errorhandling.InvariantOption
import org.scalatest.Matchers
import org.scalatest.funsuite.AnyFunSuite

class OptionExercisesTest extends AnyFunSuite with Matchers {

  test("getUserEmail") {
    val userMap = Map(
      UserId(222) -> User(UserId(222), "john", Some(Email("j@x.com"))),
      UserId(123) -> User(UserId(123), "elisa", Some(Email("e@y.com"))),
      UserId(444) -> User(UserId(444), "bob", None)
    )

    getUserEmail(UserId(123), userMap) shouldEqual Some(Email("e@y.com"))
    getUserEmail(UserId(444), userMap) shouldEqual None
    getUserEmail(UserId(111), userMap) shouldEqual None
  }

  test("optSingleAccountId") {}

  test("optEditor") {}

  test("parseShape") {
    parseShape("C 5") shouldEqual InvariantOption.Some(Shape.Circle(5))
  }

  test("filterDigits") {}

  test("checkAllDigits") {}

}
