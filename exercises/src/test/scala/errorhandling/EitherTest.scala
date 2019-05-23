package errorhandling

import answers.errorhandling.EitherAnswers
import exercises.errorhandling.EitherExercises
import exercises.errorhandling.EitherExercises.GetUserError.{NonUniqueUserId, UserNotFound}
import exercises.errorhandling.EitherExercises.{parseStringToInt, PasswordError}
import exercises.errorhandling.EitherExercises.PasswordError.{NoDigit, NoLowerCase, NoUpperCase, TooSmall}
import exercises.errorhandling.OptionExercises.User
import org.scalatest.{FunSuite, Matchers}
import toimpl.errorhandling.EitherToImpl

class EitherExercisesTest extends EitherTest(EitherExercises)
class EitherAnswersTest   extends EitherTest(EitherAnswers)

class EitherTest(impl: EitherToImpl) extends FunSuite with Matchers {
  import impl._

  ////////////////////////
  // 1. Error ADT
  ////////////////////////

  test("getUser") {
    getUser(123, List(User(222, "paul"), User(123, "john"))) shouldEqual Right(User(123, "john"))
    getUser(111, List(User(222, "paul"), User(123, "john"))) shouldEqual Left(UserNotFound)
    getUser(123, List(User(123, "paul"), User(123, "john"))) shouldEqual Left(NonUniqueUserId)
  }

  validatePasswordTest("validatePassword")(validatePassword)

  def validatePasswordTest(name: String)(f: String => Either[PasswordError, Unit]) =
    test(name) {
      validatePassword("Foobar12") shouldEqual Right(())
      validatePassword("foo") shouldEqual Left(TooSmall)
      validatePassword("foobar12") shouldEqual Left(NoUpperCase)
      validatePassword("FOOBAR12") shouldEqual Left(NoLowerCase)
      validatePassword("Foobarxx") shouldEqual Left(NoDigit)
    }

  ////////////////////////
  // 2. Composing errors
  ////////////////////////

  test("leftMap") {
    leftMap(Left(List(1, 2, 3)))(xs => 0 :: xs) shouldEqual Left(List(0, 1, 2, 3))
  }

  test("tuple2") {
    tuple2(Right(1), Right("foo")) shouldEqual Right((1, "foo"))
    tuple2(Left("error1"), Left("error2")) shouldEqual Left("error1")
  }

  test("tuple3") {
    tuple3(Right(1), Right("foo"), Right(true)) shouldEqual Right((1, "foo", true))
    tuple3(Left("error1"), Right(5), Left("error2")) shouldEqual Left("error1")
  }

  test("tuple5") {
    tuple4(Right(1), Right("foo"), Right(true), Right('c')) shouldEqual Right((1, "foo", true, 'c'))
    tuple4(Left("error1"), Right(5), Left("error2"), Right('c')) shouldEqual Left("error1")
  }

  validatePasswordTest("validatePassword_v2")(validatePassword_v2)

  test("map2") {
    map2(Right(1), Right("foo"))(_.toString + _) shouldEqual Right("1foo")
    map2(Left("error1"), Right("error2"))(_.toString + _) shouldEqual Left("error1")
  }

  test("tuple2_v2") {
    tuple2_v2(Right(1), Right("foo")) shouldEqual Right((1, "foo"))
    tuple2_v2(Left("error1"), Left("error2")) shouldEqual Left("error1")
  }

  test("sequence") {
    sequence(List(Right(1), Right(5), Right(12))) shouldEqual Right(List(1, 5, 12))
    sequence(List(Right(1), Left("error"), Right(12))) shouldEqual Left("error")
  }

  validatePasswordTest("validatePassword_v3")(validatePassword_v3)

  test("traverse") {
    traverse(List("1", "23", "54"))(parseStringToInt) shouldEqual Right(List(1, 23, 54))
    traverse(List.empty[String])(parseStringToInt) shouldEqual Right(Nil)
    traverse(List("1", "hello", "54"))(parseStringToInt).isLeft shouldEqual true
  }

  validatePasswordTest("validatePassword_v4")(validatePassword_v4)

  test("traverse_") {
    traverse_(List("FooBar12", "FooBar34"))(validatePassword) shouldEqual Right(())
    traverse_(List("FooBar12", "123"))(validatePassword).isLeft shouldEqual true
  }

  validatePasswordTest("validatePassword_v5")(validatePassword_v5)

}
