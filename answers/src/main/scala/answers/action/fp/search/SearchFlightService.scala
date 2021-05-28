package answers.action.fp.search

import java.time.LocalDate

import answers.action.fp.IO

trait SearchFlightService {
  def search(from: Airport, to: Airport, date: LocalDate): IO[SearchResult]
}

object SearchFlightService {

  def fromTwoClients(client1: SearchFlightClient, client2: SearchFlightClient): SearchFlightService =
    fromClients(List(client1, client2))

  def fromClients(clients: List[SearchFlightClient]): SearchFlightService =
    new SearchFlightService {
      def search(from: Airport, to: Airport, date: LocalDate): IO[SearchResult] = {
        def fetchFlights(client: SearchFlightClient): IO[List[Flight]] =
          client
            .search(from, to, date)
            .handleErrorWith(_ => IO(Nil))
            .map(_.filter { flight =>
              flight.from == from && flight.to == to && flight.departureDate == date
            })

        clients
          .traverse(fetchFlights)
          .map(_.flatten)
          .map(SearchResult.apply)
      }
    }
}
