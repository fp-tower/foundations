package answers.action.imperative

import java.time.LocalDate

import answers.action.UserCreationInstances
import answers.action.imperative.RetryAnswers._
import answers.action.imperative.UserCreationAnswers.{dateOfBirthFormatter, readSubscribeToMailingList}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.collection.mutable.ListBuffer
import scala.util.{Success, Try}

class RetryAnswersTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks with UserCreationInstances {

  test("readSubscribeToMailingListRetry example success") {
    val outputs = ListBuffer.empty[String]
    val console = Console.mock(ListBuffer("Never", "N"), outputs)
    val result  = readSubscribeToMailingListRetry(console, maxAttempt = 2)

    assert(result == false)
    assert(
      outputs.toList == List(
        """Would you like to subscribe to our mailing list? [Y/N]""",
        """Incorrect format, enter "Y" for Yes or "N" for "No"""",
        """Would you like to subscribe to our mailing list? [Y/N]""",
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
        """Incorrect format, enter "Y" for Yes or "N" for "No"""",
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
        """What's your date of birth? [dd-mm-yyyy]""",
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
        """Incorrect format, for example enter "18-03-2001" for 18th of March 2001""",
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
      forAll(invalidMaxAttemptGen) { (maxAttempt) =>
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
      forAll(invalidMaxAttemptGen) { (maxAttempt) =>
        val console = Console.mock(ListBuffer.empty, ListBuffer.empty)
        val result  = Try(impl(console, maxAttempt))

        assert(result.isFailure)
      }
    }
  }

}
