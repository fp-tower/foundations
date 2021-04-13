package exercises.action.fp.booking

import java.time.LocalDate

import exercises.action.fp.IO

trait SearchFlightService {
  def search(from: Airport, to: Airport, date: LocalDate): IO[SearchResult]
}

object SearchFlightService {

  // 1. Implement `fromTwoClients` which creates a `SearchFlightService` by
  // querying two `SearchFlightClient` and aggregating their results.
  def fromTwoClients(client1: SearchFlightClient, client2: SearchFlightClient): SearchFlightService =
    ???

  def fromClients(clients: List[SearchFlightClient]): SearchFlightService =
    ???
}
