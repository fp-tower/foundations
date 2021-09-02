package answers.errorhandling.option

import answers.errorhandling.option.OptionAnswers._
import answers.errorhandling.option.OptionAnswers.Role._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class OptionAnswersTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("getAccountId example") {
    val accountId = AccountId(124)

    assert(Reader(accountId, true).getAccountId == Some(accountId))
    assert(Editor(accountId, "Comic Sans").getAccountId == Some(accountId))
    assert(Admin.getAccountId == None)
  }

  test("asEditor example") {
    val accountId = AccountId(124)

    assert(Reader(accountId, true).asEditor == None)
    assert(Editor(accountId, "Comic Sans").asEditor == Some(Editor(accountId, "Comic Sans")))
    assert(Admin.asEditor == None)
  }

  test("getUserEmail example") {
    val users = Map(
      UserId(222) -> User(UserId(222), "john", Admin, Some(Email("j@x.com"))),
      UserId(123) -> User(UserId(123), "elisa", Admin, Some(Email("e@y.com"))),
      UserId(444) -> User(UserId(444), "bob", Admin, None)
    )

    assert(getUserEmail(UserId(123), users) == Some(Email("e@y.com")))
    assert(getUserEmail(UserId(111), users) == None) // no user
    assert(getUserEmail(UserId(444), users) == None) // no email
  }

  test("getAccountIds example") {
    val users = List(
      User(UserId(111), "Eda", Editor(AccountId(555), "Comic Sans"), Some(Email("e@y.com"))),
      User(UserId(222), "Bob", Reader(AccountId(555), true), None),
      User(UserId(333), "Lea", Reader(AccountId(741), false), None),
      User(UserId(444), "Jo", Admin, Some(Email("admin@fp-tower.com")))
    )
    assert(getAccountIds(users) == List(AccountId(555), AccountId(741)))
  }

  test("checkAllEmails example success") {
    assert(
      checkAllEmails(
        List(
          User(UserId(111), "Eda", Editor(AccountId(555), "Comic Sans"), Some(Email("e@y.com"))),
          User(UserId(222), "Bob", Reader(AccountId(555), true), None),
          User(UserId(333), "Lea", Reader(AccountId(741), false), None),
          User(UserId(444), "Jo", Admin, Some(Email("admin@fp-tower.com")))
        )
      ) == None
    )
  }

  test("checkAllEmails example failure") {
    assert(
      checkAllEmails(
        List(
          User(UserId(111), "Eda", Editor(AccountId(555), "Comic Sans"), Some(Email("e@y.com"))),
          User(UserId(222), "Bob", Reader(AccountId(555), true), None),
          User(UserId(333), "Lea", Reader(AccountId(741), false), None),
          User(UserId(444), "Jo", Admin, Some(Email("admin@fp-tower.com")))
        )
      ) == None
    )
  }

  test("sequence example") {
    assert(sequence(List(Some(1), Some(2), Some(3))) == Some(List(1, 2, 3)))
    assert(sequence(List(Some(1), None, Some(3))) == None)
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
