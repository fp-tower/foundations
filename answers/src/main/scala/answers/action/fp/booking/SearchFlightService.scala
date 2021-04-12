package answers.action.fp.booking

import java.time.LocalDate

import answers.action.fp.IO

trait SearchFlightService {
  def search(from: Airport, to: Airport, date: LocalDate, predicate: FlightPredicate): IO[List[Flight]]
}

object SearchFlightService {

  def fromTwoClients(client1: SearchFlightClient, client2: SearchFlightClient): SearchFlightService =
    new SearchFlightService {
      def search(from: Airport, to: Airport, date: LocalDate, predicate: FlightPredicate): IO[List[Flight]] =
        for {
          flights1 <- client1.search(from, to, date)
          flights2 <- client2.search(from, to, date)
        } yield combineFlightResults(List(flights1, flights2), predicate)
    }

  def fromClients(clients: List[SearchFlightClient]): SearchFlightService =
    new SearchFlightService {
      def search(from: Airport, to: Airport, date: LocalDate, predicate: FlightPredicate): IO[List[Flight]] =
        clients
          .traverse(_.search(from, to, date))
          .map(combineFlightResults(_, predicate))
    }

  def combineFlightResults(flightResults: List[List[Flight]], predicate: FlightPredicate): List[Flight] =
    flightResults.flatten
      .filter(predicate.isValid)
      .groupBy(_.flightId)
      .map { case (_, values) => values.minBy(_.cost) }
      .toList
      .sortBy(_.cost)
}
