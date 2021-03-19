package exercises.action.fp

import java.time.Instant

trait Clock {
  def now: Action[Instant]
}

object Clock {
  val system: Clock = new Clock {
    val now: Action[Instant] =
      Action(Instant.now())
  }

  def constant(instant: Instant): Clock = new Clock {
    val now: Action[Instant] = Action(instant)
  }
}
