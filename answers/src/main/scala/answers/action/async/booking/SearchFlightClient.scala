package answers.action.async.booking

import java.time.LocalDate

import answers.action.async.IO
import answers.action.fp.booking._

trait SearchFlightClient {
  def search(from: Airport, to: Airport, date: LocalDate): IO[List[Flight]]
}

object SearchFlightClient {}
