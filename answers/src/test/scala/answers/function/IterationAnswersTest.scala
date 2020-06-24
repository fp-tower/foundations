package answers.function

import answers.function.IterationAnswers._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class IterationAnswersTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  ////////////////////////
  // Exercise 1: for loop
  ////////////////////////

  test("sum") {
    assert(sum(List(1, 5, 2)) == 8)
    assert(sum(List()) == 0)
  }

  test("sum consistent with std library") {
    forAll { (numbers: List[Int]) =>
      assert(sum(numbers) == numbers.sum)
    }
  }

  test("sum split/merge") {
    forAll { (xs: List[Int], ys: List[Int]) =>
      assert((sum(xs) + sum(ys)) == sum(xs ++ ys))
    }
  }

  test("mkString") {
    assert(mkString(List('H', 'e', 'l', 'l', 'o')) == "Hello")
    assert(mkString(List()) == "")
  }

  test("mkString consistent with std library") {
    forAll { (letters: List[Char]) =>
      assert(mkString(letters) == letters.mkString)
    }
  }

  test("mkString reverse") {
    forAll { (letters: List[Char]) =>
      assert(mkString(letters).reverse == mkString(letters.reverse))
    }
  }

  test("wordCount") {
    assert(wordCount(List("Hi", "Hello", "Hi")) == Map("Hi" -> 2, "Hello" -> 1))
    assert(wordCount(List()) == Map())
  }

  test("wordCount add 1") {
    forAll { (words: List[String], word: String) =>
      assert(wordCount(word :: words)(word) == (wordCount(words).getOrElse(word, 0) + 1))
    }
  }

  test("foldLeft size") {
    forAll { (numbers: List[Int]) =>
      assert(foldLeft(numbers, 0)((acc, _) => acc + 1) == numbers.size)
    }
  }

  test("foldLeft reverse") {
    forAll { (numbers: List[Int]) =>
      assert(foldLeft(numbers, List.empty[Int])((acc, x) => x :: acc) == numbers.reverse)
    }
  }

  test("sumFoldLeft consistent with sum") {
    forAll { (numbers: List[Int]) =>
      assert(sumFoldLeft(numbers) == sum(numbers))
    }
  }

  test("mkStringFoldLeft consistent with mkString") {
    forAll { (letters: List[Char]) =>
      assert(mkStringFoldLeft(letters) == mkString(letters))
    }
  }

  test("wordCountFoldLeft consistent with wordCount") {
    forAll { (words: List[String]) =>
      assert(wordCountFoldLeft(words) == wordCount(words))
    }
  }

  ///////////////////////////
  // Exercise 2: recursion
  ///////////////////////////

  test("sumRecursive") {
    assert(sumRecursive(List(1, 2, 3, 4)) == 10)
    assert(sumRecursive(Nil) == 0)
  }

  test("sumRecursive is not stack safe") {
    try {
      sumRecursive(List.fill(100000)(0))
      fail("Expected stack overflow")
    } catch {
      case _: StackOverflowError => succeed
      case e: Throwable          => fail(e)
    }
  }

}
