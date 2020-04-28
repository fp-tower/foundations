package answers.function

import answers.function.ParametricFunctionAnswers._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class ParametricFunctionAnswersTest extends AnyFunSuite with Matchers with ScalaCheckDrivenPropertyChecks {

  ////////////////////
  // Exercise 1: Pair
  ////////////////////

  test("Pair#swap") {
    Pair(0, 1).swap shouldEqual Pair(1, 0)
  }

  test("Pair#map") {
    Pair(0, 1).map(identity) shouldEqual Pair(0, 1)
  }

  test("Pair#forAll") {
    Pair(2, 6).forAll(_ > 0) shouldEqual true
    Pair(2, 6).forAll(_ > 2) shouldEqual false
    Pair(2, 6).forAll(_ > 9) shouldEqual false
  }

  test("Pair#forAll consistent with List") {
    forAll { (x: Int, y: Int, predicate: Int => Boolean) =>
      Pair(x, y).forAll(predicate) shouldEqual List(x, y).forall(predicate)
    }
  }

  test("Pair#zipWith") {
    Pair(0, 1).zipWith(Pair(2, 3), (x: Int, y: Int) => x + y) shouldEqual Pair(2, 4)
  }

  ////////////////////////////
  // Exercise 2: Predicate
  ////////////////////////////

  test("Predicate && true") {
    forAll { (p: (Int => Boolean), x: Int) =>
      (Predicate(p) && Predicate.True)(x) shouldEqual p(x)
    }
  }

  test("Predicate && false") {
    forAll { (p: (Int => Boolean), x: Int) =>
      (Predicate(p) && Predicate.False)(x) shouldEqual false
    }
  }

  test("Predicate || true") {
    forAll { (p: (Int => Boolean), x: Int) =>
      (Predicate(p) || Predicate.True)(x) shouldEqual true
    }
  }

  test("Predicate || false") {
    forAll { (p: (Int => Boolean), x: Int) =>
      (Predicate(p) || Predicate.False)(x) shouldEqual p(x)
    }
  }

  test("Predicate flip") {
    forAll { (x: Int) =>
      Predicate.True.flip(x) shouldEqual false
    }
  }

  test("Predicate isLongerThan") {
    isLongerThan(5)("hello") shouldEqual true
    isLongerThan(5)("hey") shouldEqual false
  }

}
