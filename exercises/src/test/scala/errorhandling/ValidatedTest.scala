package errorhandling

import answers.errorhandling.ValidatedAnswers
import cats.data.NonEmptyList
import exercises.errorhandling.Validated._
import exercises.errorhandling.ValidatedExercises
import org.scalatest.{FunSuite, Matchers}
import toimpl.errorhandling.ValidatedToImpl

class ValidatedExercisesTest extends ValidatedTest(ValidatedExercises)

class ValidatedAnswersTest extends ValidatedTest(ValidatedAnswers)

class ValidatedTest(impl: ValidatedToImpl) extends FunSuite with Matchers {
  import impl._

  def boom[A, B, C](a: A, b: B): C = ???

  test("tuple2") {
    tuple2(valid(1), valid("foo"))(boom) shouldEqual Valid((1, "foo"))
    tuple2(Invalid(1), Valid("foo"))(boom) shouldEqual Invalid(1)
    tuple2(Invalid("error1"), Invalid("error2"))(_ ++ _) shouldEqual Invalid("error1error2")
  }

  test("map2") {
    map2(Valid(3), Valid(2))(_ * _, boom) shouldEqual Valid(6)
    map2(Invalid(1), Valid("foo"))(boom, boom) shouldEqual Invalid(1)
    map2(Invalid("error1"), Invalid("error2"))(boom, _ ++ _) shouldEqual Invalid("error1error2")
  }

  test("tuple2Nel") {
    tuple2Nel(Valid(1), Valid("foo")) shouldEqual Valid((1, "foo"))
    tuple2Nel(Invalid(NonEmptyList.of(1)), Valid("foo")) shouldEqual Invalid(NonEmptyList.of(1))
    tuple2Nel(Invalid(NonEmptyList.of("error1")), Invalid(NonEmptyList.of("error2"))) shouldEqual
      Invalid(NonEmptyList.of("error1", "error2"))
  }

}
