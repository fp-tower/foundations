package exercises.action.fp.console

import java.time.{Instant, LocalDate}

import exercises.action.DateGenerator._
import exercises.action.fp.console.UserCreationService._
import exercises.action.fp
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.collection.mutable.ListBuffer
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen

import scala.util.{Success, Try}

class UserCreationServiceTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  val fixClock: Clock = Clock.constant(Instant.MIN)

  val invalidYesNoGen: Gen[String] =
    arbitrary[String].filterNot(Set("Y", "N"))

  val invalidDateGen: Gen[String] =
    arbitrary[String].suchThat(str => Try(parseDateOfBirth(str).unsafeRun()).isFailure)

  test("readName success") {
    forAll { (name: String) =>
      val inputs  = ListBuffer(name)
      val outputs = ListBuffer.empty[String]
      val console = Console.mock(inputs, outputs)
      val service = new UserCreationService(console, fixClock)

      val result = service.readName.unsafeRun()

      assert(result == name)
      assert(outputs.toList == List("What's your name?"))
    }
  }

  test("readDate success") {
    forAll { (date: LocalDate) =>
      val inputs  = ListBuffer(formatDateOfBirth(date))
      val outputs = ListBuffer.empty[String]
      val console = Console.mock(inputs, outputs)
      val service = new UserCreationService(console, fixClock)

      val result = service.readDateOfBirth.unsafeRun()

      assert(result == date)
      assert(outputs.toList == List("What's your date of birth? [dd-mm-yyyy]"))
    }
  }

  test("readDate failure") {
    forAll(invalidDateGen) { (input: String) =>
      val inputs  = ListBuffer(input)
      val outputs = ListBuffer.empty[String]
      val console = Console.mock(inputs, outputs)
      val service = new UserCreationService(console, fixClock)

      val result = Try(service.readDateOfBirth.unsafeRun())

      assert(result.isFailure)
      assert(
        outputs.toList == List(
          "What's your date of birth? [dd-mm-yyyy]",
          """Incorrect format, for example enter "18-03-2001" for 18th of March 2001""",
        )
      )
    }
  }

  test("readSubscribeToMailingList success") {
    forAll { (bool: Boolean) =>
      val inputs  = ListBuffer(formatYesNo(bool))
      val outputs = ListBuffer.empty[String]
      val console = Console.mock(inputs, outputs)
      val service = new UserCreationService(console, fixClock)

      val result = service.readSubscribeToMailingList.unsafeRun()

      assert(result == bool)
      assert(outputs.toList == List("Would you like to subscribe to our mailing list? [Y/N]"))
    }
  }

  test("readSubscribeToMailingList failure") {
    forAll(invalidYesNoGen) { (input: String) =>
      val inputs  = ListBuffer(input)
      val outputs = ListBuffer.empty[String]
      val console = Console.mock(inputs, outputs)
      val service = new UserCreationService(console, fixClock)

      val result = Try(service.readSubscribeToMailingList.unsafeRun())

      assert(result.isFailure)
      assert(
        outputs.toList == List(
          "Would you like to subscribe to our mailing list? [Y/N]",
          """Incorrect format, enter "Y" for Yes or "N" for "No"""",
        )
      )
    }
  }

  test("readUser no retry") {
    forAll(
      arbitrary[String],
      dateGen,
      arbitrary[Boolean],
      instantGen
    ) { (name, dob, yesNo, now) =>
      val inputs  = ListBuffer(name, formatDateOfBirth(dob), formatYesNo(yesNo))
      val outputs = ListBuffer.empty[String]
      val console = Console.mock(inputs, outputs)
      val clock   = Clock.constant(now)
      val service = new UserCreationService(console, clock)

      val result   = Try(service.readUser.unsafeRun())
      val expected = User(name, dob, yesNo, now)

      assert(result == Success(expected))
    }
  }

  ignore("readUser with retry") {
    forAll(
      arbitrary[String],
      Gen.listOf(invalidDateGen),
      dateGen,
      Gen.listOf(invalidYesNoGen),
      arbitrary[Boolean],
      instantGen,
      MinSuccessful(100)
    ) { (name, invalidDates, dob, invalidYesNo, yesNo, now) =>
      val dateInputs  = invalidDates :+ formatDateOfBirth(dob)
      val yesNoInputs = invalidYesNo :+ formatYesNo(yesNo)
      val inputs      = ListBuffer.from(List(name) ++ dateInputs ++ yesNoInputs)
      val outputs     = ListBuffer.empty[String]
      val console     = Console.mock(inputs, outputs)
      val clock       = Clock.constant(now)
      val service     = new UserCreationService(console, clock)

      val result   = Try(service.readUser.unsafeRun())
      val expected = fp.console.User(name, dob, yesNo, now)

      if (invalidDates.size < 3 && invalidYesNo.size < 3)
        assert(result == Success(expected))
      else
        assert(result.isFailure)
    }
  }

}
