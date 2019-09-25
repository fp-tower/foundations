package errorhandling

import answers.errorhandling.EitherAnswers
import exercises.errorhandling.EitherExercises.GetOrderError.{NonUniqueOrderId, OrderNotFound}
import exercises.errorhandling.EitherExercises.UsernameError.{InvalidCharacter, TooSmall}
import exercises.errorhandling.EitherExercises.{parseStringToInt, Order, UsernameError}
import exercises.errorhandling.{EitherExercises, Username}
import org.scalatest.Matchers
import org.scalatest.funsuite.AnyFunSuite
import toimpl.errorhandling.EitherToImpl

class EitherExercisesTest extends EitherTest(EitherExercises)
class EitherAnswersTest   extends EitherTest(EitherAnswers)

class EitherTest(impl: EitherToImpl) extends AnyFunSuite with Matchers {
  import impl._

  ////////////////////////
  // 1. Use cases
  ////////////////////////

  test("getOrder") {
    getOrder(123, List(Order(222, "paul"), Order(123, "john"))) shouldEqual Right(Order(123, "john"))
    getOrder(111, List(Order(222, "paul"), Order(123, "john"))) shouldEqual Left(OrderNotFound)
    getOrder(123, List(Order(123, "paul"), Order(123, "john"))) shouldEqual Left(NonUniqueOrderId)
  }

  test("validateUsernameSize") {
    validateUsernameSize("moreThan3Char") == Right(())
    validateUsernameSize("foo") == Right(())
    validateUsernameSize("fo") == Left(TooSmall)
  }

  test("validateUsernameCharacter") {
    validateUsernameCharacter('a') shouldEqual Right(())
    validateUsernameCharacter('B') shouldEqual Right(())
    validateUsernameCharacter('3') shouldEqual Right(())
    validateUsernameCharacter('-') shouldEqual Right(())
    validateUsernameCharacter('_') shouldEqual Right(())
    validateUsernameCharacter('!') shouldEqual Left(InvalidCharacter('!'))
    validateUsernameCharacter('@') shouldEqual Left(InvalidCharacter('@'))
  }

  validateUsernameContentTest(1)(validateUsernameContent)

  validateUsernameTest(1)(validateUsername)

  def validateUsernameContentTest(count: Int)(f: String => Either[InvalidCharacter, Unit]) =
    test(s"validateUsernameContent $count") {
      validateUsernameContent("Foo1-2_") shouldEqual Right(())
      validateUsernameContent("!(Foo)") shouldEqual Left(InvalidCharacter('!'))
    }

  def validateUsernameTest(count: Int)(f: String => Either[UsernameError, Username]) =
    test(s"validateUsername $count") {
      validateUsername("foo") shouldEqual Right(Username("foo"))
      validateUsername("  foo ") shouldEqual Right(Username("foo"))
      validateUsername("abc!@£") shouldEqual Left(InvalidCharacter('!'))
      validateUsername(" yo") shouldEqual Left(TooSmall)
    }

  ////////////////////////
  // 2. Composing Either
  ////////////////////////

  test("leftMap") {
    leftMap(Left(List(1, 2, 3)))(xs => 0 :: xs) shouldEqual Left(List(0, 1, 2, 3))
  }

  test("tuple2") {
    tuple2(Right(1), Right("foo")) shouldEqual Right((1, "foo"))
    tuple2(Left("error1"), Left("error2")) shouldEqual Left("error1")
  }

  test("map2") {
    map2(Right(1), Right("foo"))(_.toString + _) shouldEqual Right("1foo")
    map2(Left("error1"), Right("error2"))(_.toString + _) shouldEqual Left("error1")
  }

  validateUsernameTest(2)(validateUsername_v2)

  validateUsernameTest(3)(validateUsername_v3)

  test("sequence") {
    sequence(List(Right(1), Right(5), Right(12))) shouldEqual Right(List(1, 5, 12))
    sequence(List(Right(1), Left("error"), Right(12))) shouldEqual Left("error")
  }

  validateUsernameContentTest(2)(validateUsernameContent_v2)

  test("traverse") {
    traverse(List("1", "23", "54"))(parseStringToInt) shouldEqual Right(List(1, 23, 54))
    traverse(List.empty[String])(parseStringToInt) shouldEqual Right(Nil)
    traverse(List("1", "hello", "54"))(parseStringToInt).isLeft shouldEqual true
  }

  validateUsernameContentTest(3)(validateUsernameContent_v3)

  test("traverse_") {
    traverse_(List("1", "23", "54"))(parseStringToInt) shouldEqual Right(())
    traverse_(List("1", "hello", "54"))(validateUsername).isLeft shouldEqual true
  }

  validateUsernameContentTest(4)(validateUsernameContent_v4)

}
