package exercises.functors

trait Monad[F[_]] extends Applicative[F] {
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

  // Derived functions (to implement in exercises)
  def flatten[A](ffa: F[F[A]]): F[A]

  def flatTap[A, B](fa: F[A])(f: A => F[B]): F[A]

  def ifM[A](cond: F[Boolean])(ifTrue: => F[A], ifFalse: => F[A]): F[A]

}

object Monad {
  def apply[F[_]](implicit ev: Monad[F]): Monad[F] = ev

  object syntax {
    implicit class MonadOps[F[_], A](fa: F[A]){
      def flatMap[B](f: A => F[B])(implicit ev: Monad[F]): F[B] = ev.flatMap(fa)(f)

      def flatTap[B](f: A => F[B])(implicit ev: Monad[F]): F[A] =
        ev.flatTap(fa)(f)
    }

    implicit class MonadOps2[F[_], A](fa: F[F[A]]){
      def flatten(implicit ev: Monad[F]): F[A] = ev.flatten(fa)
    }

    implicit class MonadOps3[F[_], A](cond: F[Boolean]){
      def ifM(ifTrue: => F[A], ifFalse: => F[A])(implicit ev: Monad[F]): F[A] =
        ev.ifM(cond)(ifTrue, ifFalse)
    }
  }
}