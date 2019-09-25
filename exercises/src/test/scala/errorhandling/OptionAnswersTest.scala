package errorhandling

import answers.errorhandling.OptionAnswers
import exercises.errorhandling.Country.{France, Switzerland}
import exercises.errorhandling.{User, Username}
import org.scalatest.Matchers
import org.scalatest.funsuite.AnyFunSuite

class OptionAnswersTest extends AnyFunSuite with Matchers {
  import OptionAnswers._

  ////////////////////////
  // 1. Use cases
  ////////////////////////

  test("getOrder") {
    getOrder(123, List(Order(222, "paul"), Order(123, "john"))) shouldEqual Some(Order(123, "john"))
    getOrder(111, List(Order(222, "paul"), Order(123, "john"))) shouldEqual None
  }

  test("charToDigit") {
    0.to(9).map(x => charToDigit(x.toString.head) shouldEqual Some(x))
    charToDigit('A') shouldEqual None
  }

  test("isValidUsername") {
    isValidUsername("foo") shouldEqual true
    isValidUsername("abc_1-2-3") shouldEqual true
    isValidUsername("abc!@£") shouldEqual false
    isValidUsername(" yo") shouldEqual false
  }

  test("validateUsername") {
    validateUsername("foo") shouldEqual Some(Username("foo"))
    validateUsername("  foo ") shouldEqual Some(Username("foo"))
    validateUsername("abc!@£") shouldEqual None
    validateUsername(" yo") shouldEqual None
  }

  test("validateCountry") {
    validateCountry("FRA") shouldEqual Some(France)
    validateCountry("foo") shouldEqual None
    validateCountry("FRANCE") shouldEqual None
    validateCountry("DZA") shouldEqual None // not supported
  }

  validateUserTest(1)(validateUser)

  ////////////////////////
  // 2. Composing Option
  ////////////////////////

  test("tuple2") {
    tuple2(Some(1), Some("hello")) shouldEqual Some((1, "hello"))
    tuple2(Some(1), None) shouldEqual None
  }

  test("map2") {
    map2(Some(1), Some(2))(_ + _) shouldEqual Some(3)
    map2(Some(1), Option.empty[Int])(_ + _) shouldEqual None
  }

  validateUserTest(2)(validateUser_v2)

  validateUserTest(3)(validateUser_v3)

  validateUsernamesTest(1)(validateUsernames)

  test("sequence") {
    sequence(List(Some(1), Some(5), Some(8))) shouldEqual Some(List(1, 5, 8))
    sequence(Nil) shouldEqual Some(Nil)
    sequence(List(Some(1), None, Some(8))) shouldEqual None
  }

  validateUsernamesTest(2)(validateUsernames_v2)

  test("traverse") {
    def checkEven(x: Int): Option[Int] = if (x % 2 == 1) Some(x) else None
    traverse(List(1, 5, 9))(checkEven) shouldEqual Some(List(1, 5, 9))
    traverse(List.empty[Int])(checkEven) shouldEqual Some(Nil)
    traverse(List(1, 4, 9))(checkEven) shouldEqual None
  }

  validateUsernamesTest(3)(validateUsernames_v3)

  def validateUserTest(count: Int)(f: (String, String) => Option[User]) =
    test(s"validateUser $count") {
      validateUser("foo", "FRA") shouldEqual Some(User(Username("foo"), France))
      validateUser("Foo1-2-3", "CHE") shouldEqual Some(User(Username("Foo1-2-3"), Switzerland))
      validateUser("aa", "CHE") shouldEqual None
      validateUser("foo", "123") shouldEqual None
    }

  def validateUsernamesTest(count: Int)(f: List[String] => Option[List[Username]]) =
    test(s"validateUsernames $count") {
      f(List("  foo", "Foo123", "Foo1-2_3")) shouldEqual Some(
        List(Username("foo"), Username("Foo123"), Username("Foo1-2_3"))
      )
      f(List("  foo", "x", "Foo1-2_3")) shouldEqual None
    }
}
