package function

import org.scalatest.{FreeSpec, Matchers}
import FunctionExercises._

class FunctionExercisesTest extends FreeSpec with Matchers {

  "doubleInc" in {
    doubleInc(0) shouldEqual 1
    doubleInc(6) shouldEqual 13
  }

}
