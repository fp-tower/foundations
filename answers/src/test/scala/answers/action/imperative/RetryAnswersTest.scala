package answers.action.imperative

import java.time.LocalDate

import answers.action.UserCreationInstances
import answers.action.imperative.RetryAnswers._
import answers.action.imperative.UserCreationAnswers.{dateOfBirthFormatter, readSubscribeToMailingList}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Success, Try}

class RetryAnswersTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks with UserCreationInstances {

  val thunkGen: Gen[() => Int] =
    Gen.oneOf(
      Arbitrary.arbitrary[Int].map(() => _),
      Gen.const(() => throw new Exception("Boom"))
    )

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
      forAll(validMaxAttempt, Gen.listOf(invalidYesNoGen), arbitrary[Boolean]) {
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
      forAll(validMaxAttempt, Gen.listOf(invalidDateGen), dateGen) {
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

  test("retry when block always succeeds") {
    var counter = 0
    val result = retry(1) {
      counter += 1
      2 + 2
    }
    assert(result == 4)
    assert(counter == 1)
  }

  test("retry when block always fails") {
    forAll { (error: Exception) =>
      var counter = 0
      def exec(): Int = {
        counter += 1
        throw error
      }
      val result = Try(retry(5)(exec()))

      assert(result == Failure(error))
      assert(counter == 5)
    }
  }

  test("retry when block fails and then succeeds") {
    var counter = 0
    def exec(): String = {
      counter += 1
      if (counter < 3) throw new Exception("Boom!")
      else "Hello"
    }
    val result = retry(5)(exec())

    assert(result == "Hello")
    assert(counter == 3)
  }

  test("retryWithError when block always succeeds") {
    var counter = 0
    val result = retryWithError(1)(
      action = 2 + 2,
      onError = _ => counter += 1
    )
    assert(result == 4)
    assert(counter == 0)
  }

  test("retryWithError when block always fails") {
    var counter = 0
    val result = Try {
      retryWithError(5)(
        action = throw new Exception("boom"),
        onError = _ => counter += 1
      )
    }

    assert(result.isFailure)
    assert(counter == 5)
  }

  test("retryWithError when block fails and then succeeds") {
    var counter = 0
    val result = retryWithError(5)(
      action = if (counter >= 3) "" else throw new Exception("boom"),
      onError = _ => counter += 1
    )

    assert(result == "")
    assert(counter == 3)
  }

  test("onError") {
    var counter = 0
    onError(() => 1, _ => counter += 1)

  }

  test("retryWithError is consistent with retry + onError") {
    forAll(Gen.listOf(thunkGen), validMaxAttempt) { (thunks, maxAttempt) =>
      val it1      = thunks.iterator
      var counter1 = 0
      val result1 = Try(
        retryWithError(maxAttempt)(
          action = it1.next().apply(),
          onError = _ => counter1 += 1
        )
      )

      val it2      = thunks.iterator
      var counter2 = 0
      val result2 = Try(
        retry(maxAttempt)(
          action = onError(
            action = it2.next().apply(),
            callback = _ => counter2 += 1
          )
        )
      )

      assert(result1.toEither.left.map(_.getMessage) == result2.toEither.left.map(_.getMessage))
      assert(counter1 == counter2)
    }
  }

}
