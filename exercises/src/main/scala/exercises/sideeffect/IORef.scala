package exercises.sideeffect

import java.util.concurrent.atomic.AtomicReference

case class IORef[A](private val ref: AtomicReference[A]) {
  def get: IO[A]                 = new IO(() => ref.get())
  def set(newValue: A): IO[Unit] = new IO(() => ref.set(newValue))

  def modify(f: A => A): IO[A] =
    modifyFold(a => { val newA = f(a); (newA, newA) })

  // copied from https://github.com/scalaz/ioeffect
  def modifyFold[B](f: A => (B, A)): IO[B] =
    new IO(() => {
      var loop = true
      var b: B = null.asInstanceOf[B]

      while (loop) {
        val current   = ref.get
        val (b, newA) = f(current)
        loop = !ref.compareAndSet(current, newA)
      }
      b
    })
}

object IORef {
  def apply[A](value: A): IORef[A] = IORef(new AtomicReference(value))
}
