package answers.action.async.search

import answers.action.async.IO
import answers.action.fp.search.{Airport, Flight}

import java.time.LocalDate

trait SearchFlightClient {
  def search(from: Airport, to: Airport, date: LocalDate): IO[List[Flight]]
}

object SearchFlightClient {

  def constant(flights: IO[List[Flight]]): SearchFlightClient =
    new SearchFlightClient {
      def search(from: Airport, to: Airport, date: LocalDate): IO[List[Flight]] =
        flights
    }
}
