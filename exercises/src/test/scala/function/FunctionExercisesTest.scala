package function

import org.scalatest.{FreeSpec, Matchers}
import FunctionExercises._

class FunctionExercisesTest extends FreeSpec with Matchers {

  "doubleInc" in {
    doubleInc(0) shouldEqual 1
    doubleInc(6) shouldEqual 13
  }

  "identity" in {
    identity(3) shouldEqual 3
    identity("foo") shouldEqual "foo"
  }

  "const" in {
    const("foo")(5) shouldEqual "foo"
    const(5)("foo") shouldEqual 5
  }

  "join" in {
    val reverse: Boolean => Boolean = x => !x
    val zeroOne: Boolean => String = x => if(x) "1" else "0"

    join(zeroOne, reverse)(_ + _.toString)(true) shouldEqual "1false"
  }

}
