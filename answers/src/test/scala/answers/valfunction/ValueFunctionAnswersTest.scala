package answers.valfunction

import answers.valfunction.ValueFunctionAnswers._
import org.scalacheck.Gen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class ValueFunctionAnswersTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  /////////////////////////////////////////////////////
  // Exercise 1: String API with higher-order functions
  /////////////////////////////////////////////////////

  test("selectDigits") {
    assert(selectDigits("hello4world-80") == "480")
    assert(selectDigits("welcome") == "")
  }

  test("selectDigits length is smaller") {
    forAll { (text: String) =>
      assert(selectDigits(text).length <= text.length)
    }
  }

  test("selectDigits only returns numbers") {
    forAll { (text: String) =>
      selectDigits(text).foreach { char =>
        assert(char.isDigit)
      }
    }
  }

  test("String filter result satisfies predicate") {
    forAll { (text: String, predicate: Char => Boolean) =>
      text.filter(predicate).forall(predicate)
    }
  }

  test("secret") {
    assert(secret("abc123") == "******")
  }

  test("secret is idempotent") {
    forAll { (text: String) =>
      assert(secret(secret(text)) == secret(text))
    }
  }

  test("isValidUsernameCharacter") {
    assert(isValidUsernameCharacter('a'))
    assert(isValidUsernameCharacter('A'))
    assert(isValidUsernameCharacter('1'))
    assert(isValidUsernameCharacter('-'))
    assert(isValidUsernameCharacter('_'))
    assert(!isValidUsernameCharacter('~'))
    assert(!isValidUsernameCharacter('!'))
  }

  test("isValidUsername") {
    assert(isValidUsername("john-doe"))
    assert(!isValidUsername("*john*"))
  }

  test("if a username is case valid, so is its inverse") {
    forAll { (username: String) =>
      assert(isValidUsername(username.reverse) == isValidUsername(username))
    }
  }

  test("if two usernames are valid, then concatenating them form a valid username") {
    forAll { (username1: String, username2: String) =>
      val lhs = isValidUsername(username1 + username2)
      val rhs = isValidUsername(username1) && isValidUsername(username2)
      assert(lhs == rhs)
    }
  }

  ///////////////////////
  // Exercise 2: Point
  ///////////////////////

  test("isPositive") {
    assert(Point(2, 4, 9).isPositive)
    assert(Point(0, 0, 0).isPositive)
    assert(!Point(0, -2, 1).isPositive)
  }

  test("isPositive max 0") {
    forAll { (x: Int, y: Int, z: Int) =>
      assert(Point(x.max(0), y.max(0), z.max(0)).isPositive)
    }
  }

  test("isPositive with positive generator") {
    forAll(Gen.posNum[Int], Gen.posNum[Int], Gen.posNum[Int]) { (x: Int, y: Int, z: Int) =>
      assert(Point(x, y, z).isPositive)
    }
  }

  test("isEven") {
    assert(Point(2, 4, 8).isEven)
    assert(Point(0, -8, -2).isEven)
    assert(!Point(3, -2, 0).isEven)
  }

  test("isEven * 2") {
    forAll { (x: Int, y: Int, z: Int) =>
      assert(Point(x * 2, y * 2, z * 2).isEven)
    }
  }

  test("forAll") {
    assert(Point(1, 1, 1).forAll(_ == 1))
    assert(!Point(1, 2, 5).forAll(_ == 1))
  }

  test("forAll constant") {
    forAll { (x: Int, y: Int, z: Int, constant: Boolean) =>
      assert(Point(x, y, z).forAll(_ => constant) == constant)
    }
  }

  test("forAll consistent with List") {
    forAll { (x: Int, y: Int, z: Int, predicate: Int => Boolean) =>
      assert(Point(x, y, z).forAll(predicate) == List(x, y, z).forall(predicate))
    }
  }

}
