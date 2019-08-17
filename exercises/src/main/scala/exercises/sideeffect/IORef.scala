package exercises.sideeffect

import java.util.concurrent.atomic.AtomicReference

import answers.sideeffect.IOAnswers.IO

import scala.annotation.tailrec

case class IORef[A](ref: AtomicReference[A]) {
  def get: IO[A]                 = IO.effect(ref.get())
  def set(newValue: A): IO[Unit] = IO.effect(ref.set(newValue))

  // copied from cats-effect
  def modify[B](f: A => (A, B)): IO[B] = {
    @tailrec
    def spin: B = {
      val c      = ref.get
      val (u, b) = f(c)
      if (!ref.compareAndSet(c, u)) spin
      else b
    }
    IO.effect(spin)
  }

  // copied from cats-effect
  def update(f: A => A): IO[Unit] =
    modify(a => (f(a), ()))
}

object IORef {
  def apply[A](value: A): IORef[A] = IORef(new AtomicReference(value))
}
