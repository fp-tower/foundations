package answers.dataprocessing

import answers.dataprocessing.RecursionAnswers._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class RecursionAnswersTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  val largeSize = 100000

  test("unsafeSum is not stack-safe") {
    try {
      unsafeSum(List.fill(largeSize)(0))
      fail("Expected stack overflow")
    } catch {
      case _: StackOverflowError => succeed
      case e: Throwable          => fail(e)
    }
  }

  test("sum") {
    assert(sum(List(1, 5, 2)) == 8)
    assert(sum(Nil) == 0)
    assert(sum(List.fill(largeSize)(0)) == 0)
  }

  test("sum is consistent with std library") {
    forAll { (numbers: List[Int]) =>
      assert(sum(numbers) == numbers.sum)
    }
  }

  test("reverse is consistent with std library") {
    forAll { (numbers: List[Int]) =>
      assert(reverse(numbers) == numbers.reverse)
    }
  }

  test("reverse twice is a noop") {
    forAll { (numbers: List[Int]) =>
      assert(reverse(reverse(numbers)) == numbers)
    }
  }

  test("min is consistent with std library") {
    forAll { (numbers: List[Int]) =>
      assert(min(numbers) == numbers.minOption)
    }
  }

  test("foldLeft is consistent with std library") {
    forAll { (numbers: List[Int], default: Int, combine: (Int, Int) => Int) =>
      assert(foldLeft(numbers, default)(combine) == numbers.foldLeft(default)(combine))
    }
  }

}
