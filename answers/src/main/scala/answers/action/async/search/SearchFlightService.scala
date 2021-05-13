package answers.action.async.search

import answers.action.async._
import answers.action.fp.search.{Airport, Flight, SearchResult}

import java.time.LocalDate
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

trait SearchFlightService {
  def search(from: Airport, to: Airport, date: LocalDate): IO[SearchResult]
}

object SearchFlightService {

  def fromClients(clients: List[SearchFlightClient])(ec: ExecutionContext): SearchFlightService =
    new SearchFlightService {
      def search(from: Airport, to: Airport, date: LocalDate): IO[SearchResult] = {
        def searchPerClient(client: SearchFlightClient): IO[List[Flight]] =
          client
            .search(from, to, date)
            .map(removeInvalidFlights)
            .timeout(100.millis)(ec)
            .handleErrorWith(_ => IO(Nil))

        def removeInvalidFlights(flights: List[Flight]): List[Flight] =
          flights.filter { flight =>
            flight.from == from && flight.to == to && flight.departureDate == date
          }

        clients
          .parTraverse(searchPerClient)(ec)
          .map(_.flatten)
          .map(SearchResult(_))
      }
    }

}
