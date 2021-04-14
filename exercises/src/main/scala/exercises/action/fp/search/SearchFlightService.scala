package exercises.action.fp.search

import java.time.LocalDate

import exercises.action.fp.IO

trait SearchFlightService {
  def search(from: Airport, to: Airport, date: LocalDate): IO[SearchResult]
}

object SearchFlightService {

  // 1. Implement `fromTwoClients` which creates a `SearchFlightService` by
  // combining the results from two `SearchFlightClient`.
  // For example, imagine we fetch flight data from Swissair and lastminute.com.
  //
  // A few things to consider:
  // a) The aggregated list of flights must be ordered using the "best" ordering
  //    (see `SearchResult` companion object).
  // b) Both clients may return data about the same flight in which case, we should only keep
  //    the flight with the lowest `unitPrice`.
  // c) A client may occasionally return flights which do not match the search criteria,
  //    for example, flights from London Heathrow while the search was for London Gatwick airport.
  //    It is the responsibility of `SearchFlightService` to return only valid flights.
  // d) A client may occasionally throw an exception. `SearchFlightService` should NOT propagate it.
  def fromTwoClients(client1: SearchFlightClient, client2: SearchFlightClient): SearchFlightService =
    ???

  def fromClients(clients: List[SearchFlightClient]): SearchFlightService =
    ???
}
