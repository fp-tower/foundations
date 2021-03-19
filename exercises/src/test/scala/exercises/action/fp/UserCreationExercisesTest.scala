package exercises.action.fp

import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.collection.mutable.ListBuffer
import UserCreationExercises._

class UserCreationExercisesTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("readName") {
    forAll { (name: String, otherInputs: List[String]) =>
      val inputs  = ListBuffer.from(name :: otherInputs)
      val outputs = ListBuffer.empty[String]
      val console = Console.mock(inputs, outputs)

      val result = readName(console).execute()

      assert(result == name)
      assert(inputs.toList == otherInputs) // consumed one input
      assert(outputs.toList == List("What's your name?"))
    }
  }

}
