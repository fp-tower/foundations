package exercises.action.fp.search

import java.time.LocalDate

import exercises.action.fp.IO

// This represent the main API of Lambda Corp.
// `search` is called whenever a user press the "Search" button on the website.
trait SearchFlightService {
  def search(from: Airport, to: Airport, date: LocalDate): IO[SearchResult]
}

object SearchFlightService {

  // 1. Implement `fromTwoClients` which creates a `SearchFlightService` by
  // combining the results from two `SearchFlightClient`.
  // For example, imagine we fetch flight data from Swissair and lastminute.com.
  //
  // Please order the aggregated flights using the "best" ordering strategy
  // (see `SearchResult` companion object).
  // Note: A few tests are already defined in `SearchFlightServiceTest` and `SearchResultTest`.
  def fromTwoClients(client1: SearchFlightClient, client2: SearchFlightClient): SearchFlightService =
    ???

  // 2. Clients may returns data for the same flight. For example, if we combine data from British Airways
  // and latminute.com, the latter may include British Airways flights.
  // Update `fromTwoClients` so that if we get two or more flights with the same `flightId`, we should
  // select the flight with the lowest `unitPrice` anf discard the other ones.

  // 3. Clients may occasionally return invalid data. For example, one may returns flights from
  // London Heathrow airport while the search was for London Gatwick.
  // Update `fromTwoClients` so that `SearchFlightService` only returns flights that satisfies the
  // search criteria.

  // 4. Can you think of other scenarios we should consider in `fromTwoClients`?
  // Try to write a test for each scenario before updating `fromTwoClients`.

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

  // 5. Implement `fromClients` which behaves like `fromTwoClients` but for
  //    an unknown number of `SearchFlightClient`.
  def fromClients(clients: List[SearchFlightClient]): SearchFlightService =
    ???
}
