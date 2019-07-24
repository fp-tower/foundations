package exercises.sideeffect

class IO[A](private val sideEffect: () => A) {
  def unsafeRun(): A = sideEffect()
}

object IO {
  def apply[A](effect: => A): IO[A] =
    new IO(() => effect)
}
