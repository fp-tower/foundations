package answers.dataprocessing

import java.util.concurrent.atomic.AtomicReference

import scala.annotation.tailrec

case class Ref[A](underlying: AtomicReference[A]) {
  def get: A = underlying.get

  @tailrec
  final def modify(update: A => A): A = {
    val oldValue = underlying.get
    val newValue = update(oldValue)
    if (!underlying.compareAndSet(oldValue, newValue)) modify(update)
    else newValue
  }
}

object Ref {
  def apply[A](value: A): Ref[A] =
    new Ref(new AtomicReference(value))

  def empty[A]: Ref[A] =
    new Ref(new AtomicReference())
}
