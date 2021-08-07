package exercises.errorhandling.either
import exercises.errorhandling.either.EitherExercises1.UserEmailError._
import exercises.errorhandling.either.EitherExercises1._
import exercises.errorhandling.option.OptionExercises.Role.Admin
import exercises.errorhandling.option.OptionExercises.{Email, User, UserId}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class EitherExercises1Test extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  ignore("getUserEmail example") {
    val users = Map(
      UserId(222) -> User(UserId(222), "john", Admin, Some(Email("j@x.com"))),
      UserId(123) -> User(UserId(123), "elisa", Admin, Some(Email("e@y.com"))),
      UserId(444) -> User(UserId(444), "bob", Admin, None)
    )

    assert(getUserEmail(UserId(123), users) == Right(Email("e@y.com")))
    assert(getUserEmail(UserId(111), users) == Left("User 111 is missing"))
    assert(getUserEmail(UserId(444), users) == Left("User 444 has no email address"))
  }

}
