package answers.sideeffect

import java.util.concurrent.atomic.AtomicReference

import scala.annotation.tailrec

case class IOAsyncRef[A](ref: AtomicReference[A]) {
  def get: IOAsync[A]                 = IOAsync.effect(ref.get())
  def set(newValue: A): IOAsync[Unit] = IOAsync.effect(ref.set(newValue))

  // copied from cats-effect
  def modify[B](f: A => (A, B)): IOAsync[B] = {
    @tailrec
    def spin: B = {
      val c      = ref.get
      val (u, b) = f(c)
      if (!ref.compareAndSet(c, u)) spin
      else b
    }
    IOAsync.effect(spin)
  }

  // copied from cats-effect
  def update(f: A => A): IOAsync[Unit] =
    modify(a => (f(a), ()))

  def updateGetNew(f: A => A): IOAsync[A] =
    modify { a =>
      val newValue = f(a)
      (newValue, newValue)
    }
}

object IOAsyncRef {
  def apply[A](value: A): IOAsync[IOAsyncRef[A]] =
    IOAsync.effect(IOAsyncRef(new AtomicReference(value)))

  def unsafe[A](value: A): IOAsyncRef[A] =
    IOAsyncRef(new AtomicReference(value))
}
