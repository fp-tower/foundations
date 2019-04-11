package exercises.typeclass2

case class Compose[F[_], G[_], A](getCompose: F[G[A]])