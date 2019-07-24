package exercises.sideeffect

class IO[A](sideEffect: () => A) {
  def unsafeRun(): A = sideEffect()
}

object IO {
  def apply[A](effect: => A): IO[A] =
    new IO(() => effect)

}
