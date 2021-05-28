package exercises.action.fp.search

import java.time.LocalDate

import exercises.action.fp.IO

// This interface represents the API used to contact a flight data provider such as:
// British Airways, lastminute.com or Swissair
//
// We assume this API is standard and all data providers use the same interface.
//
// For example, a real implementation of `SearchFlightClient` could use an HTTP client.
trait SearchFlightClient {
  def search(from: Airport, to: Airport, date: LocalDate): IO[List[Flight]]
}

object SearchFlightClient {

  // test client which executes the same action for all requests
  def constant(flights: IO[List[Flight]]): SearchFlightClient =
    new SearchFlightClient {
      def search(from: Airport, to: Airport, date: LocalDate): IO[List[Flight]] =
        flights
    }
}
