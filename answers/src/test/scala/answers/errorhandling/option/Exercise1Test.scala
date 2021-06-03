package answers.errorhandling.option
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import Exercise1._

class Exercise1Test extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("getUserEmail example") {
    val users = Map(
      UserId(222) -> User(UserId(222), "john", Some(Email("j@x.com"))),
      UserId(123) -> User(UserId(123), "elisa", Some(Email("e@y.com"))),
      UserId(444) -> User(UserId(444), "bob", None)
    )

    assert(getUserEmail(UserId(123), users) == Some(Email("e@y.com")))
    assert(getUserEmail(UserId(111), users) == None) // no user
    assert(getUserEmail(UserId(444), users) == None) // no email
  }
}
