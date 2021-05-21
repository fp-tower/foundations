package answers.action.imperative

import java.time.{Instant, LocalDate}

import answers.action.DateGenerator._
import answers.action.UserCreationGenerator._
import answers.action.imperative.UserCreationAnswers._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.collection.mutable.ListBuffer
import scala.util.{Success, Try}

class UserCreationAnswersTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("parseYesNo") {
    assert(parseYesNo("Y") == true)
    assert(parseYesNo("N") == false)

    forAll(invalidYesNoGen) { (line: String) =>
      assert(Try(parseYesNo(line)).isFailure)
    }
  }

  test("readSubscribeToMailingList example") {
    val inputs  = ListBuffer("N")
    val outputs = ListBuffer.empty[String]
    val console = Console.mock(inputs, outputs)
    val result  = readSubscribeToMailingList(console)

    assert(result == false)
    assert(inputs.isEmpty) // "N" has been consumed
    assert(outputs.toList == List("Would you like to subscribe to our mailing list? [Y/N]"))
  }

  test("readSubscribeToMailingList") {
    forAll { (yesNo: Boolean) =>
      val inputs  = ListBuffer(formatYesNo(yesNo))
      val outputs = ListBuffer.empty[String]
      val console = Console.mock(inputs, outputs)

      val result = readSubscribeToMailingList(console)

      assert(result == yesNo)
    }
  }

  test("readSubscribeToMailingList invalid input") {
    forAll(invalidYesNoGen) { input =>
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
    forAll(dateGen) { (date: LocalDate) =>
      val input   = dateOfBirthFormatter.format(date)
      val inputs  = ListBuffer(input)
      val outputs = ListBuffer.empty[String]
      val console = Console.mock(inputs, outputs)

      val result = readDateOfBirth(console)

      assert(result == date)
    }
  }

  test("readDateOfBirth invalid input") {
    forAll(invalidDateGen) { input =>
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

  test("readUser pbt") {
    forAll { (name: String, dob: LocalDate, yesNo: Boolean, now: Instant) =>
      val inputs = ListBuffer(
        name,
        dateOfBirthFormatter.format(dob),
        formatYesNo(yesNo)
      )
      val outputs = ListBuffer.empty[String]
      val console = Console.mock(inputs, outputs)
      val clock   = Clock.constant(now)
      val result  = readUser(console, clock)

      val expected = User(name, dob, yesNo, now)

      assert(result == expected)
    }
  }

  test("readSubscribeToMailingListRetry example success") {
    val outputs = ListBuffer.empty[String]
    val console = Console.mock(ListBuffer("Never", "N"), outputs)
    val result  = readSubscribeToMailingListRetry(console, maxAttempt = 2)

    assert(result == false)
    assert(
      outputs.toList == List(
        """Would you like to subscribe to our mailing list? [Y/N]""",
        """Incorrect format, enter "Y" for Yes or "N" for "No"""",
        """Would you like to subscribe to our mailing list? [Y/N]"""
      )
    )
  }

  test("readSubscribeToMailingListRetry example failure") {
    val outputs = ListBuffer.empty[String]
    val console = Console.mock(ListBuffer("Never"), outputs)
    val result  = Try(readSubscribeToMailingListRetry(console, maxAttempt = 1))

    assert(result.isFailure)
    assert(
      outputs.toList == List(
        """Would you like to subscribe to our mailing list? [Y/N]""",
        """Incorrect format, enter "Y" for Yes or "N" for "No""""
      )
    )

    val console2 = Console.mock(ListBuffer("Never"), ListBuffer.empty[String])
    val result2  = Try(readSubscribeToMailingList(console2))
    assert(result.failed.get.getMessage == result2.failed.get.getMessage)
  }

  test("readDateOfBirthRetry example success") {
    val outputs = ListBuffer.empty[String]
    val console = Console.mock(ListBuffer("July 21st 1986", "21-07-1986"), outputs)
    val result  = readDateOfBirthRetry(console, maxAttempt = 2)

    assert(result == LocalDate.of(1986, 7, 21))
    assert(
      outputs.toList == List(
        """What's your date of birth? [dd-mm-yyyy]""",
        """Incorrect format, for example enter "18-03-2001" for 18th of March 2001""",
        """What's your date of birth? [dd-mm-yyyy]"""
      )
    )
  }

  test("readDateOfBirthRetry example failure") {
    val outputs        = ListBuffer.empty[String]
    val invalidAttempt = "July 21st 1986"
    val console        = Console.mock(ListBuffer(invalidAttempt), outputs)
    val result         = Try(readDateOfBirthRetry(console, maxAttempt = 1))

    assert(result.isFailure)
    assert(
      outputs.toList == List(
        """What's your date of birth? [dd-mm-yyyy]""",
        """Incorrect format, for example enter "18-03-2001" for 18th of March 2001"""
      )
    )
  }

  checkReadSubscribeToMailingList("readSubscribeToMailingListRetry", readSubscribeToMailingListRetry)
  checkReadSubscribeToMailingList("readSubscribeToMailingListRetryWhileLoop", readSubscribeToMailingListRetryWhileLoop)
  checkReadSubscribeToMailingList("readSubscribeToMailingListRetryV2", readSubscribeToMailingListRetryV2)

  checkReadDateOfBirth("readDateOfBirthRetry", readDateOfBirthRetry)
  checkReadDateOfBirth("readDateOfBirthRetryV2", readDateOfBirthRetryV2)

  def checkReadSubscribeToMailingList(
    name: String,
    impl: (Console, Int) => Boolean
  ): Unit = {
    test(s"$name retry") {
      forAll(validMaxAttemptGen, Gen.listOf(invalidYesNoGen), arbitrary[Boolean]) {
        (maxAttempt: Int, invalidInputs: List[String], bool: Boolean) =>
          val validInput = if (bool) "Y" else "N"
          val inputs     = ListBuffer.from(invalidInputs :+ validInput)
          val outputs    = ListBuffer.empty[String]
          val console    = Console.mock(inputs, outputs)
          val result     = Try(impl(console, maxAttempt))

          val pairOutput = List(
            "Would you like to subscribe to our mailing list? [Y/N]",
            s"""Incorrect format, enter "Y" for Yes or "N" for "No""""
          )

          val attempts    = (invalidInputs.size + 1).min(maxAttempt)
          val pairOutputs = List.fill(attempts)(pairOutput).flatten

          if (invalidInputs.size < maxAttempt) {
            assert(result == Success(bool))
            assert(outputs.toList == pairOutputs.dropRight(1))
          } else {
            assert(result.isFailure)
            assert(outputs.toList == pairOutputs)
          }
      }
    }

    test(s"$name if maxAttempt <= 0") {
      forAll(invalidMaxAttemptGen) { maxAttempt =>
        val console = Console.mock(ListBuffer.empty, ListBuffer.empty)
        val result  = Try(impl(console, maxAttempt))

        assert(result.isFailure)
      }
    }
  }

  def checkReadDateOfBirth(
    name: String,
    impl: (Console, Int) => LocalDate
  ): Unit = {
    test(s"$name retry") {
      forAll(validMaxAttemptGen, Gen.listOf(invalidDateGen), dateGen) {
        (maxAttempt: Int, invalidInputs: List[String], date: LocalDate) =>
          val dateStr = dateOfBirthFormatter.format(date)
          val inputs  = ListBuffer.from(invalidInputs :+ dateStr)
          val outputs = ListBuffer.empty[String]
          val console = Console.mock(inputs, outputs)

          val result = Try(impl(console, maxAttempt))

          if (invalidInputs.size < maxAttempt)
            assert(result == Success(date))
          else
            assert(result.isFailure)
      }
    }

    test(s"$name if maxAttempt <= 0") {
      forAll(invalidMaxAttemptGen) { maxAttempt =>
        val console = Console.mock(ListBuffer.empty, ListBuffer.empty)
        val result  = Try(impl(console, maxAttempt))

        assert(result.isFailure)
      }
    }
  }

}
