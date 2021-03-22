package answers.action.imperative

import answers.action.UserCreationInstances
import answers.action.imperative.UserCreationService._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.collection.mutable.ListBuffer
import scala.util.{Success, Try}

class UserCreationServiceTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks with UserCreationInstances {

  test("readUser") {
    forAll(
      arbitrary[String],
      Gen.listOf(invalidDateGen),
      dateGen,
      Gen.listOf(invalidYesNoGen),
      arbitrary[Boolean],
      instantGen
    ) { (name, invalidDates, dob, invalidYesNo, yesNo, now) =>
      val dateInputs  = invalidDates :+ formatDate(dob)
      val yesNoInputs = invalidYesNo :+ formatBoolean(yesNo)
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
