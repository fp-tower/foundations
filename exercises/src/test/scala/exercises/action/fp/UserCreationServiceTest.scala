package exercises.action.fp

import java.time.{Instant, LocalDate}

import exercises.action.TimeInstances
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.collection.mutable.ListBuffer
import exercises.action.fp.UserCreationService._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen

import scala.util.{Success, Try}

class UserCreationServiceTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks with TimeInstances {

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

  ignore("readUser with retry") {
    forAll(
      arbitrary[String],
      Gen.listOf(invalidDateGen),
      dateGen,
      Gen.listOf(invalidYesNoGen),
      arbitrary[Boolean],
      instantGen
    ) { (name, invalidDates, dob, invalidYesNo, yesNo, now) =>
      val dateInputs  = invalidDates :+ formatDateOfBirth(dob)
      val yesNoInputs = invalidYesNo :+ formatYesNo(yesNo)
      val inputs      = ListBuffer.from(List(name) ++ dateInputs ++ yesNoInputs)
      val outputs     = ListBuffer.empty[String]
      val console     = Console.mock(inputs, outputs)
      val clock       = Clock.constant(now)
      val service     = new UserCreationService(console, clock)
      val result      = Try(service.readUser.unsafeRun())

      val user = User(name, dob, yesNo, now)

      if (invalidDates.size < 3 && invalidYesNo.size < 3)
        assert(result == Success(user))
      else
        assert(result.isFailure)
    }
  }

}
