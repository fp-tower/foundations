package answers.action.fp

import answers.action.UserCreationInstances
import answers.action.fp.UserCreationAnswers._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.collection.mutable.ListBuffer

class UserCreationAnswersTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks with UserCreationInstances {

  test("readName") {
    forAll { (name: String, otherInputs: List[String]) =>
      val inputs  = ListBuffer.from(name :: otherInputs)
      val outputs = ListBuffer.empty[String]
      val console = Console.mock(inputs, outputs)

      val action = readName(console)

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

      val result = readSubscribeToMailingList(console).execute()

      assert(result == bool)
      assert(inputs.size == otherInputs.size)
      assert(
        outputs.toList == List("Would you like to subscribe to our mailing list? [Y/N]")
      )
    }
  }

  test("readSubscribeToMailingList failure") {
    forAll(invalidYesNoInput) { (input) =>
      val inputs  = ListBuffer(input)
      val outputs = ListBuffer.empty[String]
      val console = Console.mock(inputs, outputs)

      val result = readSubscribeToMailingList(console).attempt.execute()

      assert(result.isFailure)
      assert(
        outputs.toList == List(
          "Would you like to subscribe to our mailing list? [Y/N]",
          s"""Incorrect format, enter "Y" for Yes or "N" for "No""""
        )
      )
    }
  }

}
