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
  // Note: A example based test is defined in `SearchFlightServiceTest`.
  //       You can also defined tests for `SearchResult` in `SearchResultTest`
  def fromTwoClients(client1: SearchFlightClient, client2: SearchFlightClient): SearchFlightService =
    ???

  // 2. Several clients can return data for the same flight. For example, if we combine data
  // from British Airways and lastminute.com, lastminute.com may include flights from British Airways.
  // Update `fromTwoClients` so that if we get two or more flights with the same `flightId`,
  // `SearchFlightService` selects the flight with the lowest `unitPrice` and discards the other ones.

  // 3. Clients may occasionally return invalid data. For example, one may returns flights from
  // London Heathrow airport while the search was for London Gatwick airport.
  // Update `fromTwoClients` so that `SearchFlightService` only returns flights that satisfies the
  // search criteria.

  // 4. Can you think of other scenarios we should consider in `fromTwoClients`?
  // Try to write a test for each scenario before updating `fromTwoClients`.
  // Note: Some scenarios are extremely difficult to test and fix.
  //       It is already great if you can think of the issue!

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
  //
  // a) A client may occasionally throw an exception. `SearchFlightService` should
  //    handle the error gracefully, for example log a message and ignore the error.
  //
  // b) Each client's search request is executed sequentially - one after another.
  //    Here are the current execution steps of `fromTwoClients`
  //    1. send search request to client 1
  //    2. receive list of flights from client 1
  //    3. send search request to client 2
  //    4. receive list of flights from client 2
  //    5. aggregate results from client 1 and 2
  //    Currently, we only send the request to client 2 after we have received the response from client 1.
  //    Instead, it would be better to send both search requests concurrently:
  //    1. send search request to client 1
  //    2. send search request to client 2
  //    3. receive list of flights from client 1
  //    4. receive list of flights from client 2
  //    5. aggregate results from client 1 and 2
  //
  // c) A client may be extremely slow. `fromTwoClients` should define a timeout
  //    so that it doesn't wait more than 2 seconds per client.

  // 5. Implement `fromClients` which behaves like `fromTwoClients` but for
  //    an unknown number of `SearchFlightClient`.
  def fromClients(clients: List[SearchFlightClient]): SearchFlightService =
    ???
}
