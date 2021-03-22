package exercises.action.imperative

import java.time.Instant

import exercises.action.TimeInstances
import exercises.action.imperative.UserCreationExercises.User
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.collection.mutable.ListBuffer
import scala.util.{Success, Try}

class UserCreationServiceTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks with TimeInstances {

  ignore("readUser") {
    val name        = "Bob"
    val dateInputs  = List("Hey", "21st July 1986", "21-07-1986")
    val yesNoInputs = List("Never", "Maybe", "Y")
    val now         = Instant.ofEpochSecond(9999999999L)
    val inputs      = ListBuffer.from(List(name) ++ dateInputs ++ yesNoInputs)
    val outputs     = ListBuffer.empty[String]
    val console     = Console.mock(inputs, outputs)
    val clock       = Clock.constant(now)
    val service     = new UserCreationService(console, clock)
    val result      = Try(service.readUser())

    val user = ???

    assert(result == Success(user))
  }

}
