package exercises.functors

trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}

trait Applicative[F[_]] extends Functor[F] {
  def pure[A](a: A): F[A]
  def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C]
}

trait Monad[F[_]] extends Applicative[F] {
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
}


object Functor {
  def apply[F[_]](implicit ev: Functor[F]): Functor[F] = ev

  object syntax {
    implicit class FunctorOps[F[_], A](fa: F[A]){
      def map[B](f: A => B)(implicit ev: Functor[F]): F[B] = ev.map(fa)(f)
    }
  }
}

object Applicative {
  def apply[F[_]](implicit ev: Applicative[F]): Applicative[F] = ev

  object syntax {
    implicit class ApplicativeOps[F[_], A](fa: F[A]){
      def map2[B, C](fb: F[B])(f: (A, B) => C)(implicit ev: Applicative[F]): F[C] = ev.map2(fa, fb)(f)
    }

    implicit class ApplicativeOps2[A](a: A){
      def pure[F[_]](implicit ev: Applicative[F]): F[A] = ev.pure(a)
    }
  }
}

object Monad {
  def apply[F[_]](implicit ev: Monad[F]): Monad[F] = ev

  object syntax {
    implicit class MonadOps[F[_], A](fa: F[A]){
      def flatMap[B](f: A => F[B])(implicit ev: Monad[F]): F[B] = ev.flatMap(fa)(f)
    }
  }
}