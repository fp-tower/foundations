package answers.action.fp.booking

import org.scalacheck.Gen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class SearchFlightServiceTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {
  import Generator._
  import SearchFlightService._

  test("combineFlightResults - all results satisfy the predicate") {
    forAll(
      Gen.listOf(Gen.listOf(flightGen)),
      predicateGen
    ) { (flightResults, predicate) =>
      val result = combineFlightResults(flightResults, predicate)
      assert(result.forall(predicate.isValid))
    }
  }

  test("combineFlightResults - all results are ordered by cost") {
    forAll(
      Gen.listOf(Gen.listOf(flightGen)),
      predicateGen
    ) { (flightResults, predicate) =>
      val result = combineFlightResults(flightResults, predicate)
      assert(result.sortBy(_.cost) == result)
    }
  }

  test("combineFlightResults - all results are unique by flight id") {
    forAll(
      Gen.listOf(Gen.listOf(flightGen)),
      predicateGen
    ) { (flightResults, predicate) =>
      val result = combineFlightResults(flightResults, predicate)
      assert(result.distinctBy(_.flightId).size == result.size)
    }
  }

}
