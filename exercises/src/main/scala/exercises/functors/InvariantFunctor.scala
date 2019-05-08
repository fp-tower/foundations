package exercises.functors

trait InvariantFunctor[F[_]] {
  def imap[A, B](fa: F[A])(f: A => B)(g: B => A): F[B]
}

object InvariantFunctor {
  def apply[F[_]](implicit ev: InvariantFunctor[F]): InvariantFunctor[F] = ev

  object syntax {
    implicit class InvariantFunctorOps[F[_], A](fa: F[A]) {
      def imap[B](f: A => B)(g: B => A)(implicit ev: InvariantFunctor[F]): F[B] =
        ev.imap(fa)(f)(g)
    }
  }
}
