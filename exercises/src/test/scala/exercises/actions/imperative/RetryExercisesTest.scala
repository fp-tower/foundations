package exercises.actions.imperative

import java.time.LocalDate

import exercises.actions.TimeInstances
import exercises.actions.imperative.RetryExercises._
import exercises.actions.imperative.UserCreationExercises.readSubscribeToMailingList
import org.scalacheck.Gen
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Success, Try}

class RetryExercisesTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks with TimeInstances {

  ignore("readSubscribeToMailingListRetry example success") {
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

  ignore("readSubscribeToMailingListRetry example failure") {
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

  ignore("readDateOfBirthRetry example success") {
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

  ignore("readDateOfBirthRetry example failure") {
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

  ignore("retry until action succeeds") {
    var counter = 0
    val result = retry(5) { () =>
      counter += 1
      require(counter >= 3, "Counter is too low")
      "Hello"
    }
    assert(result == "Hello")
    assert(counter == 3)
  }

  ignore("retry when action fails") {
    forAll { (error: Exception) =>
      var counter = 0
      val result = Try(retry(5)(() => {
        counter += 1
        throw error
      }))

      assert(result == Failure(error))
      assert(counter == 5)
    }
  }

  ignore("onError success") {
    var counter = 0
    val result  = onError(() => "Hello", _ => counter += 1)

    assert(result == "Hello")
    assert(counter == 0)
  }

  ignore("onError failure") {
    var counter = 0
    val result  = Try(onError(() => throw new Exception("Boom"), _ => counter += 1))

    assert(result.isFailure)
    assert(counter == 1)
  }

  ignore("onError failure rethrow the initial error") {
    val result = Try(onError(() => throw new Exception("Boom"), _ => throw new Exception("BadaBoom")))

    assert(result.isFailure)
    assert(result.failed.get.getMessage == "Boom")
  }

}
