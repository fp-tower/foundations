package exercises.actions.imperative

import java.time.{Instant, LocalDate}

import exercises.actions.TimeInstances
import exercises.actions.imperative.UserCreationExercises._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.collection.mutable.ListBuffer
import scala.util.Try

class UserCreationExercisesTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks with TimeInstances {

  ignore("readSubscribeToMailingList example") {
    val inputs  = ListBuffer("N")
    val outputs = ListBuffer.empty[String]
    val console = Console.mock(inputs, outputs)
    val result  = readSubscribeToMailingList(console)

    assert(result == false)
    assert(inputs.isEmpty) // "N" has been consumed
    assert(outputs.toList == List("Would you like to subscribe to our mailing list? [Y/N]"))
  }

  ignore("readDateOfBirth example") {
    val console = Console.mock(ListBuffer("21-07-1986"), ListBuffer())
    val result  = readDateOfBirth(console)

    assert(result == LocalDate.of(1986, 7, 21))
  }

  ignore("readUser example") {
    val inputs  = ListBuffer("Eda", "18-03-2001", "Y")
    val outputs = ListBuffer.empty[String]
    val console = Console.mock(inputs, outputs)
    val result  = readUser(console)

    val expected = User(
      name = "Eda",
      dateOfBirth = LocalDate.of(2001, 3, 18),
//      subscribedToMailingList = true,
      createdAt = Instant.now()
    )

    assert(result == expected)
  }

  ignore("readSubscribeToMailingListRetry example") {
    val outputs = ListBuffer.empty[String]
    val console = Console.mock(ListBuffer("No", "N"), outputs)
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

  ignore("readDateOfBirthRetry example") {
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

}
