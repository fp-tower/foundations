package exercises.functors

import exercises.typeclass.Eq

case class Compose[F[_], G[_], A](getCompose: F[G[A]])

object Compose {
  implicit def eq[F[_], G[_], A](implicit ev: Eq[F[G[A]]]): Eq[Compose[F, G, A]] =
    Eq.by(_.getCompose)
}