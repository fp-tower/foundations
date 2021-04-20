package answers.action.imperative

import java.time.LocalDate

import answers.action.DateGenerator._
import answers.action.UserCreationGenerator._
import answers.action.imperative.UserCreationService._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.collection.mutable.ListBuffer
import scala.util.{Success, Try}

class UserCreationServiceTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("formatDate - parseDate round trip") {
    forAll { (date: LocalDate) =>
      val encoded: String = formatDateOfBirth(date)
      assert(parseDateOfBirth(encoded) == date)
    }
  }

  test("formatBoolean - parseYesNo round trip") {
    forAll { (boolean: Boolean) =>
      val encoded: String = formatYesNo(boolean)
      assert(parseYesNo(encoded) == boolean)
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
      val result      = Try(service.readUser())

      val user = User(name, dob, yesNo, now)

      if (invalidDates.size < 3 && invalidYesNo.size < 3)
        assert(result == Success(user))
      else
        assert(result.isFailure)
    }
  }

}
