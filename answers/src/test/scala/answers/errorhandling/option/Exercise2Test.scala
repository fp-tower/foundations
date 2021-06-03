package answers.errorhandling.option
import answers.errorhandling.option.Exercise2.AccountId
import answers.errorhandling.option.Exercise2.Role._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class Exercise2Test extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("getSingleAccountId example") {
    val accountId = AccountId(124)

    assert(Reader(accountId, true).getSingleAccountId == Some(accountId))
    assert(Editor(accountId, "Comic Sans").getSingleAccountId == Some(accountId))
    assert(Admin.getSingleAccountId == None)
  }

  test("asEditor example") {
    val accountId = AccountId(124)

    assert(Reader(accountId, true).asEditor == None)
    assert(Editor(accountId, "Comic Sans").asEditor == Some(Editor(accountId, "Comic Sans")))
    assert(Admin.asEditor == None)
  }
}
