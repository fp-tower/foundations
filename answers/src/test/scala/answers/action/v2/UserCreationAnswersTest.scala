package answers.action.v2

import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDate}

import answers.action.v2.UserCreationAnswers._
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.collection.mutable.ListBuffer
import scala.util.Try

class UserCreationAnswersTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  val localDateGen: Gen[LocalDate] =
    Gen
      .choose(LocalDate.MIN.toEpochDay, LocalDate.MAX.toEpochDay)
      .map(LocalDate.ofEpochDay)

  val localDateFormatter: Gen[DateTimeFormatter] =
    Gen.oneOf(DateTimeFormatter.ISO_LOCAL_DATE, dateOfBirthFormatter)

  val invalidYesNoInput: Gen[String] =
    Gen.alphaNumStr.filterNot(Set("Y", "N"))

  val invalidDateInput: Gen[String] =
    Gen.alphaNumStr.suchThat(date => Try(dateOfBirthFormatter.parse(date)).isFailure)

  val invalidMaxAttempt: Gen[Int] =
    Gen.choose(Int.MinValue, 0)

  test("readSubscribeToMailingList example") {
    val console = Console.mock(ListBuffer("N"), ListBuffer())
    val result  = readSubscribeToMailingList(console)

    assert(result == false)
  }

  test("readSubscribeToMailingList") {
    forAll { (boolean: Boolean) =>
      val boolStr = if (boolean) "Y" else "N"
      val inputs  = ListBuffer(boolStr)
      val outputs = ListBuffer.empty[String]
      val console = Console.mock(inputs, outputs)

      val result = readSubscribeToMailingList(console)

      assert(result == boolean)
    }
  }

  test("readSubscribeToMailingList invalid input") {
    forAll(invalidYesNoInput) { input =>
      val inputs  = ListBuffer(input)
      val outputs = ListBuffer.empty[String]
      val console = Console.mock(inputs, outputs)

      val result = Try(readSubscribeToMailingList(console))

      assert(result.isFailure)
    }
  }

  test("readDateOfBirth example") {
    val console = Console.mock(ListBuffer("21-07-1986"), ListBuffer())
    val result  = readDateOfBirth(console)

    assert(result == LocalDate.of(1986, 7, 21))
  }

  test("readDateOfBirth") {
    forAll(localDateGen) { (date: LocalDate) =>
      val input   = dateOfBirthFormatter.format(date)
      val inputs  = ListBuffer(input)
      val outputs = ListBuffer.empty[String]
      val console = Console.mock(inputs, outputs)

      val result = readDateOfBirth(console)

      assert(result == date)
    }
  }

  test("readDateOfBirth invalid input") {
    forAll(invalidDateInput) { input =>
      val inputs  = ListBuffer(input)
      val outputs = ListBuffer.empty[String]
      val console = Console.mock(inputs, outputs)

      val result = Try(readDateOfBirth(console))

      assert(result.isFailure)
    }
  }

  test("readUser example") {
    val inputs  = ListBuffer("Eda", "18-03-2001", "Y")
    val outputs = ListBuffer.empty[String]
    val now     = Instant.ofEpochSecond(9999999)
    val console = Console.mock(inputs, outputs)
    val clock   = Clock.constant(now)
    val result  = readUser(console, clock)

    val expected = User(
      name = "Eda",
      dateOfBirth = LocalDate.of(2001, 3, 18),
      subscribedToMailingList = true,
      createdAt = now
    )

    assert(result == expected)
  }

  checkReadDateOfBirth("readDateOfBirth", readDateOfBirth)
  checkReadDateOfBirth("readDateOfBirthV2", readDateOfBirthV2)

  checkReadSubscribeToMailingList("readSubscribeToMailingList", readSubscribeToMailingList)
  checkReadSubscribeToMailingList("readSubscribeToMailingListV2", readSubscribeToMailingListV2)

  def checkReadDateOfBirth(
    name: String,
    impl: (Console, DateTimeFormatter, Int) => LocalDate
  ): Unit = {
    test(s"$name success") {
      forAll(localDateFormatter, localDateGen) { (formatter, date) =>
        val dateStr = formatter.format(date)
        val console = Console.mock(ListBuffer(dateStr), ListBuffer.empty)
        val result  = impl(console, formatter, 1)

        assert(result == date)
      }
    }

    test(s"$name retry") {
      forAll(localDateFormatter, localDateGen, Gen.listOf(invalidDateInput)) { (formatter, date, attempts) =>
        val dateStr    = formatter.format(date)
        val inputs     = ListBuffer.from(attempts :+ dateStr)
        val outputs    = ListBuffer.empty[String]
        val console    = Console.mock(inputs, outputs)
        val maxAttempt = attempts.size + 1

        val result = impl(console, formatter, maxAttempt)

        assert(result == date)

        val pairOutput = List(
          "What's your date of birth? [dd-mm-yyyy]",
          """Incorrect format, for example enter "18-03-2001" for 18th of March 2001"""
        )

        val expectedOutputs = 1.to(maxAttempt).flatMap(_ => pairOutput).toList.dropRight(1)
        assert(outputs.toList == expectedOutputs)
      }
    }

    test(s"$name if maxAttempt <= 0") {
      forAll(localDateFormatter, invalidMaxAttempt) { (formatter, maxAttempt) =>
        val console = Console.mock(ListBuffer.empty, ListBuffer.empty)
        val result  = Try(impl(console, formatter, maxAttempt))

        assert(result.isFailure)
      }
    }
  }

  def checkReadSubscribeToMailingList(
    name: String,
    impl: (Console, Int) => Boolean
  ): Unit = {
    test(s"$name retry") {
      forAll(Arbitrary.arbitrary[Boolean], Gen.listOf(invalidYesNoInput)) { (bool, attempts) =>
        val boolStr    = if (bool) "Y" else "N"
        val inputs     = ListBuffer.from(attempts :+ boolStr)
        val outputs    = ListBuffer.empty[String]
        val console    = Console.mock(inputs, outputs)
        val maxAttempt = attempts.size + 1

        val result = impl(console, maxAttempt)

        assert(result == bool)

        val pairOutput = List(
          "Would you like to subscribe to our mailing list? [Y/N]",
          s"""Incorrect format, enter "Y" for Yes or "N" for "No"""""
        )

        val expectedOutputs = 1.to(maxAttempt).flatMap(_ => pairOutput).toList.dropRight(1)
        assert(outputs.toList == expectedOutputs)
      }
    }

    test(s"$name if maxAttempt <= 0") {
      forAll(invalidMaxAttempt) { (maxAttempt) =>
        val console = Console.mock(ListBuffer.empty, ListBuffer.empty)
        val result  = Try(impl(console, maxAttempt))

        assert(result.isFailure)
      }
    }
  }

  test("readUser") {
    val inputs  = ListBuffer("Bob", "12-03-1997", "Y")
    val now     = Instant.now()
    val console = Console.mock(inputs, ListBuffer.empty)
    val clock   = Clock.constant(now)

    val user = readUser(
      console,
      clock,
      readDateOfBirthV2(_, dateOfBirthFormatter, 2),
      readSubscribeToMailingListV2(_, 2)
    )

    val expectedUser = User(
      name = "Bob",
      dateOfBirth = LocalDate.of(1997, 3, 12),
      subscribedToMailingList = true,
      createdAt = now
    )

    assert(user == expectedUser)
  }

}
