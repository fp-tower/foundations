package exercises.action.fp.booking

import org.scalatest.funsuite.AnyFunSuite
import SearchFlightGenerator._
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import Ordering.Implicits._

class SearchResultTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("flights are sorted by cost from cheapest to most expensive") {
    forAll(Gen.listOf(flightGen)) { (flights: List[Flight]) =>
      val result   = SearchResult.fromList(flights)
      val expected = result.flights.sortBy(_.unitPrice)
      assert(result.flights == expected)
    }
  }

  test("flights all come from the inputs") {
    forAll(Gen.listOf(flightGen)) { (flights: List[Flight]) =>
      val result = SearchResult.fromList(flights)

      result.flights.foreach { resultFlight =>
        assert(flights.contains(resultFlight))
      }

      List(result.best, result.cheapest, result.fastest).flatten.foreach { resultFlight =>
        assert(flights.contains(resultFlight))
      }
    }
  }

  test("cheapest is indeed the cheapest flights") {
    forAll(Gen.listOf(flightGen)) { (flights: List[Flight]) =>
      val result = SearchResult.fromList(flights)

      result.cheapest match {
        case None => assert(result.flights.isEmpty)
        case Some(cheapest) =>
          result.flights.foreach { flight =>
            assert(cheapest.unitPrice <= flight.unitPrice)
          }
      }

    }
  }

  test("fastest is indeed the fastest flights") {
    forAll(Gen.listOf(flightGen)) { (flights: List[Flight]) =>
      val result = SearchResult.fromList(flights)

      result.fastest match {
        case None => assert(result.flights.isEmpty)
        case Some(fastest) =>
          result.flights.foreach { flight =>
            assert(fastest.duration <= flight.duration)
          }
      }
    }
  }

  test("no flights have the same ids") {
    forAll(Gen.listOf(flightGen)) { (flights: List[Flight]) =>
      val result = SearchResult.fromList(flights)

      result.flights.groupBy(_.flightId).foreach {
        case (_, flightsById) => assert(flightsById.size == 1)
      }
    }
  }

}
