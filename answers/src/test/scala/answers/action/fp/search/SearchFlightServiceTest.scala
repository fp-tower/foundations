package answers.action.fp.search

import org.scalacheck.Gen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class SearchFlightServiceTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {
  import SearchFlightGenerator._
  import answers.action.DateGenerator._

  test("all results match the criteria") {
    forAll(Gen.listOf(searchFlightClientGen), airportGen, airportGen, dateGen, MinSuccessful(100)) {
      (clients, from, to, date) =>
        val service = SearchFlightService.fromClients(clients)
        val result  = service.search(from, to, date).unsafeRun()

        result.flights.foreach { flight =>
          assert(flight.from == from)
          assert(flight.to == to)
          assert(flight.departureDate == date)
        }
    }
  }

}
