package answers.action.async.booking

import java.time.LocalDate

import answers.action.async.{IO, ListExtension}
import answers.action.fp.booking.FlightPredicate.BasicSearch
import answers.action.fp.booking.{Airport, Flight, FlightPredicate}

import scala.concurrent.ExecutionContext

trait SearchFlightService {
  def search(from: Airport, to: Airport, date: LocalDate, predicate: FlightPredicate): IO[List[Flight]]
}

object SearchFlightService {

  def fromPartners(partners: List[Partner], ec: ExecutionContext): SearchFlightService =
    new SearchFlightService {
      def search(from: Airport, to: Airport, date: LocalDate, predicate: FlightPredicate): IO[List[Flight]] =
        partners
          .parTraverse { partner =>
            partner.client
              .search(from, to, date)
              .timeout(partner.timeout)(ec)
              .handleErrorWith(_ => IO(Nil))
          }(ec)
          .map(combineFlightResults(_, predicate && BasicSearch(from, to, date)))
    }

  def combineFlightResults(flightResults: List[List[Flight]], predicate: FlightPredicate): List[Flight] =
    flightResults.flatten
      .filter(predicate.isValid)
      .groupBy(_.flightId)
      .map { case (_, values) => values.minBy(_.cost) }
      .toList
      .sortBy(_.cost)
}
