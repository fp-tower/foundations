package answers.action.fp.search

import answers.action.fp.search.SearchFlightGenerator._
import org.scalacheck.Gen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.Ordering.Implicits._

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

  test("flights are sorted by number of stops and then price") {
    forAll(Gen.listOf(flightGen)) { (flights: List[Flight]) =>
      val result   = SearchResult(flights)
      val expected = result.flights.sorted(SearchResult.bestOrdering)
      assert(result.flights == expected)
    }
  }

  test("flights are distinct by flightId") {
    forAll(Gen.listOf(flightGen)) { (flights: List[Flight]) =>
      val result = SearchResult(flights)

      assert(result.flights.distinctBy(_.flightId) == result.flights)
    }
  }

  test("only cheapest flight is kept when same id") {
    forAll(Gen.listOf(flightGen), Gen.alphaNumStr, MinSuccessful(100)) { (flights: List[Flight], id: String) =>
      val result           = SearchResult(flights)
      val resultWithSameId = SearchResult(flights.map(_.copy(flightId = id)))

      assert(resultWithSameId.flights.size <= 1)
      assert(resultWithSameId.cheapest.map(_.unitPrice) == result.cheapest.map(_.unitPrice))
    }
  }

  test("all flights come from the inputs") {
    forAll(Gen.listOf(flightGen)) { (flights: List[Flight]) =>
      val result = SearchResult(flights)

      result.flights.foreach { resultFlight =>
        assert(flights.contains(resultFlight))
      }
    }
  }

}
