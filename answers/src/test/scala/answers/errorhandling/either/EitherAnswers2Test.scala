package answers.errorhandling.either

import answers.errorhandling.either.EitherAnswers2.Country._
import answers.errorhandling.either.EitherAnswers2.FormError._
import answers.errorhandling.either.EitherAnswers2._
import org.scalacheck.Gen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class EitherAnswers2Test extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("validateCountry example") {
    assert(validateCountry("FRA") == Right(France))
    assert(validateCountry("UK") == Left(InvalidFormat("UK")))
    assert(validateCountry("ARG") == Left(NotSupported("ARG")))
  }

  test("validateCountry round trip") {
    forAll(Gen.oneOf(Country.all)) { country =>
      assert(validateCountry(country.code) == Right(country))
    }
  }

  test("checkUsernameSize example") {
    assert(checkUsernameSize("bob_2167") == Right(()))
    assert(checkUsernameSize("bob_2") == Right(()))
    assert(checkUsernameSize("bo") == Left(TooSmall(2)))
  }

  test("checkUsernameCharacters example") {
    assert(checkUsernameCharacters("_abc-123_") == Right(()))
    assert(checkUsernameCharacters("foo!~23}AD") == Left(InvalidCharacters(List('!', '~', '}'))))
  }

  test("checkUsernameCharacters PBT") {
    forAll((text: String) =>
      checkUsernameCharacters(text) match {
        case Left(InvalidCharacters(chars)) => chars.foreach(c => assert(text.contains(c)))
        case Right(_)                       => assert(text.forall(isValidUsernameCharacter))
      }
    )
  }

  test("validateUser example") {
    assert(validateUser("bob_2167", "FRA") == Right(User(Username("bob_2167"), France)))
    assert(validateUser("bob_2167", "UK") == Left(InvalidFormat("UK")))
    assert(validateUser("bo", "FRA") == Left(TooSmall(2)))
    assert(validateUser("bo", "UK") == Left(TooSmall(2)))
  }

}
