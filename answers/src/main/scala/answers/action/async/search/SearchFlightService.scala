package answers.action.async.search

import java.time.{Duration, LocalDate}
import answers.action.async._
import answers.action.fp.search.{Airport, Flight, SearchResult}

import scala.concurrent.ExecutionContext

trait SearchFlightService {
  def search(from: Airport, to: Airport, date: LocalDate): IO[SearchResult]
}

object SearchFlightService {

  def fromClients(clients: List[SearchFlightClient])(ec: ExecutionContext): SearchFlightService =
    new SearchFlightService {
      val wrappedClients = clients.map(wrappedClient(_)(ec))

      def search(from: Airport, to: Airport, date: LocalDate): IO[SearchResult] =
        wrappedClients
          .parTraverse(_.search(from, to, date))(ec)
          .map(_.flatten)
          .map(SearchResult(_))
    }

  def wrappedClient(client: SearchFlightClient)(ec: ExecutionContext): SearchFlightClient =
    new SearchFlightClient {
      def search(from: Airport, to: Airport, date: LocalDate): IO[List[Flight]] =
        client
          .search(from, to, date)
          .map(_.filter { flight =>
            flight.from == from && flight.to == to && flight.departureDate == date
          })
          .timeout(Duration.ofMillis(100))(ec)
          .handleErrorWith(_ => IO(Nil))
    }
}
