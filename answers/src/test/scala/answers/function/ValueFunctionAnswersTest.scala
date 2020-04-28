package answers.function

import answers.function.ValueFunctionAnswers._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class ValueFunctionAnswersTest extends AnyFunSuite with Matchers with ScalaCheckDrivenPropertyChecks {

  /////////////////////////////////////////////////////
  // Exercise 1: String API with higher-order functions
  /////////////////////////////////////////////////////

  test("selectDigits") {
    selectDigits("123foo0-!Bar~+3") shouldEqual "12303"
    selectDigits("hello") shouldEqual ""
  }

  test("selectDigits length is smaller") {
    forAll { (text: String) =>
      assert(selectDigits(text).length <= text.length)
    }
  }

  test("selectDigits only returns numbers") {
    forAll { (text: String) =>
      assert(selectDigits(text).forall(_.isDigit))
    }
  }

  test("String filter result satisfies predicate") {
    forAll { (text: String, predicate: Char => Boolean) =>
      text.filter(predicate).forall(predicate)
    }
  }

  test("secret") {
    secret("abc123") shouldEqual "******"
  }

  test("secret same length") {
    forAll { (text: String) =>
      secret(text).length shouldEqual text.length
    }
  }

  test("isValidUsernameCharacter") {
    isValidUsernameCharacter('a') shouldEqual true
    isValidUsernameCharacter('A') shouldEqual true
    isValidUsernameCharacter('1') shouldEqual true
    isValidUsernameCharacter('-') shouldEqual true
    isValidUsernameCharacter('_') shouldEqual true
    isValidUsernameCharacter('~') shouldEqual false
    isValidUsernameCharacter('!') shouldEqual false
  }

  test("isValidUsername") {
    isValidUsername("john-doe") shouldEqual true
    isValidUsername("*john*") shouldEqual false
  }

  ///////////////////////
  // Exercise 2: Point3
  ///////////////////////

  test("isPositive") {
    Point3(2, 3, 9).isPositive shouldEqual true
    Point3(0, 0, 0).isPositive shouldEqual true
    Point3(0, -2, -1).isPositive shouldEqual false
  }

  test("isPositive max 0") {
    forAll { (x: Int, y: Int, z: Int) =>
      Point3(x.max(0), y.max(0), z.max(0)).isPositive shouldEqual true
    }
  }

  test("isEven") {
    Point3(2, 4, 8).isEven shouldEqual true
    Point3(0, -8, -2).isEven shouldEqual true
    Point3(3, -2, 0).isEven shouldEqual false
  }

  test("isEven * 2") {
    forAll { (x: Int, y: Int, z: Int) =>
      Point3(x * 2, y * 2, z * 2).isEven shouldEqual true
    }
  }

  test("forAll") {
    Point3(1, 1, 1).forAll(_ == 1) shouldEqual true
    Point3(1, 2, 5).forAll(_ == 1) shouldEqual false
  }

  test("forAll constant") {
    forAll { (x: Int, y: Int, z: Int, constant: Boolean) =>
      Point3(x, y, z).forAll(_ => constant) shouldEqual constant
    }
  }

  test("forAll consistent with List") {
    forAll { (x: Int, y: Int, z: Int, predicate: Int => Boolean) =>
      Point3(x, y, z).forAll(predicate) shouldEqual List(x, y, z).forall(predicate)
    }
  }

}
