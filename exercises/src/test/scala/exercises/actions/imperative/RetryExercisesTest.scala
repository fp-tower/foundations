package exercises.actions.imperative

import java.time.LocalDate

import exercises.actions.imperative.RetryExercises._
import exercises.actions.imperative.UserCreationExercises.readSubscribeToMailingList
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Try}

class RetryExercisesTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

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

  ignore("retry when block always succeeds") {
    var counter = 0
    val result = retry(1) { () =>
      counter += 1
      2 + 2
    }
    assert(result == 4)
    assert(counter == 1)
  }

  ignore("retry when block always fails") {
    forAll { (error: Exception) =>
      var counter = 0
      def exec(): Int = {
        counter += 1
        throw error
      }
      val result = Try(retry(5)(exec))

      assert(result == Failure(error))
      assert(counter == 5)
    }
  }

}
