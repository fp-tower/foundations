package function

import answers.function.FunctionAnswers
import exercises.function.FunctionExercises
import exercises.function.FunctionExercises.User
import org.scalatest.Matchers
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import toimpl.function.FunctionToImpl

import scala.collection.mutable.ListBuffer

class FunctionAnswersTest   extends FunctionToImplTest(FunctionAnswers)
class FunctionExercisesTest extends FunctionToImplTest(FunctionExercises)

class FunctionToImplTest(impl: FunctionToImpl) extends AnyFunSuite with Matchers with ScalaCheckDrivenPropertyChecks {
  import impl._

  ////////////////////////////
  // 1. first class functions
  ////////////////////////////

  test("tripleVal") {
    tripleVal(5) shouldEqual 15
  }

  test("tripleList") {
    tripleList(List(1, 2, 3)) shouldEqual List(3, 6, 9)
  }

  test("move") {
    move(true)(5) shouldEqual 6
    move(false)(5) shouldEqual 4
  }

  test("move2") {
    move2(true, 5) shouldEqual 6
    move2(false, 5) shouldEqual 4
  }

  test("move3") {
    move3(true)(5) shouldEqual 6
    move3(false)(5) shouldEqual 4
  }

  test("applyMany") {
    applyMany(List(_ + 1, _ - 1, _ * 2))(10) shouldEqual List(11, 9, 20)
  }

  test("applyManySum") {
    applyManySum(List(_ + 1, _ - 1, _ * 2))(10) shouldEqual 40
  }

  ////////////////////////////
  // 2. polymorphic functions
  ////////////////////////////

  test("identity") {
    identity(3) shouldEqual 3
    identity("foo") shouldEqual "foo"
  }

  test("const") {
    const("foo")(5) shouldEqual "foo"
    const(5)("foo") shouldEqual 5
    List(1, 2, 3).map(const(0)) shouldEqual List(0, 0, 0)
  }

  test("apply - apply2") {
    apply(5, (_: Int) + 1) shouldEqual 6
    apply2(5)(_ + 1) shouldEqual 6
  }

  test("setAge") {
    setAge(10) == List(User("John", 10), User("Lisa", 10))
  }

  test("getUsersUnchanged") {
    getUsersUnchanged == List(User("John", 26), User("Lisa", 5))
  }

  test("andThen") {
    val isEven = (_: Int) % 2 == 0
    val inc    = (_: Int) + 1
    andThen(inc, isEven)(10) shouldEqual false
  }

  test("andThen - compose") {
    val isEven = (_: Int) % 2 == 0
    val inc    = (_: Int) + 1
    compose(isEven, inc)(10) shouldEqual false
    andThen(inc, isEven)(10) shouldEqual false
  }

  test("doubleInc") {
    doubleInc(0) shouldEqual 1
    doubleInc(6) shouldEqual 13
  }

  test("incDouble") {
    incDouble(0) shouldEqual 2
    incDouble(6) shouldEqual 14
  }

  test("curry") {
    def plus(x: Int, y: Int): Int = x + y

    curry(plus)(4)(6) shouldEqual 10
  }

  test("uncurry") {
    def plus(x: Int)(y: Int): Int = x + y

    uncurry(plus)(4, 6) shouldEqual 10
  }

  test("join") {
    val reverse: Boolean => Boolean = x => !x
    val zeroOne: Boolean => String  = x => if (x) "1" else "0"

    join(zeroOne, reverse)(_ + _.toString)(true) shouldEqual "1false"
  }

  List(sumList _, sumList2 _, sumList3 _).zipWithIndex.foreach {
    case (f, i) =>
      test(s"sumList $i small") {
        f(List(1, 2, 3, 10)) shouldEqual 16
        f(Nil) shouldEqual 0
      }
  }

  List(sumList2 _, sumList3 _).zipWithIndex.foreach {
    case (f, i) =>
      test(s"sumList $i large") {
        val xs = 1.to(1000000).toList

        f(xs) shouldEqual xs.sum
      }
  }

  List(find[Int] _, find2[Int] _).zipWithIndex.foreach {
    case (f, i) =>
      test(s"find $i") {
        val xs = 1.to(100).toList

        f(xs)(_ == 5) shouldEqual Some(5)
        f(xs)(_ == -1) shouldEqual None
      }

      test(s"find $i is lazy") {
        val xs = 1.to(1000000).toList

        val seen = ListBuffer.empty[Int]

        val res = f(xs) { x =>
          seen += x; x > 10
        }

        res shouldEqual Some(11)
        seen.size shouldEqual 11
      }

      if (i == 0)
        test(s"find $i is stack safe") {
          val xs = 1.to(10000000).toList

          f(xs)(_ == 5) shouldEqual Some(5)
          f(xs)(_ == -1) shouldEqual None
        }
  }

  List(impl.forAll _, forAll2 _).zipWithIndex.foreach {
    case (f, i) =>
      test(s"forAll $i") {
        f(List(true, true, true)) shouldEqual true
        f(List(true, false, true)) shouldEqual false
        f(Nil) shouldEqual true
      }

      if (i == 0)
        test(s"forAll $i is stack safe") {
          val xs = List.fill(1000000)(true)

          f(xs) shouldEqual true
        }
  }

  test("memoize") {
    def inc(x: Int): Int                         = x + 1
    def circleCircumference(radius: Int): Double = 2 * radius * Math.PI

    forAll((x: Int) => memoize(inc)(x) shouldEqual inc(x))
    forAll((x: Int) => memoize(circleCircumference)(x) shouldEqual circleCircumference(x))
  }

}
