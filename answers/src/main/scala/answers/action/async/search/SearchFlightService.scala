package answers.action.async.search

import java.time.LocalDate

import answers.action.async.{IO, ListExtension}
import answers.action.fp.search.{Airport, Flight, SearchResult}

import scala.concurrent.ExecutionContext

trait SearchFlightService {
  def search(from: Airport, to: Airport, date: LocalDate): IO[SearchResult]
}

object SearchFlightService {

  def fromPartners(partners: List[Partner], ec: ExecutionContext): SearchFlightService =
    new SearchFlightService {
      def search(from: Airport, to: Airport, date: LocalDate): IO[SearchResult] = {
        def fetchFlights(partner: Partner): IO[List[Flight]] =
          partner.client
            .search(from, to, date)
            .map(_.filter { flight =>
              flight.from == from && flight.to == to && flight.departureDate == date
            })
            .handleErrorWith(_ => IO(Nil))
            .timeout(partner.timeout)(ec)

        partners
          .parTraverse(fetchFlights)(ec)
          .map(_.flatten)
          .map(SearchResult(_))
      }
    }
}
