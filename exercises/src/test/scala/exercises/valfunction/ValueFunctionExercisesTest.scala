package exercises.valfunction

import exercises.valfunction.ValueFunctionExercises._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class ValueFunctionExercisesTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  /////////////////////////////////////////////////////
  // Exercise 1: String API with higher-order functions
  /////////////////////////////////////////////////////

  // replace `ignore` by `test` to enable the test
  test("selectDigits examples") {
    assert(selectDigits("hello4world-80") == "480")
    assert(selectDigits("welcome") == "")
  }

  // replace `ignore` by `test` to enable the test
  test("selectDigits length is smaller") {
    forAll { (text: String) =>
      assert(selectDigits(text).length <= text.length)
    }
  }
  test("select digits lenght is smaller") {
    forAll { (text: String) =>
      selectDigits(text).foreach(c => assert(c.isDigit))
    }
  }

  test("secret examples") {
    assert(secret("Welcome123") == "**********")
  }

  test("secret length is equal") {
    forAll { (text: String) =>
      assert(secret(text).length == text.length)
    }
  }
  test("secret all characters are '*'") {
    forAll { (text: String) =>
      secret(text).foreach(c => assert(c == '*'))
    }
  }

  test("isValidUsernameCharacter examples") {
    assert(isValidUsernameCharacter('3') == true)
    assert(isValidUsernameCharacter('a') == true)
    assert(isValidUsernameCharacter('^') == false)
  }
  test("isValidUsernameCharacter all characters are valid") {
    forAll { (char: Char) =>
      assert(isValidUsernameCharacter(char) == (char.isLetter || char.isDigit || char == '-' || char == '_'))
    }
  }

  test("isValidUsername examples") {
    assert(isValidUsername("john-doe") == true)
    assert(isValidUsername("*john*") == false)
  }
  test("isValidUsername all characters are valid") {
    forAll { (username: String) =>
      assert(isValidUsername(username) == username.forall(isValidUsernameCharacter))
    }
  }
  test("isPositive examples") {
    assert(Point(2, 4, 9).isPositive == true)
    assert(Point(0, 0, 0).isPositive == true)
    assert(Point(0, -2, 1).isPositive == false)
  }

  test("isPositive all coordinates are positive") {
    forAll { (x: Int, y: Int, z: Int) =>
      assert(Point(x, y, z).isPositive == (x >= 0 && y >= 0 && z >= 0))
    }
  }
  test("isEven examples") {
    assert(Point(2, 4, 8).isEven == true)
    assert(Point(0, -8, -2).isEven == true)
    assert(Point(3, -2, 0).isEven == false)
  }

  test("forAll examples") {
    forAll { (x: Int, y: Int, z: Int, predicate : Int => Boolean) =>
      Point(x, y, z).forAll(predicate) == List(x,y,z).forall(predicate)
  }
  }

  ///////////////////////
  // Exercise 2: Point
  ///////////////////////

}
