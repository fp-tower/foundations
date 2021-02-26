package answers.action.v3

import java.time.LocalDate

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.collection.mutable.ListBuffer
import ConsoleAnswers._

import scala.util.Try

class ConsoleAnswersTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  implicit val localDateArbitrary: Arbitrary[LocalDate] =
    Arbitrary(
      Gen
        .choose(LocalDate.MIN.toEpochDay, LocalDate.MAX.toEpochDay)
        .map(LocalDate.ofEpochDay)
    )

  test("user console") {
    forAll { (name: String, ageAttempt1: String, ageAttempt2: String, age: Int, date: LocalDate) =>
      val ages    = List(ageAttempt1, ageAttempt2, age.toString)
      val inputs  = ListBuffer(name) ++ ages
      val outputs = ListBuffer.empty[String]
      val console = Console.test(inputs, outputs)
      val clock   = Clock.constant(date)

      val firstAge = ages.flatMap(x => Try(x.toInt).toOption).head

      assert(
        testableUserConsole(console, clock).execute() ==
          User(name, firstAge, date)
      )
    }

  }

}
