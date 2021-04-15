package exercises.action.fp.search

import org.scalacheck.Gen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import exercises.action.DateGenerator._
import exercises.action.fp.search.SearchFlightGenerator._

class SearchFlightServiceTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  ignore("fromTwoClients - all flights must match the from, to and date requested") {
    forAll(searchFlightClientGen, searchFlightClientGen, airportGen, airportGen, dateGen, MinSuccessful(100)) {
      (client1, client2, from, to, date) =>
        val service = SearchFlightService.fromTwoClients(client1, client2)
        val result  = service.search(from, to, date).unsafeRun()

        result.flights.foreach { flight =>
          assert(flight.from == from)
          assert(flight.to == to)
          assert(flight.departureDate == date)
        }
    }
  }
}
