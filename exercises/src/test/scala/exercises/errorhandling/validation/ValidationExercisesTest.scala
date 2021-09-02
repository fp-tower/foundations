package exercises.errorhandling.validation

import exercises.errorhandling.NEL
import exercises.errorhandling.validation.ValidationExercises.Country._
import exercises.errorhandling.validation.ValidationExercises.FormError._
import exercises.errorhandling.validation.ValidationExercises._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class ValidationExercisesTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  ignore("validateCountry example") {
    assert(validateCountry("FRA") == France.valid)
    assert(validateCountry("UK") == InvalidFormat("UK").invalid)
    assert(validateCountry("ARG") == NotSupported("ARG").invalid)
  }

  ignore("checkUsernameSize example") {
    assert(checkUsernameSize("bob_2167") == ().valid)
    assert(checkUsernameSize("bob_2") == ().valid)
    assert(checkUsernameSize("bo") == TooSmall(2).invalid)
  }

  ignore("checkUsernameCharacters example") {
    assert(checkUsernameCharacters("_abc-123_") == ().valid)
    assert(checkUsernameCharacters("foo!~23}AD") == InvalidCharacters(List('!', '~', '}')).invalid)
  }

  ignore("validateUsername example") {
    assert(validateUsername("bob_2167") == Username("bob_2167").valid)
    assert(validateUsername("bo") == TooSmall(2).invalid)
    assert(validateUsername("foo!~23}AD") == InvalidCharacters(List('!', '~', '}')).invalid)
    assert(validateUsername("!") == NEL(TooSmall(1), InvalidCharacters(List('!'))).invalid)
  }

  ignore("validateUser example") {
    assert(validateUser("bob_2167", "FRA") == User(Username("bob_2167"), France).valid)
    assert(validateUser("bob_2167", "UK") == InvalidFormat("UK").invalid)
    assert(validateUser("bo", "FRA") == TooSmall(2).invalid)
    assert(validateUser("b!", "UK") == NEL(TooSmall(2), InvalidCharacters(List('!')), InvalidFormat("UK")).invalid)
  }

}
