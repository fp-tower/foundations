package exercises.errorhandling.option

import exercises.errorhandling.option.OptionExercises.Role._
import exercises.errorhandling.option.OptionExercises._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class OptionExercisesTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  ignore("getAccountId example") {
    val accountId = AccountId(124)

    assert(getAccountId(Reader(accountId, true)) == Some(accountId))
    assert(getAccountId(Editor(accountId, "Comic Sans")) == Some(accountId))
    assert(getAccountId(Admin) == None)
  }

  ignore("getUserEmail example") {
    val users = Map(
      UserId(222) -> User(UserId(222), "john", Admin, Some(Email("j@x.com"))),
      UserId(123) -> User(UserId(123), "elisa", Admin, Some(Email("e@y.com"))),
      UserId(444) -> User(UserId(444), "bob", Admin, None)
    )

    assert(getUserEmail(UserId(123), users) == Some(Email("e@y.com")))
    assert(getUserEmail(UserId(111), users) == None) // no user
    assert(getUserEmail(UserId(444), users) == None) // no email
  }

  ignore("getAccountIds example") {
    val users = List(
      User(UserId(111), "Eda", Editor(AccountId(555), "Comic Sans"), Some(Email("e@y.com"))),
      User(UserId(222), "Bob", Reader(AccountId(555), true), None),
      User(UserId(333), "Lea", Reader(AccountId(741), false), None),
      User(UserId(444), "Jo", Admin, Some(Email("admin@fp-tower.com")))
    )
    assert(getAccountIds(users) == List(AccountId(555), AccountId(741)))
  }

  ignore("checkAllEmails example success") {
    assert(
      checkAllEmails(
        List(
          User(UserId(111), "Eda", Editor(AccountId(555), "Comic Sans"), Some(Email("e@y.com"))),
          User(UserId(444), "Jo", Admin, Some(Email("admin@fp-tower.com")))
        )
      ) == Some(List(Email("e@y.com"), Email("admin@fp-tower.com")))
    )
  }

  ignore("checkAllEmails example failure") {
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

  ignore("sequence example") {
    assert(sequence(List(Some(1), Some(2), Some(3))) == Some(List(1, 2, 3)))
    assert(sequence(List(Some(1), None, Some(3))) == None)
  }

  ignore("asEditor example") {
    val accountId = AccountId(124)

    assert(asEditor(Reader(accountId, true)) == None)
    assert(asEditor(Editor(accountId, "Comic Sans")) == Some(Editor(accountId, "Comic Sans")))
    assert(asEditor(Admin) == None)
  }
}
