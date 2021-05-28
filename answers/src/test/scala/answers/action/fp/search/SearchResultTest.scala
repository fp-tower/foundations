package answers.action.fp.search

import answers.action.fp.search.Airport._
import answers.action.fp.search.SearchFlightGenerator._
import answers.action.fp.search.SearchResult.bestOrdering
import org.scalacheck.Gen
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import java.time.{Duration, Instant}
import scala.Ordering.Implicits._

class SearchResultTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("apply removes duplicate and sorts using best") {
    val now = Instant.now()

    val flight1a = Flight("1", "BA", parisOrly, londonGatwick, now, Duration.ofMinutes(100), 0, 91.5, "")
    val flight1b = Flight("1", "BA", parisOrly, londonGatwick, now, Duration.ofMinutes(100), 0, 89.5, "")
    val flight2  = Flight("2", "LH", parisOrly, londonGatwick, now, Duration.ofMinutes(105), 0, 96.5, "")
    val flight3  = Flight("3", "BA", parisOrly, londonGatwick, now, Duration.ofMinutes(140), 1, 234.0, "")
    val flight4  = Flight("4", "LH", parisOrly, londonGatwick, now, Duration.ofMinutes(210), 2, 55.5, "")

    val result = SearchResult(List(flight3, flight1a, flight1b, flight2, flight4))

    assert(result.flights == List(flight1b, flight2, flight3, flight4))
  }

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
      val expected = result.flights.sorted(bestOrdering)
      assert(result.flights == expected)
    }
  }

  test("no duplicate flights by flight id") {
    forAll(Gen.listOf(flightGen)) { (flights: List[Flight]) =>
      val result       = SearchResult(flights)
      val distinctById = result.flights.distinctBy(_.flightId)

      assert(distinctById.size == result.flights.size)
    }
  }

  test("only cheapest flight is kept when same id") {
    forAll(flightGen, Gen.listOf(arbitrary[Double])) { (flight: Flight, prices: List[Double]) =>
      val flights = prices.map(price => flight.copy(unitPrice = price))
      val result  = SearchResult(flights)

      assert(result.flights.size <= 1)
      assert(result.cheapest.map(_.unitPrice) == prices.minOption)
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
