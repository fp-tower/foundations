package answers.action.imperative

import java.time.{Instant, LocalDate}

import answers.action.UserCreationInstances
import answers.action.imperative.UserCreationAnswers._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.collection.mutable.ListBuffer
import scala.util.Try

class UserCreationAnswersTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks with UserCreationInstances {
  import answers.action.DateGenerator._

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
        if (yesNo) "Y" else "N"
      )
      val outputs = ListBuffer.empty[String]
      val console = Console.mock(inputs, outputs)
      val clock   = Clock.constant(now)
      val result  = readUser(console, clock)

      val expected = User(name, dob, yesNo, now)

      assert(result == expected)
    }
  }

}
