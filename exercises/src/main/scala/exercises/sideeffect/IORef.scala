package exercises.sideeffect

import java.util.concurrent.atomic.AtomicReference

case class IORef[A](private val ref: AtomicReference[A]) {
  def get: IO[A] = IO.effect(ref.get())
  def set(newValue: A): IO[Unit] = IO.effect(ref.set(newValue))
  def modify(f: A => A): IO[Unit] = ???
}

object IORef {
  def apply[A](value: A): IORef[A] = IORef(new AtomicReference(value))
}
