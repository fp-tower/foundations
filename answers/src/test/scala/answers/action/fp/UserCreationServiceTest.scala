package answers.action.fp

import java.time.Instant

import answers.action.UserCreationInstances
import answers.action.fp.UserCreationService._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.collection.mutable.ListBuffer
import scala.util.Success

class UserCreationServiceTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks with UserCreationInstances {

  val fixClock: Clock = Clock.constant(Instant.MIN)

  test("readName") {
    forAll { (name: String, otherInputs: List[String]) =>
      val inputs  = ListBuffer.from(name :: otherInputs)
      val outputs = ListBuffer.empty[String]
      val console = Console.mock(inputs, outputs)
      val service = new UserCreationService(console, fixClock)

      val action = service.readName

      // nothing is happen before `execute` is called
      assert(inputs.size == otherInputs.size + 1)
      assert(outputs.isEmpty)

      val result = action.execute()

      assert(result == name)
      assert(inputs.size == otherInputs.size)
      assert(outputs.size == 1)
    }
  }

  test("readSubscribeToMailingList success") {
    forAll { (bool: Boolean, otherInputs: List[String]) =>
      val boolStr = if (bool) "Y" else "N"
      val inputs  = ListBuffer.from(boolStr :: otherInputs)
      val outputs = ListBuffer.empty[String]
      val console = Console.mock(inputs, outputs)
      val service = new UserCreationService(console, fixClock)

      val result = service.readSubscribeToMailingList.execute()

      assert(result == bool)
      assert(inputs.size == otherInputs.size)
      assert(
        outputs.toList == List("Would you like to subscribe to our mailing list? [Y/N]")
      )
    }
  }

  test("readSubscribeToMailingList failure") {
    forAll(invalidYesNoGen) { (input) =>
      val inputs  = ListBuffer(input)
      val outputs = ListBuffer.empty[String]
      val console = Console.mock(inputs, outputs)
      val service = new UserCreationService(console, fixClock)

      val result = service.readSubscribeToMailingList.attempt.execute()

      assert(result.isFailure)
      assert(
        outputs.toList == List(
          "Would you like to subscribe to our mailing list? [Y/N]",
          s"""Incorrect format, enter "Y" for Yes or "N" for "No""""
        )
      )
    }
  }

  test("readUser") {
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
      val result      = service.readUser.attempt.execute()

      val user = User(name, dob, yesNo, now)

      if (invalidDates.size < 3 && invalidYesNo.size < 3)
        assert(result == Success(user))
      else
        assert(result.isFailure)
    }
  }

}
