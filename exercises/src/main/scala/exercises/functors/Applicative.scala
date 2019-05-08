package exercises.functors

trait Applicative[F[_]] extends Functor[F] {
  def pure[A](a: A): F[A]
  def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C]

  // Derived functions (to implement in exercises)
  def map3[A, B, C, D](fa: F[A], fb: F[B], fc: F[C])(f: (A, B, C) => D): F[D]

  def tuple2[A, B](fa: F[A], fb: F[B]): F[(A, B)]

  def productL[A, B](fa: F[A], fb: F[B]): F[A]
  def productR[A, B](fa: F[A], fb: F[B]): F[B]

  def unit: F[Unit]
}

object Applicative {
  def apply[F[_]](implicit ev: Applicative[F]): Applicative[F] = ev

  object syntax {
    implicit class ApplicativeOps[F[_], A](fa: F[A]) {
      def map2[B, C](fb: F[B])(f: (A, B) => C)(implicit ev: Applicative[F]): F[C] =
        ev.map2(fa, fb)(f)

      def map3[B, C, D](fb: F[B], fc: F[C])(f: (A, B, C) => D)(implicit ev: Applicative[F]): F[D] =
        ev.map3(fa, fb, fc)(f)

      def tuple2[B](fb: F[B])(implicit ev: Applicative[F]): F[(A, B)] =
        ev.tuple2(fa, fb)

      def productL[B](fb: F[B])(implicit ev: Applicative[F]): F[A] =
        ev.productL(fa, fb)

      def productR[B](fb: F[B])(implicit ev: Applicative[F]): F[B] =
        ev.productR(fa, fb)

      def <*[B](fb: F[B])(implicit ev: Applicative[F]): F[A] =
        productL(fb)

      def *>[B](fb: F[B])(implicit ev: Applicative[F]): F[B] =
        productR(fb)
    }

    implicit class ApplicativeOps2[A](a: A) {
      def pure[F[_]](implicit ev: Applicative[F]): F[A] = ev.pure(a)
    }

    def unit[F[_]](implicit ev: Applicative[F]): F[Unit] = ev.unit
  }
}
