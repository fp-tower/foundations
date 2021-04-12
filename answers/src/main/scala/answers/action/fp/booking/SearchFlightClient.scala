package answers.action.fp.booking

import java.time.LocalDate

import answers.action.fp.IO

trait SearchFlightClient {
  def search(from: Airport, to: Airport, date: LocalDate): IO[List[Flight]]
}
