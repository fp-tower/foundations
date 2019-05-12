package exercises.functors

trait FlatMap[F[_]] extends Apply[F] {
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

  def flatten[A](ffa: F[F[A]]): F[A] =
    flatMap(ffa)(identity)

  def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] =
    flatMap(fa)(a => map(fb)(f(a, _)))
}

object FlatMap {
  def apply[F[_]](implicit ev: FlatMap[F]): FlatMap[F] = ev
}
