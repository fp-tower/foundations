package answers.function

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import IterationAnswers._

class IterationAnswersTest extends AnyFunSuite with Matchers with ScalaCheckDrivenPropertyChecks {

  ////////////////////////
  // Exercise 1: for loop
  ////////////////////////

  test("sum") {
    sum(List(1, 5, 2)) shouldEqual 8
    sum(List()) shouldEqual 0
  }

  test("sum consistent with std library") {
    forAll((numbers: List[Int]) => sum(numbers) shouldEqual numbers.sum)

    forAll((xs: List[Int], ys: List[Int]) => (sum(xs) + sum(ys)) shouldEqual sum(xs ++ ys))
  }

  test("mkString") {
    mkString(List('H', 'e', 'l', 'l', 'o')) shouldEqual "Hello"
    mkString(List()) shouldEqual ""
  }

  test("mkString pbt") {
    forAll((letters: List[Char]) => mkString(letters) shouldEqual letters.mkString)

    forAll((letters: List[Char]) => mkString(letters).reverse shouldEqual mkString(letters.reverse))
  }

  test("wordCount") {
    wordCount(List("Hi", "Hello", "Hi")) shouldEqual Map("Hi" -> 2, "Hello" -> 1)
    wordCount(List()) shouldEqual Map()
  }

  test("wordCount pbt") {
    forAll { (words: List[String], word: String) =>
      wordCount(word :: words).get(word).get shouldEqual (wordCount(words).get(word).getOrElse(0) + 1)
    }
  }

  test("foldLeft") {
    forAll { (numbers: List[Int]) =>
      foldLeft(numbers, 0)((acc, _) => acc + 1) shouldEqual numbers.size
    }

    forAll { (numbers: List[Int]) =>
      foldLeft(numbers, List.empty[Int])((acc, x) => x :: acc) shouldEqual numbers.reverse
    }
  }

}
