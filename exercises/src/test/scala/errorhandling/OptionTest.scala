package errorhandling

import answers.errorhandling.OptionAnswers
import eu.timepit.refined.W
import eu.timepit.refined.refineMV
import eu.timepit.refined.numeric.Interval
import exercises.errorhandling.OptionExercises
import exercises.errorhandling.OptionExercises.User
import org.scalatest.{FunSuite, Matchers}
import toimpl.errorhandling.OptionToImpl

class OptionExercisesTest extends OptionTest(OptionExercises)
class OptionAnswersTest   extends OptionTest(OptionAnswers)

class OptionTest(impl: OptionToImpl) extends FunSuite with Matchers {
  import impl._

  test("getUser") {
    getUser(123, List(User(222, "paul"), User(123, "john"))) shouldEqual Some(User(123, "john"))
    getUser(111, List(User(222, "paul"), User(123, "john"))) shouldEqual None
  }

  test("charToDigit") {
    0.to(9).map(x => charToDigit(x.toString.head) shouldEqual Some(x))
    charToDigit('A') shouldEqual None
  }

  test("refinedCharToDigit") {
    type ZeroToNine = Interval.Closed[W.`0`.T, W.`9`.T]
    refinedCharToDigit('0') shouldEqual Some(refineMV[ZeroToNine](0))
    refinedCharToDigit('4') shouldEqual Some(refineMV[ZeroToNine](4))
    refinedCharToDigit('A') shouldEqual None
  }

}
