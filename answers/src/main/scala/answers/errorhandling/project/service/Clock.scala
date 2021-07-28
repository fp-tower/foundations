package answers.errorhandling.project.service

import answers.action.async.IO

import java.time.Instant

trait Clock {
  def now: IO[Instant]
}

object Clock {
  val system: Clock = new Clock {
    def now: IO[Instant] = IO(Instant.now())
  }

  def constant(instant: Instant): Clock = new Clock {
    def now: IO[Instant] = IO(instant)
  }
}
