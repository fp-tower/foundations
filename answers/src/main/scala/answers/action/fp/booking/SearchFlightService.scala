package answers.action.fp.booking

import java.time.LocalDate

import answers.action.fp.IO

trait SearchFlightService {
  def search(from: Airport, to: Airport, date: LocalDate): IO[SearchResult]
}

object SearchFlightService {

  def fromTwoClients(client1: SearchFlightClient, client2: SearchFlightClient): SearchFlightService =
    new SearchFlightService {
      def search(from: Airport, to: Airport, date: LocalDate): IO[SearchResult] =
        for {
          flights1 <- client1.search(from, to, date)
          flights2 <- client2.search(from, to, date)
        } yield combineFlightResults(List(flights1, flights2), flightPredicate(from, to, date))
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
    SearchResult.fromList(flightResults.flatten.filter(predicate))
}
