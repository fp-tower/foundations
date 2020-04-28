package answers.function

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import IterationAnswers._

import scala.util.Try

class IterationAnswersTest extends AnyFunSuite with Matchers with ScalaCheckDrivenPropertyChecks {

  ////////////////////////
  // Exercise 1: for loop
  ////////////////////////

  test("sum") {
    sum(List(1, 5, 2)) shouldEqual 8
    sum(List()) shouldEqual 0
  }

  test("sum consistent with std library") {
    forAll { (numbers: List[Int]) =>
      sum(numbers) shouldEqual numbers.sum
    }
  }

  test("sum split/merge") {
    forAll { (xs: List[Int], ys: List[Int]) =>
      (sum(xs) + sum(ys)) shouldEqual sum(xs ++ ys)
    }
  }

  test("mkString") {
    mkString(List('H', 'e', 'l', 'l', 'o')) shouldEqual "Hello"
    mkString(List()) shouldEqual ""
  }

  test("mkString consistent with std library") {
    forAll { (letters: List[Char]) =>
      mkString(letters) shouldEqual letters.mkString
    }
  }

  test("mkString reverse") {
    forAll { (letters: List[Char]) =>
      mkString(letters).reverse shouldEqual mkString(letters.reverse)
    }
  }

  test("wordCount") {
    wordCount(List("Hi", "Hello", "Hi")) shouldEqual Map("Hi" -> 2, "Hello" -> 1)
    wordCount(List()) shouldEqual Map()
  }

  test("wordCount add 1") {
    forAll { (words: List[String], word: String) =>
      wordCount(word :: words).get(word).get shouldEqual (wordCount(words).get(word).getOrElse(0) + 1)
    }
  }

  test("foldLeft size") {
    forAll { (numbers: List[Int]) =>
      foldLeft(numbers, 0)((acc, _) => acc + 1) shouldEqual numbers.size
    }
  }

  test("foldLeft reverse") {
    forAll { (numbers: List[Int]) =>
      foldLeft(numbers, List.empty[Int])((acc, x) => x :: acc) shouldEqual numbers.reverse
    }
  }

  test("sumFoldLeft consistent with sum") {
    forAll { (numbers: List[Int]) =>
      sumFoldLeft(numbers) shouldEqual sum(numbers)
    }
  }

  test("mkStringFoldLeft consistent with mkString") {
    forAll { (letters: List[Char]) =>
      mkStringFoldLeft(letters) shouldEqual mkString(letters)
    }
  }

  test("wordCountFoldLeft consistent with wordCount") {
    forAll { (words: List[String]) =>
      wordCountFoldLeft(words) shouldEqual wordCount(words)
    }
  }

  ///////////////////////////
  // Exercise 2: recursion
  ///////////////////////////

  test("sumRecursive") {
    sumRecursive(List(1, 2, 3, 4)) shouldEqual 10
    sumRecursive(Nil) shouldEqual 0
  }

  test("sumRecursive is not stack safe") {
    try {
      sumRecursive(List.fill(100000)(0))
      fail("Expected stack overflow")
    } catch {
      case _: StackOverflowError => succeed
      case e                     => fail(e)
    }
  }

}
