package exercises.action.fp.search

import exercises.action.fp.search.SearchFlightGenerator._
import org.scalacheck.Gen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.Ordering.Implicits._

// Run the test using the green arrow next to class name (if using IntelliJ)
// or run `sbt` in the terminal to open it in shell mode, then type:
// testOnly exercises.action.fp.search.SearchResultTest
class SearchResultTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("cheapest, fastest and best are consistent with flights") {
    forAll(Gen.listOf(flightGen)) { (flights: List[Flight]) =>
      val result = SearchResult(flights)

      (result.cheapest, result.fastest, result.best) match {
        case (None, None, None)             => assert(result.flights.isEmpty)
        case (Some(f1), Some(f2), Some(f3)) => assert(result.flights.exists(Set(f1, f2, f3)))
        case _                              => fail("inconsistent")
      }
    }
  }

  test("cheapest is cheaper than any other flights") {
    forAll(Gen.listOf(flightGen)) { (flights: List[Flight]) =>
      val result = SearchResult(flights)

      for {
        cheapest <- result.cheapest
        flight   <- result.flights
      } assert(cheapest.unitPrice <= flight.unitPrice)
    }
  }

  test("fastest is faster than any other flights") {
    forAll(Gen.listOf(flightGen)) { (flights: List[Flight]) =>
      val result = SearchResult(flights)

      for {
        fastest <- result.fastest
        flight  <- result.flights
      } assert(fastest.duration <= flight.duration)
    }
  }

}
