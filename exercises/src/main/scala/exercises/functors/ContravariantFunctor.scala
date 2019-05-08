package exercises.functors

trait ContravariantFunctor[F[_]] extends InvariantFunctor[F] {
  def contramap[A, B](fa: F[A])(f: B => A): F[B]

  def imap[A, B](fa: F[A])(f: A => B)(g: B => A): F[B] =
    contramap(fa)(g)
}

object ContravariantFunctor {
  def apply[F[_]](implicit ev: ContravariantFunctor[F]): ContravariantFunctor[F] = ev

  object syntax {
    implicit class ContravariantFunctorOps[F[_], A](fa: F[A]) {
      def contramap[B](f: B => A)(implicit ev: ContravariantFunctor[F]): F[B] =
        ev.contramap(fa)(f)
    }
  }
}
