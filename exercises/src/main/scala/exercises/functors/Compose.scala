package exercises.functors

import cats.Eq

case class Compose[F[_], G[_], A](getCompose: F[G[A]])

object Compose {
  implicit def eq[F[_], G[_], A](implicit ev: Eq[F[G[A]]]): Eq[Compose[F, G, A]] =
    Eq.by(_.getCompose)
}