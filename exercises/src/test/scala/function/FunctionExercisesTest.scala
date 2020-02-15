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

  //////////////////////////////////////////////////
  // 2. functions as output (aka curried functions)
  //////////////////////////////////////////////////

  test("increment") {
    increment(5) shouldEqual 6
  }

  ////////////////////////////
  // 3. parametric functions
  ////////////////////////////

  test("Pair#swap") {
    Pair("John", "Doe").swap shouldEqual Pair("Doe", "John")
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
