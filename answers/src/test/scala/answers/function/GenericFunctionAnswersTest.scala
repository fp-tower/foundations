package answers.function

import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDate, ZoneOffset}

import answers.function.GenericFunctionAnswers._
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class GenericFunctionAnswersTest extends AnyFunSuite with Matchers with ScalaCheckDrivenPropertyChecks {

  ////////////////////
  // Exercise 1: Pair
  ////////////////////

  test("Pair swap") {
    Pair(0, 1).swap shouldEqual Pair(1, 0)
  }

  test("Pair map") {
    Pair(0, 1).map(identity) shouldEqual Pair(0, 1)
  }

  test("Pair zipWith") {
    Pair(0, 1).zipWith(Pair(2, 3))(_ + _) shouldEqual Pair(2, 4)
  }

  test("Pair decoded") {
    decoded shouldEqual Pair("Functional", "Programming")
  }

  test("Pair productNames") {
    productNames shouldEqual Pair(Product("Coffee", 2.5), Product("Plane ticket", 329.99))
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
    localDateDecoder.decode("\"2020-03-26\"") shouldEqual LocalDate.of(2020, 3, 26)
  }

  test("JsonDecoder LocalDate random") {
    forAll { (localDate: LocalDate) =>
      val json = "\"" + DateTimeFormatter.ISO_LOCAL_DATE.format(localDate) + "\""
      localDateDecoder.decode(json) shouldEqual localDate
    }
  }

  test("JsonDecoder Option") {
    optionDecoder(stringDecoder).decode("null") shouldEqual None
    optionDecoder(stringDecoder).decode("\"hello\"") shouldEqual Some("hello")
  }

  implicit val localDateArbitrary: Arbitrary[LocalDate] =
    Arbitrary(
      Gen
        .choose(Instant.MIN.getEpochSecond, Instant.MAX.getEpochSecond)
        .map(Instant.ofEpochSecond)
        .map(_.atZone(ZoneOffset.UTC).toLocalDate)
    )

}
