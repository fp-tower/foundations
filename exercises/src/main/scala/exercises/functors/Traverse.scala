package exercises.functors

import exercises.typeclass.Foldable

trait Traverse[F[_]] extends Functor[F] with Foldable[F] {

  def traverse[G[_]: Applicative, A, B](fa: F[A])(f: A => G[B]): G[F[B]]

  def sequence[G[_]: Applicative, A](fga: F[G[A]]): G[F[A]]

}

object Traverse {
  def apply[F[_]](implicit ev: Traverse[F]): Traverse[F] = ev

  object syntax {
    implicit class TraverseOps[F[_], A](fa: F[A]) {
      def traverse[G[_]: Applicative, B](f: A => G[B])(implicit ev: Traverse[F]): G[F[B]] = ev.traverse(fa)(f)
    }

    implicit class TraverseOps2[F[_], G[_], A](fa: F[G[A]]) {
      def sequence(implicit evT: Traverse[F], evA: Applicative[G]): G[F[A]] = evT.sequence(fa)
    }
  }
}
