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
  // a) A client may occasionally return flights which do not match the search criteria,
  //    for example, flights from London Heathrow airport while the search was for London Gatwick.
  //    It is the responsibility of `SearchFlightService` to return only valid flights.
  // b) The aggregated list of flights must be ordered using the "best" ordering strategy
  //    (see `SearchResult` companion object).
  // c) Both clients may return data about the same flight in which case, we should only keep
  //    the flight with the lowest `unitPrice`.
  // Note: A few tests are already defined in `SearchFlightServiceTest` and `SearchResultTest`.
  def fromTwoClients(client1: SearchFlightClient, client2: SearchFlightClient): SearchFlightService =
    ???

  // 2. Can you think of other scenarios we should consider in `fromTwoClients`?
  //    Try to write a test for each scenario before refactoring `fromTwoClients`.

  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //                                          //
  //                SPOILER                   //
  //                                          //
  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////
  //////////////////////////////////////////////
  // Here are some examples:
  // a) A client may occasionally throw an exception. `SearchFlightService` should
  //    handle the error gracefully, for example log a message or ignore the error.
  // b) A client may be really slow. We should use a timeout when fetching data
  //    from a client.
  //    Note: Implementing a timeout on `IO` is too difficult now. We'll look into
  //    this in the bonus exercises.

  // 3. Implement `fromClients` which behaves like `fromTwoClients` but for
  //    an unknown number of `SearchFlightClient`.
  def fromClients(clients: List[SearchFlightClient]): SearchFlightService =
    ???
}
