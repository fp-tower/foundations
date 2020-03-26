package function

import exercises.function.FunctionExercises
import exercises.function.FunctionExercises._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class FunctionExercisesTest extends AnyFunSuite with Matchers with ScalaCheckDrivenPropertyChecks {

  test("selectDigits") {
    selectDigits("123foo0-!Bar~+3") shouldEqual "12303"
    selectDigits("hello") shouldEqual ""
  }

  test("secret") {
    secret("abc123") shouldEqual "******"
  }

}
