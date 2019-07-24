package exercises.sideeffect

trait NaturalTransformation[F[_], G[_]] {
  def apply[A](fa: F[A]): G[A]
}
