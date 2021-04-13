package exercises.action.fp.booking

import java.time.LocalDate

import exercises.action.fp.IO

trait SearchFlightClient {
  def search(from: Airport, to: Airport, date: LocalDate): IO[List[Flight]]
}
