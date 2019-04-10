package exercises.typeclass2

trait Apply[F[_]] extends Functor[F] {
  def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C]
}

object Apply {
  def apply[F[_]](implicit ev: Apply[F]): Apply[F] = ev
}