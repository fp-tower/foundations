package answers.action.async.search

import java.time.LocalDate

import answers.action.async.IO
import answers.action.fp.search._

trait SearchFlightClient {
  def search(from: Airport, to: Airport, date: LocalDate): IO[List[Flight]]
}

object SearchFlightClient {}
