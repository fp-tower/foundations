package errorhandling

import answers.errorhandling.OptionAnswers.Role._
import answers.errorhandling.OptionAnswers._
import org.scalatest.Matchers
import org.scalatest.funsuite.AnyFunSuite

class OptionAnswersTest extends AnyFunSuite with Matchers {

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

  test("optSingleAccountId") {
    Editor(AccountId(123), "Comic Sans").optSingleAccountId shouldEqual Some(AccountId(123))
    Reader(AccountId(123), premiumUser = true).optSingleAccountId shouldEqual Some(AccountId(123))
    Admin.optSingleAccountId shouldEqual None
  }

  test("optEditor") {
    val editor = Editor(AccountId(123), "Comic Sans")
    editor.optEditor shouldEqual Some(editor)
    Reader(AccountId(123), premiumUser = true).optEditor shouldEqual None
    Admin.optEditor shouldEqual None
  }

  test("filterDigits") {
    filterDigits("a1bc4".toList) shouldEqual List(1, 4)
  }

  test("checkAllDigits") {
    checkAllDigits("1234".toList) shouldEqual Some(List(1, 2, 3, 4))
    checkAllDigits("a1bc4".toList) shouldEqual None
  }

}
