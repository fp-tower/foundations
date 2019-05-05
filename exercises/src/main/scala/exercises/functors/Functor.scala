package exercises.functors

trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]

  // Derived functions (to implement in exercises)
  def void[A](fa: F[A]): F[Unit]

  def as[A, B](fa: F[A])(value: B): F[B]

  def widen[A, B >: A](fa: F[A]): F[B]

  def tupleLeft [A, B](fa: F[A])(value: B): F[(B, A)]
  def tupleRight[A, B](fa: F[A])(value: B): F[(A, B)]
}


object Functor {
  def apply[F[_]](implicit ev: Functor[F]): Functor[F] = ev

  object syntax {
    implicit class FunctorOps[F[_], A](fa: F[A]){
      def map[B](f: A => B)(implicit ev: Functor[F]): F[B] = ev.map(fa)(f)
      def void(implicit ev: Functor[F]): F[Unit] = ev.void(fa)
      def as[B](value: B)(implicit ev: Functor[F]): F[B] = ev.as(fa)(value)
      def widen[B >: A](implicit ev: Functor[F]): F[B] = ev.widen(fa)
      def tupleLeft[B](value: B)(implicit ev: Functor[F]): F[(B, A)] = ev.tupleLeft(fa)(value)
      def tupleRight[B](value: B)(implicit ev: Functor[F]): F[(A, B)] = ev.tupleRight(fa)(value)
    }
  }
}
