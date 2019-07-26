package exercises.sideeffect

class IO[A](sideEffect: () => A) {
  def unsafeRun(): A = sideEffect()

  def map[B](f: A => B): IO[B] =
    new IO(() => f(unsafeRun()))

  def flatMap[B](f: A => IO[B]): IO[B] =
    new IO(() => f(unsafeRun()).unsafeRun())
}

object IO {
  def apply[A](effect: => A): IO[A] =
    new IO(() => effect)

}
