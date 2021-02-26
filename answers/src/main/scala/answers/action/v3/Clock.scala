package answers.action.v3

import java.time.LocalDate

import answers.action.v3.LazyAction.delay

trait Clock {
  def readToday: LazyAction[LocalDate]
}

object Clock {
  val system: Clock = new Clock {
    val readToday: LazyAction[LocalDate] =
      delay(LocalDate.now())
  }

  def constant(date: LocalDate): Clock = new Clock {
    val readToday: LazyAction[LocalDate] =
      delay(date)
  }
}
