package answers.errorhandling.validation

import answers.errorhandling.NEL
import answers.errorhandling.validation.ValidationAnswers._
import answers.errorhandling.validation.ValidationAnswers.FormError._
import answers.errorhandling.validation.ValidationAnswers.Country._
import answers.errorhandling.validation.ValidationAnswers.FieldIds._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class ValidationAnswersTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("validateCountry example") {
    assert(validateCountry("FRA") == France.valid)
    assert(validateCountry("UK") == InvalidFormat("UK").invalid)
    assert(validateCountry("ARG") == NotSupported("ARG").invalid)
  }

  test("validateUsername example") {
    assert(validateUsername("bob_2167") == Username("bob_2167").valid)
    assert(validateUsername("bo") == TooSmall(2).invalid)
    assert(validateUsername("foo!~23}AD") == InvalidCharacters(List('!', '~', '}')).invalid)
    assert(validateUsername("!") == NEL(TooSmall(1), InvalidCharacters(List('!'))).invalid)
  }

  test("validateUser example") {
    assert(validateUser("bob_2167", "FRA") == User(Username("bob_2167"), France).valid)
    assert(validateUser("bob_2167", "UK") == FieldError(countryOfResidence, NEL(InvalidFormat("UK"))).invalid)
    assert(validateUser("bo", "FRA") == FieldError(username, NEL(TooSmall(2))).invalid)
    assert(
      validateUser("b!", "UK") == NEL(
        FieldError(username, NEL(TooSmall(2), InvalidCharacters(List('!')))),
        FieldError(countryOfResidence, NEL(InvalidFormat("UK")))
      ).invalid
    )
  }

}
