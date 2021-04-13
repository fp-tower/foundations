package exercises.action.imperative

import java.time.{Instant, LocalDate}

import exercises.action.DateGenerator._
import exercises.action.imperative.UserCreationExercises.User
import exercises.action.imperative.UserCreationService._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.collection.mutable.ListBuffer
import scala.util.{Success, Try}

class UserCreationServiceTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("formatDate - parseDate round trip") {
    forAll { (date: LocalDate) =>
      val encoded: String = formatDate(date)
      assert(parseDate(encoded) == date)
    }
  }

  ignore("readUser") {
    val name        = "Bob"
    val dob         = LocalDate.of(1986, 7, 21)
    val dateInputs  = List("Hey", "21st July 1986", formatDate(dob))
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
