package function

import answers.function.FunctionAnswers
import answers.function.FunctionAnswers._
import org.scalatest.Matchers
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.collection.mutable.ListBuffer

class FunctionAnswersTest extends AnyFunSuite with Matchers with ScalaCheckDrivenPropertyChecks {

  //////////////////////////////////////////////////////
  // 1. Functions as input (aka higher order functions)
  //////////////////////////////////////////////////////

  test("keepLetters") {
    keepLetters("123foo0-!Bar~+3") shouldEqual "fooBar"
  }

  test("secret") {
    secret("abc123") shouldEqual "******"
  }

  test("isValidUsernameCharacter") {
    isValidUsernameCharacter('a') shouldEqual true
    isValidUsernameCharacter('A') shouldEqual true
    isValidUsernameCharacter('1') shouldEqual true
    isValidUsernameCharacter('-') shouldEqual true
    isValidUsernameCharacter('_') shouldEqual true
    isValidUsernameCharacter('~') shouldEqual false
    isValidUsernameCharacter('!') shouldEqual false
  }

  test("isValidUsername") {
    isValidUsername("john-doe") shouldEqual true
    isValidUsername("*john*") shouldEqual false
  }

  //////////////////////////////////////////////////
  // 2. functions as output (aka curried functions)
  //////////////////////////////////////////////////

  test("increment - decrement") {
    increment(5) shouldEqual 6
    decrement(5) shouldEqual 4
  }

  ////////////////////////////
  // 3. parametric functions
  ////////////////////////////

  test("Pair#swap") {
    Pair("John", "Doe").swap shouldEqual Pair("Doe", "John")
  }

  test("Pair#map") {
    Pair("John", "Doe").map(_.length) shouldEqual Pair(4, 3)
  }

  test("Pair#forAll") {
    Pair("John", "Doe").forAll(_.length > 2) shouldEqual true
    Pair("John", "Doe").forAll(_.startsWith("J")) shouldEqual false
    Pair("John", "Doe").forAll(_.startsWith("H")) shouldEqual false
  }

  test("Pair#zipWith") {
    Pair(0, 2).zipWith(Pair(3, 3), (x: Int, y: Int) => x + y) shouldEqual Pair(3, 5)
  }

  test("Pair#zipWithCurried") {
    Pair(0, 2).zipWithCurried(Pair(3, 3))(_ + _) shouldEqual Pair(3, 5)
  }

  test("users") {
    users shouldEqual Pair(User("John", 32), User("Elisabeth", 46))
  }

  test("longerThan5") {
    longerThan5 shouldEqual false
  }

  test("identity") {
    forAll((x: Int) => identity(x) shouldEqual x)
  }

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
