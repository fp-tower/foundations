package answers.function

import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDate, ZoneOffset}

import answers.function.GenericFunctionAnswers._
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class GenericFunctionAnswersTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  ////////////////////
  // Exercise 1: Pair
  ////////////////////

  test("Pair swap") {
    assert(Pair(0, 1).swap == Pair(1, 0))
  }

  test("Pair map") {
    assert(Pair(0, 1).map(identity) == Pair(0, 1))
  }

  test("Pair zipWith") {
    assert(Pair(0, 1).zipWith(Pair(2, 3))(_ + _) == Pair(2, 4))
  }

  test("Pair decoded") {
    assert(decoded == Pair("Functional", "Programming"))
  }

  test("Pair productNames") {
    assert(products == Pair(Product("Coffee", 2.5), Product("Plane ticket", 329.99)))
  }

  ////////////////////////////
  // Exercise 2: Predicate
  ////////////////////////////

  test("Predicate &&") {
    forAll { (p: (Int => Boolean), x: Int) =>
      assert((Predicate(p) && Predicate.True)(x) == p(x))
      assert(!(Predicate(p) && Predicate.False)(x))
    }
  }

  test("Predicate ||") {
    forAll { (p: (Int => Boolean), x: Int) =>
      assert((Predicate(p) || Predicate.True)(x))
      assert((Predicate(p) || Predicate.False)(x) == p(x))
    }
  }

  test("Predicate flip") {
    assert(!Predicate.True.flip(()))
    assert(Predicate.False.flip(()))
  }

  test("Predicate isLongerThan") {
    assert(isLongerThan(5)("hello"))
    assert(!isLongerThan(5)("hey"))
  }

  test("Predicate isLongerThan take") {
    forAll { (word: String, n: Int, min: Int) =>
      if (isLongerThan(min)(word.drop(n))) {
        assert(isLongerThan(min)(word))
      } else succeed
    }
  }

  test("Predicate contains") {
    assert(contains('l')("hello"))
    assert(!contains('z')("hello"))
  }

  test("Predicate contains filter") {
    forAll { (word: String, char: Char) =>
      assert(!contains(char)(word.filterNot(_ == char)))
    }
  }

  test("Predicate isValidUser") {
    assert(isValidUser(User("john", 18)))
    assert(!isValidUser(User("john", 17)))
    assert(!isValidUser(User("x", 23)))
  }

  ////////////////////////////
  // Exercise 3: JsonDecoder
  ////////////////////////////

  test("JsonDecoder UserId") {
    assert(userIdDecoder.decode("1234") == UserId(1234))
  }

  test("JsonDecoder UserId int.toString") {
    forAll { (id: Int) =>
      assert(userIdDecoder.decode(id.toString) == UserId(id))
    }
  }

  test("JsonDecoder LocalDate") {
    assert(localDateDecoder.decode("\"2020-03-26\"") == LocalDate.of(2020, 3, 26))
  }

  test("JsonDecoder LocalDate random") {
    forAll { (localDate: LocalDate) =>
      val json = "\"" + DateTimeFormatter.ISO_LOCAL_DATE.format(localDate) + "\""
      assert(localDateDecoder.decode(json) == localDate)
    }
  }

  test("JsonDecoder Option") {
    assert(optionDecoder(stringDecoder).decode("null") == None)
    assert(optionDecoder(stringDecoder).decode("\"hello\"") == Some("hello"))
  }

  implicit val localDateArbitrary: Arbitrary[LocalDate] =
    Arbitrary(
      Gen
        .choose(Instant.MIN.getEpochSecond, Instant.MAX.getEpochSecond)
        .map(Instant.ofEpochSecond)
        .map(_.atZone(ZoneOffset.UTC).toLocalDate)
    )

}
