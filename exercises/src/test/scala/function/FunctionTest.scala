package function

import answers.function.FunctionAnswers
import exercises.function.FunctionExercises
import exercises.function.FunctionExercises.{Direction, User}
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
    move(Direction.Up)(5) shouldEqual 6
    move(Direction.Down)(5) shouldEqual 4
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

  test("setUsersAge") {
    setUsersAge(10) shouldEqual List(User("John", 10), User("Lisa", 10))
  }

  test("getUsers") {
    getUsers shouldEqual List(User("John", 26), User("Lisa", 5))
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

  ///////////////////////////
  // 3. Recursion & Laziness
  ///////////////////////////

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

  List(mkString _, mkString2 _).zipWithIndex.foreach {
    case (f, i) =>
      test(s"mkString $i") {
        forAll((s: String) => f(s.toList) shouldEqual s)
      }
  }

  test("multiply") {
    multiply(Nil) shouldEqual 1
    multiply(List(0, 2, 4)) shouldEqual 0
    multiply(List(1, 2, 4)) shouldEqual 8

    forAll((x: Int, xs: List[Int]) => multiply(x :: xs) shouldEqual (x * multiply(xs)))

    forAll((xs: List[Int]) => multiply(xs) shouldEqual multiply(xs.reverse))
  }

  test("filter") {
    forAll((xs: List[Int], p: Int => Boolean) => filter(xs)(p) shouldEqual xs.filter(p))
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

  test("memoize") {
    def inc(x: Int): Int                         = x + 1
    def circleCircumference(radius: Int): Double = 2 * radius * Math.PI

    forAll((x: Int) => memoize(inc)(x) shouldEqual inc(x))
    forAll((x: Int) => memoize(circleCircumference)(x) shouldEqual circleCircumference(x))
  }

}
