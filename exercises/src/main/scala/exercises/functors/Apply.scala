package exercises.functors

trait Apply[F[_]] extends Functor[F] {
  def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C]

  def tuple2[A, B](fa: F[A], fb: F[B]): F[(A, B)] = map2(fa, fb)((_, _))
}

object Apply {
  def apply[F[_]](implicit ev: Apply[F]): Apply[F] = ev
}
