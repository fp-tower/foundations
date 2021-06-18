package exercises.errorhandling.either
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import EitherExercises2._
import exercises.errorhandling.either.EitherExercises2.Country._
import exercises.errorhandling.either.EitherExercises2.CountryError._
import exercises.errorhandling.either.EitherExercises2.UsernameError.{InvalidCharacters, TooSmall}

class EitherExercises2Test extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("validateCountry example") {
    assert(validateCountry("FRA") == Right(France))
    assert(validateCountry("UK") == Left(InvalidFormat("UK")))
    assert(validateCountry("ARG") == Left(NotSupported("ARG")))
  }

  test("checkUsernameSize example") {
    assert(checkUsernameSize("bob_2167") == Right(()))
    assert(checkUsernameSize("bob") == Right(()))
    assert(checkUsernameSize("bo") == Left(TooSmall(2)))
  }

  test("checkUsernameCharacters example") {
    assert(checkUsernameCharacters("_abc-123_") == Right(()))
    assert(checkUsernameCharacters("foo!~23}AD") == Left(InvalidCharacters(List('!', '~', '}'))))
  }

  test("validateUsername example") {
    assert(validateUsername("bob_2167") == Right(Username("bob_2167")))
    assert(validateUsername("bo") == Left(TooSmall(2)))
    assert(validateUsername("foo!~23}AD") == Left(InvalidCharacters(List('!', '~', '}'))))
  }

  test("validateUser example") {
    assert(validateUser("bob_2167", "FRA") == Right(User(Username("bob_2167"), France)))
    assert(validateUser("bob_2167", "UK") == Left(InvalidFormat("UK")))
    assert(validateUser("bo", "FRA") == Left(TooSmall(2)))
    assert(validateUser("bo", "UK") == Left(TooSmall(2)))
  }

}
