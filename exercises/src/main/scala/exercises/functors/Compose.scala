package exercises.functors

case class Compose[F[_], G[_], A](getCompose: F[G[A]])