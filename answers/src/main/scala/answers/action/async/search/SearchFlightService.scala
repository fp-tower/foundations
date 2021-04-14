package answers.action.async.search

import answers.action.async.{IO, ListExtension}
import answers.action.fp.search.{Airport, Flight, SearchResult}

import scala.concurrent.ExecutionContext
import java.time.LocalDate

trait SearchFlightService {
  def search(from: Airport, to: Airport, date: LocalDate): IO[SearchResult]
}

object SearchFlightService {

  def fromPartners(partners: List[Partner], ec: ExecutionContext): SearchFlightService =
    new SearchFlightService {
      def search(from: Airport, to: Airport, date: LocalDate): IO[SearchResult] =
        partners
          .parTraverse { partner =>
            partner.client
              .search(from, to, date)
              .timeout(partner.timeout)(ec)
              .handleErrorWith(_ => IO(Nil))
          }(ec)
          .map(combineFlightResults(_, flightPredicate(from, to, date)))
    }

  def fromClients(clients: List[SearchFlightClient]): SearchFlightService =
    new SearchFlightService {
      def search(from: Airport, to: Airport, date: LocalDate): IO[SearchResult] =
        clients
          .traverse { client =>
            client
              .search(from, to, date)
              .handleErrorWith(_ => IO(Nil))
          }
          .map(combineFlightResults(_, flightPredicate(from, to, date)))
    }

  def flightPredicate(from: Airport, to: Airport, date: LocalDate): Flight => Boolean =
    (flight: Flight) => {
      flight.from == from &&
      flight.to == to &&
      flight.departureDate == date
    }

  def combineFlightResults(
    flightResults: List[List[Flight]],
    predicate: Flight => Boolean,
  ): SearchResult =
    SearchResult.validate(flightResults.flatten.filter(predicate))
}
