package exercises.action.fp.console

import java.time.Instant

import exercises.action.fp.IO

trait Clock {
  def now: IO[Instant]
}

object Clock {
  val system: Clock = new Clock {
    val now: IO[Instant] =
      IO(Instant.now())
  }

  def constant(instant: Instant): Clock = new Clock {
    val now: IO[Instant] = IO(instant)
  }
}
