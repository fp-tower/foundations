package exercises.action.fp.search

import java.time.LocalDate

import exercises.action.fp.IO

trait SearchFlightService {
  def search(from: Airport, to: Airport, date: LocalDate): IO[SearchResult]
}

object SearchFlightService {

  // 1. Implement `fromTwoClients` which creates a `SearchFlightService` by
  // combining the results from two `SearchFlightClient`.
  // For example, imagine we combine the results from British Airways and Swissair.
  def fromTwoClients(client1: SearchFlightClient, client2: SearchFlightClient): SearchFlightService =
    ???

  def fromClients(clients: List[SearchFlightClient]): SearchFlightService =
    ???
}
