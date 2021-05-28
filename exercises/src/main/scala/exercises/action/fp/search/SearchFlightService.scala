package exercises.action.fp.search

import java.time.LocalDate
import exercises.action.fp.IO

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

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
    new SearchFlightService {
      def search(from: Airport, to: Airport, date: LocalDate): IO[SearchResult] =
        ???

    }

  // 2. Several clients can return data for the same flight. For example, if we combine data
  // from British Airways and lastminute.com, lastminute.com may include flights from British Airways.
  // Update `fromTwoClients` so that if we get two or more flights with the same `flightId`,
  // `SearchFlightService` selects the flight with the lowest `unitPrice` and discards the other ones.

  // 3. A client may occasionally throw an exception. `fromTwoClients` should
  // handle the error gracefully, for example log a message and ignore the error.
  // In other words, `fromTwoClients` should consider that a client which throws an exception
  // is the same as a client which returns an empty list.

  // 4. Implement `fromClients` which behaves like `fromTwoClients` but it takes
  // a list of `SearchFlightClient`.
  // Note: You can use a recursion/loop/foldLeft to call all the clients and combine their results.
  // Note: We can assume `clients` to contain less than 100 elements.
  def fromClients(clients: List[SearchFlightClient]): SearchFlightService =
    new SearchFlightService {
      def search(from: Airport, to: Airport, date: LocalDate): IO[SearchResult] =
        ???
    }

  // 5. Refactor `fromClients` using `sequence` or `traverse` from the `IO` companion object.

  //////////////////////////////////////////////
  // Concurrent IO
  //////////////////////////////////////////////

  // 6. Each client's search request is executed sequentially - one after another.
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

  //////////////////////////////////////////////
  // Bonus question (not covered by the videos)
  //////////////////////////////////////////////

  // 10. `fromClients` wait for the results from every single client. This means that
  // if one client is extremely slow, it will slow down the overall request.
  // Implement a timeout per client so that the service doesn't spend more than
  // 500 milliseconds per client.
  // Note: Move `timeout` to the IO class once it is implemented.
  def timeout[A](io: IO[A], duration: FiniteDuration)(ec: ExecutionContext): IO[A] =
    ???

  // 11. Clients may occasionally return invalid data. For example, one may returns flights for
  // London Heathrow airport while the search was for London Gatwick airport.
  // Update `fromClients` so that `SearchFlightService` only returns flights that satisfies the
  // search criteria.

}
