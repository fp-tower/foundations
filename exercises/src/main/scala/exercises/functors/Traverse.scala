package exercises.functors

import exercises.typeclass.{Foldable, Monoid}

trait Traverse[F[_]] extends Functor[F] with Foldable[F] {

  def traverse[G[_]: Applicative, A, B](fa: F[A])(f: A => G[B]): G[F[B]]

  def sequence[G[_]: Applicative, A](fga: F[G[A]]): G[F[A]]

  def traverse_[G[_]: Applicative, A, B](fa: F[A])(f: A => G[B]): G[Unit]

  def foldMapM[G[_]: Applicative, A, B: Monoid](fa: F[A])(f: A => G[B]): G[B]

  def flatSequence[G[_]: Applicative, A](fgfa: F[G[F[A]]])(implicit ev: Monad[F]): G[F[A]]

  def flatTraverse[G[_]: Applicative, A, B](fa: F[A])(f: A => G[F[B]])(implicit ev: Monad[F]): G[F[B]]

}

object Traverse {
  def apply[F[_]](implicit ev: Traverse[F]): Traverse[F] = ev

  object syntax {
    implicit class TraverseOps[F[_], A](fa: F[A]) {
      def traverse[G[_]: Applicative, B](f: A => G[B])(implicit ev: Traverse[F]): G[F[B]] =
        ev.traverse(fa)(f)

      def traverse_[G[_]: Applicative, B](f: A => G[B])(implicit ev: Traverse[F]): G[Unit] =
        ev.traverse_(fa)(f)

      def foldMapM[G[_]: Applicative, B: Monoid](f: A => G[B])(implicit ev: Traverse[F]): G[B] =
        ev.foldMapM(fa)(f)

      def flatTraverse[G[_]: Applicative, B](f: A => G[F[B]])(implicit evT: Traverse[F], evM: Monad[F]): G[F[B]] =
        evT.flatTraverse(fa)(f)
    }

    implicit class TraverseOps2[F[_], G[_], A](fa: F[G[A]]) {
      def sequence(implicit evT: Traverse[F], evA: Applicative[G]): G[F[A]] = evT.sequence(fa)
    }
  }
}
