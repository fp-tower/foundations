package function

import answers.function.FunctionAnswers
import exercises.function.FunctionExercises
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FreeSpec, Matchers}
import toimpl.function.FunctionToImpl

class FunctionAnswersTest extends FunctionToImplTest(FunctionAnswers)
class FunctionExercisesTest extends FunctionToImplTest(FunctionExercises)

class FunctionToImplTest(impl: FunctionToImpl) extends FreeSpec with Matchers with GeneratorDrivenPropertyChecks {
  import impl._

  "doubleInc" in {
    doubleInc(0) shouldEqual 1
    doubleInc(6) shouldEqual 13
  }

  "curry" in {
    def plus(x: Int, y: Int): Int = x + y

    curry(plus)(4)(6) shouldEqual 10
  }

  "uncurry" in {
    def plus(x: Int)(y: Int): Int = x + y

    uncurry(plus)(4, 6) shouldEqual 10
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

  "sumList small" in {
    sumList(List(1,2,3,10)) shouldEqual 16
    sumList(Nil) shouldEqual 0
  }

  "sumList large" in {
    val xs = 1.to(1000000).toList

    sumList(xs) shouldEqual xs.sum
  }

  "memoize" in {
    def inc(x: Int): Int = x + 1
    def circleCircumference(radius: Int): Double = 2 * radius * Math.PI

    forAll((x: Int) => memoize(inc)(x) shouldEqual inc(x))
    forAll((x: Int) => memoize(circleCircumference)(x) shouldEqual circleCircumference(x))
  }

}
