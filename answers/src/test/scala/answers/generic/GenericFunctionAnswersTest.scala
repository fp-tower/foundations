package answers.generic

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import answers.generic.GenericFunctionAnswers._
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.util.Try

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

  test("Predicate && examples") {
    assert((isEven && isPositive)(12))
    assert(!(isEven && isPositive)(11))
    assert(!(isEven && isPositive)(-4))
    assert(!(isEven && isPositive)(-7))
  }

  test("Predicate &&") {
    forAll { (p: (Int => Boolean), number: Int) =>
      val predicate = Predicate(p)
      assert(!(predicate && Predicate.False)(number))
      assert((predicate && Predicate.True)(number) == predicate(number))
    }
  }

  test("Predicate ||") {
    forAll { (p: (Int => Boolean), number: Int) =>
      val predicate = Predicate(p)
      assert((predicate || Predicate.True)(number))
      assert((predicate || Predicate.False)(number) == predicate(number))
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

  test("Predicate isValidUser") {
    assert(isValidUser(User("John", 20)))
    assert(!isValidUser(User("John", 17)))
    assert(!isValidUser(User("john", 20)))
    assert(!isValidUser(User("x", 23)))
  }

  ////////////////////////////
  // Exercise 3: JsonDecoder
  ////////////////////////////

  test("JsonDecoder UserId") {
    assert(userIdDecoder.decode("1234") == UserId(1234))
    assert(userIdDecoder.decode("-1") == UserId(-1))

    assert(Try(userIdDecoder.decode("hello")).isFailure)
    assert(Try(userIdDecoder.decode("1111111111111111")).isFailure)
  }

  test("JsonDecoder UserId round-trip") {
    forAll { (id: Int) =>
      assert(userIdDecoder.decode(id.toString) == UserId(id))
    }
  }

  test("JsonDecoder LocalDate") {
    assert(localDateDecoder.decode("\"2020-03-26\"") == LocalDate.of(2020, 3, 26))
    assert(Try(localDateDecoder.decode("2020-03-26")).isFailure)
    assert(Try(localDateDecoder.decode("hello")).isFailure)
  }

  test("JsonDecoder LocalDate round-trip (with Gen)") {
    forAll(localDateGen) { (localDate: LocalDate) =>
      val json = "\"" + DateTimeFormatter.ISO_LOCAL_DATE.format(localDate) + "\""
      assert(localDateDecoder.decode(json) == localDate)
    }
  }

  test("JsonDecoder LocalDate round-trip (with Arbitrary)") {
    forAll { (localDate: LocalDate) =>
      val json = "\"" + DateTimeFormatter.ISO_LOCAL_DATE.format(localDate) + "\""
      assert(localDateDecoder.decode(json) == localDate)
    }
  }

  test("JsonDecoder orElse") {
    forAll { (json: String, number: Int, exception: Exception) =>
      val decoder1 = JsonDecoder.constant(number) orElse JsonDecoder.fail(exception)
      val decoder2 = JsonDecoder.fail(exception) orElse JsonDecoder.constant(number)

      assert(decoder1.decode(json) == number)
      assert(decoder2.decode(json) == number)
    }
  }

  test("JsonDecoder weirdLocalDateDecoder") {
    val date = LocalDate.of(2020, 3, 26)
    assert(weirdLocalDateDecoder.decode("\"2020-03-26\"") == date)
    assert(weirdLocalDateDecoder.decode("18347") == date)
    assert(Try(weirdLocalDateDecoder.decode("hello")).isFailure)
  }

  test("JsonDecoder Option") {
    assert(optionDecoder(stringDecoder).decode("null") == None)
    assert(optionDecoder(stringDecoder).decode("\"hello\"") == Some("hello"))
  }

  test("SafeJsonDecoder Int") {
    assert(SafeJsonDecoder.int.decode("1234") == Right(1234))
    assert(SafeJsonDecoder.int.decode("hello") == Left("Invalid JSON Int: hello"))
  }

  test("SafeJsonDecoder orElse") {
    val date = LocalDate.of(2020, 3, 26)
    assert(SafeJsonDecoder.localDate.decode("\"2020-03-26\"") == Right(date))
    assert(SafeJsonDecoder.localDate.decode("18347") == Right(date))
  }

  val localDateGen: Gen[LocalDate] =
    Gen
      .choose(LocalDate.MIN.toEpochDay, LocalDate.MAX.toEpochDay)
      .map(LocalDate.ofEpochDay)

  implicit val localDateArbitrary: Arbitrary[LocalDate] =
    Arbitrary(localDateGen)

}
