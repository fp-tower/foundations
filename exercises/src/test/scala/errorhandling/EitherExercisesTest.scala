package errorhandling

import java.time.{Duration, LocalDate, ZoneOffset}
import java.util.UUID

import answers.errorhandling.EitherAnswers.CountryError.InvalidFormat
import answers.errorhandling.EitherAnswers.UserEmailError.{EmailNotFound, UserNotFound}
import answers.errorhandling.EitherAnswers.UsernameError.{InvalidCharacters, TooSmall}
import answers.errorhandling.EitherAnswers._
import answers.errorhandling.OptionAnswers
import org.scalatest.Matchers
import org.scalatest.funsuite.AnyFunSuite

class EitherExercisesTest extends AnyFunSuite with Matchers {

  ////////////////////////
  // 1. Use cases
  ////////////////////////

  test("getUserEmail") {
    import OptionAnswers.{Email, User, UserId}
    val userMap = Map(
      UserId(222) -> User(UserId(222), "john", Some(Email("j@x.com"))),
      UserId(123) -> User(UserId(123), "elisa", Some(Email("e@y.com"))),
      UserId(444) -> User(UserId(444), "bob", None)
    )

    getUserEmail(UserId(123), userMap) shouldEqual Right(Email("e@y.com"))
    getUserEmail(UserId(111), userMap) shouldEqual Left(UserNotFound(UserId(111)))
    getUserEmail(UserId(444), userMap) shouldEqual Left(EmailNotFound(UserId(444)))
  }

  test("checkout") {
    val item      = Item("xxx", 2, 12.34)
    val baseOrder = Order("123", "Draft", List(item), None, None, None)

    checkout(baseOrder) shouldEqual Right(baseOrder.copy(status = "Checkout"))
    checkout(baseOrder.copy(basket = Nil)) shouldEqual Left(OrderError.EmptyBasket)
    checkout(baseOrder.copy(status = "Delivered")) shouldEqual Left(OrderError.InvalidStatus("checkout", "Delivered"))
  }

  test("submit") {}

  test("deliver") {}

  //////////////////////////////////
  // 2. Import code with Exception
  //////////////////////////////////

  test("parseUUID") {}

  //////////////////////////////////
  // 3. Advanced API
  //////////////////////////////////

  test("validateUsername") {
    validateUsername("foo") shouldEqual Right(Username("foo"))
    validateUsername("  foo ") shouldEqual Right(Username("foo"))
    validateUsername("a!bc@£") shouldEqual Left(InvalidCharacters("!@£".toList))
    validateUsername(" yo") shouldEqual Left(TooSmall(2))
    validateUsername(" !") shouldEqual Left(TooSmall(1))
  }

  test("validateUsernameSize") {}

  test("validateUsernameCharacters") {}

  test("validateUser") {}

  test("validateUserPar") {}

  test("parSequence") {}

}
