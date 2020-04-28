package function

import answers.function.FunctionAnswers
import answers.function.FunctionAnswers._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.collection.mutable.ListBuffer

class FunctionAnswersTest extends AnyFunSuite with Matchers with ScalaCheckDrivenPropertyChecks {

  /////////////////////////////////////////
  // 4-6. Iteration & Recursion & Laziness
  /////////////////////////////////////////

  val largeSize = 1000000

  List(sum _, sumFoldLeft _, sumRecursive _).zipWithIndex.foreach {
    case (f, i) =>
      test(s"sumList $i small") {
        f(List(1, 2, 3, 10)) shouldEqual 16
        f(Nil) shouldEqual 0
      }

      test(s"sumList $i large") {
        val xs = 1.to(largeSize).toList

        f(xs) shouldEqual xs.sum
      }
  }

  List(mkString _, mkStringFoldLeft _).zipWithIndex.foreach {
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

  List(FunctionAnswers.forAll _, FunctionAnswers.forAllFoldRight _).zipWithIndex
    .foreach {
      case (f, i) =>
        test(s"forAll $i") {
          f(List(true, true, true)) shouldEqual true
          f(List(true, false, true)) shouldEqual false
          f(Nil) shouldEqual true
        }

        test(s"forAll $i is stack safe") {
          val xs = List.fill(largeSize)(true)

          f(xs) shouldEqual true
        }
    }

  List(find[Int] _, findFoldRight[Int] _).zipWithIndex.foreach {
    case (f, i) =>
      test(s"find $i") {
        val xs = 1.to(100).toList

        f(xs)(_ == 5) shouldEqual Some(5)
        f(xs)(_ == -1) shouldEqual None
      }

      test(s"find $i is lazy") {
        val xs = 1.to(largeSize).toList

        val seen = ListBuffer.empty[Int]

        val res = f(xs) { x =>
          seen += x; x > 10
        }

        res shouldEqual Some(11)
        seen.size shouldEqual 11
      }

      test(s"find $i is stack safe") {
        val xs = 1.to(largeSize).toList

        f(xs)(_ == 5) shouldEqual Some(5)
        f(xs)(_ == -1) shouldEqual None
      }
  }

  test("headOption") {
    headOption(List(1, 2, 3, 4)) shouldEqual Some(1)
    headOption(Nil) shouldEqual None
    headOption(List.fill(largeSize)(1)) shouldEqual Some(1)
  }

  ////////////////////////
  // 5. Memoization
  ////////////////////////

  test("memoize") {
    def inc(x: Int): Int                         = x + 1
    def circleCircumference(radius: Int): Double = 2 * radius * Math.PI

    forAll((x: Int) => memoize(inc)(x) shouldEqual inc(x))
    forAll((x: Int) => memoize(circleCircumference)(x) shouldEqual circleCircumference(x))
  }

}
