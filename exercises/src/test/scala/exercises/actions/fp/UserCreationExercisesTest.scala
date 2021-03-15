package exercises.actions.fp

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

}
