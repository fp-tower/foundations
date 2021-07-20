package exercises.errorhandling.either
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import EitherExercises2._
import exercises.errorhandling.either.EitherExercises2.Country._
import exercises.errorhandling.either.EitherExercises2.CountryError._
import exercises.errorhandling.either.EitherExercises2.UsernameError.{InvalidCharacters, TooSmall}

class EitherExercises2Test extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  ignore("validateCountry example") {
    assert(validateCountry("FRA") == Right(France))
    assert(validateCountry("UK") == Left(InvalidFormat("UK")))
    assert(validateCountry("ARG") == Left(NotSupported("ARG")))
  }

  ignore("checkUsernameSize example") {
    assert(checkUsernameSize("bob_2167") == Right(()))
    assert(checkUsernameSize("bob_2") == Right(()))
    assert(checkUsernameSize("bo") == Left(TooSmall(2)))
  }

  ignore("checkUsernameCharacters example") {
    assert(checkUsernameCharacters("_abc-123_") == Right(()))
    assert(checkUsernameCharacters("foo!~23}AD") == Left(InvalidCharacters(List('!', '~', '}'))))
  }

  ignore("validateUsername example") {
    assert(validateUsername("bob_2167") == Right(Username("bob_2167")))
    assert(validateUsername("bo") == Left(TooSmall(2)))
    assert(validateUsername("foo!~23}AD") == Left(InvalidCharacters(List('!', '~', '}'))))
  }

  test("validateUser example") {}

}
