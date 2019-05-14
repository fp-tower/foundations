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

    implicit class ApplicativeTuple2Ops[F[_], A, B](self: (F[A], F[B])) {
      def map2[C](f: (A, B) => C)(implicit ev: Applicative[F]): F[C] =
        ev.map2(self._1, self._2)(f)

      def tuple2(implicit ev: Applicative[F]): F[(A, B)] =
        ev.tuple2(self._1, self._2)
    }

    implicit class ApplicativeTuple3Ops[F[_], A, B, C](self: (F[A], F[B], F[C])) {
      def map3[D](f: (A, B, C) => D)(implicit ev: Applicative[F]): F[D] =
        ev.map3(self._1, self._2, self._3)(f)
    }

    def unit[F[_]](implicit ev: Applicative[F]): F[Unit] = ev.unit
  }
}
