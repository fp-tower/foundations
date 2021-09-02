package answers.errorhandling.either

import answers.errorhandling.either.EitherAnswers1.UserEmailError.{EmailNotFound, UserNotFound}
import answers.errorhandling.either.EitherAnswers1._
import answers.errorhandling.option.OptionAnswers.Role.Admin
import answers.errorhandling.option.OptionAnswers.{Email, User, UserId}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class EitherAnswers1Test extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("getUserEmail example") {
    val users = Map(
      UserId(222) -> User(UserId(222), "john", Admin, Some(Email("j@x.com"))),
      UserId(123) -> User(UserId(123), "elisa", Admin, Some(Email("e@y.com"))),
      UserId(444) -> User(UserId(444), "bob", Admin, None)
    )

    assert(getUserEmail(UserId(123), users) == Right(Email("e@y.com")))
    assert(getUserEmail(UserId(111), users) == Left(UserNotFound(UserId(111))))
    assert(getUserEmail(UserId(444), users) == Left(EmailNotFound(UserId(444))))
  }
}
