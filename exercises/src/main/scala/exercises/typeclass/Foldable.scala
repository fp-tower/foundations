package exercises.typeclass

trait Foldable[F[_]] {

  def foldLeft[A, B](fa: F[A], z: B)(f: (B, A) => B): B

  def foldRight[A, B](fa: F[A], z: B)(f: (A, => B) => B): B

  def foldMap[A, B](fa: F[A])(f: A => B)(implicit ev: Monoid[B]): B =
    foldLeft(fa, ev.empty)((acc, a) => ev.combine(f(a), acc))

}
