package exercises.action.fp.search

import java.time.LocalDate

import exercises.action.fp.IO

trait SearchFlightClient {
  def search(from: Airport, to: Airport, date: LocalDate): IO[List[Flight]]
}
