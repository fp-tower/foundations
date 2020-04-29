package answers.function

import java.time.LocalDate

import answers.function.ParametricFunctionAnswers._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class ParametricFunctionAnswersTest extends AnyFunSuite with Matchers with ScalaCheckDrivenPropertyChecks {

  ////////////////////
  // Exercise 1: Pair
  ////////////////////

  test("Pair swap") {
    Pair(0, 1).swap shouldEqual Pair(1, 0)
  }

  test("Pair map") {
    Pair(0, 1).map(identity) shouldEqual Pair(0, 1)
  }

  test("Pair forAll") {
    Pair(2, 6).forAll(_ > 0) shouldEqual true
    Pair(2, 6).forAll(_ > 2) shouldEqual false
    Pair(2, 6).forAll(_ > 9) shouldEqual false
  }

  test("Pair forAll consistent with List") {
    forAll { (x: Int, y: Int, predicate: Int => Boolean) =>
      Pair(x, y).forAll(predicate) shouldEqual List(x, y).forall(predicate)
    }
  }

  test("Pair zipWith") {
    Pair(0, 1).zipWith(Pair(2, 3), (x: Int, y: Int) => x + y) shouldEqual Pair(2, 4)
  }

  ////////////////////////////
  // Exercise 2: Predicate
  ////////////////////////////

  test("Predicate &&") {
    forAll { (p: (Int => Boolean), x: Int) =>
      (Predicate(p) && Predicate.True)(x) shouldEqual p(x)
      (Predicate(p) && Predicate.False)(x) shouldEqual false
    }
  }

  test("Predicate ||") {
    forAll { (p: (Int => Boolean), x: Int) =>
      (Predicate(p) || Predicate.True)(x) shouldEqual true
      (Predicate(p) || Predicate.False)(x) shouldEqual p(x)
    }
  }

  test("Predicate flip") {
    Predicate.True.flip(()) shouldEqual false
    Predicate.False.flip(()) shouldEqual true
  }

  test("Predicate isLongerThan") {
    isLongerThan(5)("hello") shouldEqual true
    isLongerThan(5)("hey") shouldEqual false
  }

  test("Predicate isLongerThan take") {
    forAll { (word: String, n: Int, min: Int) =>
      if (isLongerThan(min)(word.drop(n))) {
        assert(isLongerThan(min)(word))
      } else succeed
    }
  }

  test("Predicate contains") {
    contains('l')("hello") shouldEqual true
    contains('z')("hello") shouldEqual false
  }

  test("Predicate contains filter") {
    forAll { (word: String, char: Char) =>
      contains(char)(word.filterNot(_ == char)) shouldEqual false
    }
  }

  test("Predicate isValidUser") {
    isValidUser(User("john", 18)) shouldEqual true
    isValidUser(User("john", 17)) shouldEqual false
    isValidUser(User("x", 23)) shouldEqual false
  }

  ////////////////////////////
  // Exercise 3: JsonDecoder
  ////////////////////////////

  test("JsonDecoder UserId") {
    userIdDecoder.decode("1234") shouldEqual UserId(1234)
  }

  test("JsonDecoder UserId int.toString") {
    forAll { (id: Int) =>
      userIdDecoder.decode(id.toString) shouldEqual UserId(id)
    }
  }

  test("JsonDecoder LocalDate") {
    localDateDecoder.decode("2020-03-26") shouldEqual LocalDate.of(2020, 3, 26)
  }

  test("JsonDecoder LocalDate random") {
    forAll { (year: Int, month: Int, dayOfMonth: Int) =>
      val normalisedYear       = year.max(0) % 1050 + 1000
      val normalisedMonth      = (month.max(0) % 11) + 1
      val normalisedDayOfMonth = (dayOfMonth.max(0) % 27) + 1
      val formattedMonth       = "%02d".format(normalisedMonth)
      val formattedDayOfMonth  = "%02d".format(normalisedDayOfMonth)

      localDateDecoder.decode(s"$normalisedYear-$formattedMonth-$formattedDayOfMonth") shouldEqual
        LocalDate.of(normalisedYear, normalisedMonth, normalisedDayOfMonth)
    }
  }

  test("JsonDecoder Option") {
    optionDecoder(intDecoder).decode("null") shouldEqual None
    optionDecoder(intDecoder).decode("1234") shouldEqual Some(1234)
  }
}
