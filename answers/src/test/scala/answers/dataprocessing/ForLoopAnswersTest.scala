package answers.dataprocessing

import answers.dataprocessing.ForLoopAnswers._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class ForLoopAnswersTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("sum") {
    assert(sum(List(1, 5, 2)) == 8)
    assert(sum(List()) == 0)
  }

  test("sum is consistent with std library") {
    forAll { (numbers: List[Int]) =>
      assert(sum(numbers) == numbers.sum)
    }
  }

  test("sum concat") {
    forAll { (first: List[Int], second: List[Int]) =>
      assert((sum(first) + sum(second)) == sum(first ++ second))
    }
  }

  test("size") {
    assert(sum(List(1, 5, 2)) == 8)
    assert(sum(List()) == 0)
  }

  test("size is consistent with std library") {
    forAll { (numbers: List[Int]) =>
      assert(size(numbers) == numbers.size)
    }
  }

  test("size concat") {
    forAll { (first: List[Int], second: List[Int]) =>
      assert((size(first) + size(second)) == size(first ++ second))
    }
  }

  test("min") {
    assert(min(List(2, 5, 1, 8)) == Some(1))
    assert(min(Nil) == None)
  }

  test("min is lower than all values in the list") {
    forAll { (numbers: List[Int]) =>
      for {
        minValue <- min(numbers)
        number   <- numbers
      } assert(minValue <= number)
    }
  }

  test("min belongs to the list") {
    forAll { (numbers: List[Int]) =>
      for (minValue <- min(numbers))
        assert(numbers.contains(minValue))
    }
  }

  test("wordCount") {
    assert(wordCount(List("Hi", "Hello", "Hi")) == Map("Hi" -> 2, "Hello" -> 1))
    assert(wordCount(List()) == Map())
  }

  test("wordCount all counts are strictly greater than 0") {
    forAll { (words: List[String]) =>
      assert(wordCount(words).values.forall(_ > 0))
    }
  }

  test("all words are part of the result") {
    forAll { (words: List[String]) =>
      val keys = wordCount(words).keySet
      assert(words.forall(keys.contains))
    }
  }

  test("wordCount add 1") {
    forAll { (words: List[String], word: String) =>
      val result = wordCount(word :: words)
      wordCount(words).get(word) match {
        case None        => assert(result(word) == 1)
        case Some(count) => assert(result(word) == count + 1)
      }
    }
  }

  test("foldLeft is consistent with std library") {
    forAll { (numbers: List[Int], default: Int, combine: (Int, Int) => Int) =>
      assert(foldLeft(numbers, default)(combine) == numbers.foldLeft(default)(combine))
    }
  }

  test("foldLeft noop") {
    forAll { (numbers: List[Int]) =>
      assert(foldLeft(numbers, List.empty[Int])(_ :+ _) == numbers)
    }
  }

  test("sumFoldLeft consistent with sum") {
    forAll { (numbers: List[Int]) =>
      assert(sumFoldLeft(numbers) == sum(numbers))
    }
  }

  test("sizeFoldLeft consistent with size") {
    forAll { (numbers: List[Int]) =>
      assert(sizeFoldLeft(numbers) == sizeFoldLeft(numbers))
    }
  }

  test("minFoldLeft consistent with min") {
    forAll { (numbers: List[Int]) =>
      assert(minFoldLeft(numbers) == min(numbers))
    }
  }

  test("wordCountFoldLeft consistent with wordCount") {
    forAll { (words: List[String]) =>
      assert(wordCountFoldLeft(words) == wordCount(words))
    }
  }
}
