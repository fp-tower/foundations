package answers.action.fp.search

import answers.action.DateGenerator._
import answers.action.fp.search.Airport._
import answers.action.fp.search.SearchFlightGenerator._
import org.scalacheck.Gen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import java.time.{Duration, Instant, LocalDate}

class SearchFlightServiceTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("fromTwoClients example") {
    val now   = Instant.now()
    val today = LocalDate.now()

    val flight1 = Flight("1", "BA", parisOrly, londonGatwick, now, Duration.ofMinutes(100), 0, 89.5, "")
    val flight2 = Flight("2", "LH", parisOrly, londonGatwick, now, Duration.ofMinutes(105), 0, 96.5, "")
    val flight3 = Flight("3", "BA", parisOrly, londonGatwick, now, Duration.ofMinutes(140), 1, 234.0, "")
    val flight4 = Flight("4", "LH", parisOrly, londonGatwick, now, Duration.ofMinutes(210), 2, 55.5, "")

    val client1 = SearchFlightClient.constant(List(flight3, flight1))
    val client2 = SearchFlightClient.constant(List(flight2, flight4))

    val service = SearchFlightService.fromTwoClients(client1, client2)
    val result  = service.search(parisOrly, londonGatwick, today).unsafeRun()

    assert(result == SearchResult(List(flight1, flight2, flight3, flight4)))
  }

  test("fromClients - all results must match the from, to and date requested") {
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