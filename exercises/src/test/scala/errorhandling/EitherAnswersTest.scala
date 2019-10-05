package errorhandling

import java.time.{Duration, LocalDate, ZoneOffset}

import answers.errorhandling.EitherAnswers.CountryError.InvalidFormat
import answers.errorhandling.EitherAnswers.UserEmailError.{EmailNotFound, UserNotFound}
import answers.errorhandling.EitherAnswers.UsernameError.{InvalidCharacters, TooSmall}
import answers.errorhandling.EitherAnswers._
import answers.errorhandling.OptionAnswers
import org.scalatest.Matchers
import org.scalatest.funsuite.AnyFunSuite

class EitherAnswersTest extends AnyFunSuite with Matchers {

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

    getUserEmail(UserId(123), userMap) == Right("e@y.com")
    getUserEmail(UserId(444), userMap) == Left(UserNotFound(UserId(444)))
    getUserEmail(UserId(111), userMap) == Left(EmailNotFound(UserId(111)))
  }

  test("checkout") {
    val item      = Item("xxx", 2, 12.34)
    val baseOrder = Order("123", "Draft", List(item), None, None, None)

    checkout(baseOrder) shouldEqual Right(baseOrder.copy(status = "Checkout"))
    checkout(baseOrder.copy(basket = Nil)) shouldEqual Left(OrderError.EmptyBasket)
    checkout(baseOrder.copy(status = "Delivered")) shouldEqual Left(OrderError.InvalidStatus("checkout", "Delivered"))
  }

  test("submit") {
    val item      = Item("xxx", 2, 12.34)
    val now       = LocalDate.of(2019, 6, 12).atStartOfDay.toInstant(ZoneOffset.UTC)
    val baseOrder = Order("123", "Checkout", List(item), Some("10 high street"), None, None)

    submit(baseOrder, now) shouldEqual Right(baseOrder.copy(status = "Submitted", submittedAt = Some(now)))
    submit(baseOrder.copy(deliveryAddress = None), now) shouldEqual Left(OrderError.MissingDeliveryAddress)
    submit(baseOrder.copy(status = "Draft"), now) shouldEqual Left(OrderError.InvalidStatus("submit", "Draft"))
  }

  test("deliver") {
    val item        = Item("xxx", 2, 12.34)
    val submittedAt = LocalDate.of(2019, 6, 12).atStartOfDay.toInstant(ZoneOffset.UTC)
    val now         = LocalDate.of(2019, 6, 15).atStartOfDay.toInstant(ZoneOffset.UTC)
    val baseOrder   = Order("123", "Submitted", List(item), Some("10 high street"), Some(submittedAt), None)

    deliver(baseOrder, now) shouldEqual Right(
      (baseOrder.copy(status = "Delivered", deliveredAt = Some(now)), Duration.ofDays(3))
    )
    deliver(baseOrder.copy(submittedAt = None), now) shouldEqual Left(OrderError.MissingSubmittedTimestamp)
    deliver(baseOrder.copy(status = "Draft"), now) shouldEqual Left(OrderError.InvalidStatus("deliver", "Draft"))
  }

  //////////////////////////////////
  // 2. Import code with Exception
  //////////////////////////////////

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

  test("validateUsernameSize") {
    validateUsernameSize("moreThan3Char") shouldEqual Right(())
    validateUsernameSize("foo") shouldEqual Right(())
    validateUsernameSize("fo") shouldEqual Left(TooSmall(2))
  }

  test("validateUsernameCharacters") {
    validateUsernameCharacters("abcABC123-_") shouldEqual Right(())
    validateUsernameCharacters("foo!~23}AD") shouldEqual Left(InvalidCharacters(List('!', '~', '}')))
  }

  test("validateUser") {
    validateUser("  foo ", "FRA") shouldEqual Right(User(Username("foo"), Country.France))
    validateUser("~a", "FRA") shouldEqual Left(TooSmall(2))
    validateUser("  foo ", "UK") shouldEqual Left(InvalidFormat("UK"))
    validateUser("~a", "UK") shouldEqual Left(TooSmall(2))
  }

  test("validateUserPar") {
    validateUserPar("  foo ", "FRA") shouldEqual Right(User(Username("foo"), Country.France))
    validateUserPar("~a", "FRA") shouldEqual Left(List(TooSmall(2), InvalidCharacters(List('~'))))
    validateUserPar("  foo ", "UK") shouldEqual Left(List(InvalidFormat("UK")))
    validateUserPar("~a", "UK") shouldEqual Left(
      List(TooSmall(2), InvalidCharacters(List('~')), CountryError.InvalidFormat("UK"))
    )
  }

}
