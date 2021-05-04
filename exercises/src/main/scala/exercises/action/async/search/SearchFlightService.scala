package exercises.action.async.search

import java.time.LocalDate
import exercises.action.async.IO
import exercises.action.fp.search.{Airport, SearchResult}

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
  // A few points to consider:
  // a) The two requests to the clients must be executed concurrently.
  // b) A client may occasionally return invalid data.
  //    The service should remove all flights that do not match the search criteria.
  // c) A client may occasionally throw an exception.
  //    The service should ignore the error and consider this client returned an empty List.
  // d) A client may be slow.
  //    The service should put a timeout so that it doesn't wait more than 100 milliseconds for each client.
  def fromTwoClients(client1: SearchFlightClient, client2: SearchFlightClient): SearchFlightService =
    new SearchFlightService {
      def search(from: Airport, to: Airport, date: LocalDate): IO[SearchResult] =
        for {
          result1 <- client1.search(from, to, date)
          result2 <- client1.search(from, to, date)
        } yield SearchResult(result1 ++ result2)
    }

  // 2. Implement `fromClients` which behaves like `fromTwoClients` but for
  // an unknown number of `SearchFlightClient`.
  // Note: Check the methods `sequence` and `traverse` in the IO companion object.
  def fromClients(clients: List[SearchFlightClient]): SearchFlightService =
    ???
}
