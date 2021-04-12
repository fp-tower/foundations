package answers.action.fp.booking

import java.time.ZoneId

import org.scalacheck.Gen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class SearchFlightServiceTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {
  import answers.action.DateGenerator._
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

  test("all results match the criteria") {
    forAll(Gen.listOf(searchFlightClientGen), airportGen, airportGen, dateGen, predicateGen, MinSuccessful(100)) {
      (clients, from, to, date, predicate) =>
        val service = SearchFlightService.fromClients(clients)
        val result  = service.search(from, to, date, predicate).unsafeRun()

        result.foreach { flight =>
          assert(flight.from == from)
          assert(flight.to == to)
          assert(flight.departureAt.atZone(ZoneId.of("UTC")).toLocalDate == date)
          assert(predicate.isValid(flight))
        }
    }
  }

}
