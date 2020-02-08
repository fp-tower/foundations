package function

import exercises.function.FunctionExercises
import exercises.function.FunctionExercises._
import org.scalatest.Matchers
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class FunctionExercisesTest extends AnyFunSuite with Matchers with ScalaCheckDrivenPropertyChecks {

  /////////////////////////////////////////////////////
  // 1. Functions as input (aka higher order functions)
  /////////////////////////////////////////////////////

  test("keepLetters") {
    keepLetters("123foo0-!Bar~+3") shouldEqual "fooBar"
  }

  test("secret") {
    secret("abc123") shouldEqual "******"
  }

  test("isValidUsernameCharacter") {}

  test("_isValidUsernameCharacter") {}

  test("isValidUsername") {}

  test("move") {}

  ////////////////////////////
  // 2. polymorphic functions
  ////////////////////////////

  test("identity") {
    identity(3) shouldEqual 3
    identity("foo") shouldEqual "foo"
  }

  test("const") {
    const("foo")(5) shouldEqual "foo"
    const(5)("foo") shouldEqual 5
    List(1, 2, 3).map(const(0)) shouldEqual List(0, 0, 0)
  }

  ///////////////////////////
  // 3. Recursion & Laziness
  ///////////////////////////

  test("sumList small") {
    sumList(List(1, 2, 3, 10)) shouldEqual 16
    sumList(Nil) shouldEqual 0
  }

  test("sumList large") {
    val xs = 1.to(1000000).toList

    sumList(xs) shouldEqual xs.sum
  }

}
