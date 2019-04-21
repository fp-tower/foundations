package toimpl.functors

import exercises.typeclass.Monoid
import exercises.functors.{Applicative, Apply, Compose, Const, FlatMap, Functor, Id, Monad, Traverse, ZipList}

trait FunctorsToImpl {

  ////////////////////////
  // 1. Functor
  ////////////////////////

  implicit val listFunctor: Functor[List]
  implicit val optionFunctor: Functor[Option]
  implicit def eitherFunctor[E]: Functor[Either[E, ?]]
  implicit def mapFunctor[K]: Functor[Map[K, ?]]
  implicit val idFunctor: Functor[Id]
  implicit def constFunctor[R]: Functor[Const[R, ?]]
  implicit def functionFunctor[R]: Functor[R => ?]

  def void[F[_]: Functor, A](fa: F[A]): F[Unit]
  def as[F[_]: Functor, A, B](fa: F[A])(value: B): F[B]
  def widen[F[_]: Functor, A, B >: A](fa: F[A]): F[B]
  def tupleLeft [F[_]: Functor, A, B](fa: F[A])(value: B): F[(B, A)]
  def tupleRight[F[_]: Functor, A, B](fa: F[A])(value: B): F[(A, B)]
  implicit def composeFunctor[F[_]: Functor, G[_]: Functor]: Functor[Compose[F, G, ?]]

  ////////////////////////
  // 2. Applicative
  ////////////////////////

  implicit val listApplicative: Applicative[List]
  implicit val optionApplicative: Applicative[Option]
  implicit def eitherApplicative[E]: Applicative[Either[E, ?]]
  implicit def mapApply[K]: Apply[Map[K, ?]]
  implicit val idApplicative: Applicative[Id]
  implicit def constApplicative[R: Monoid]: Applicative[Const[R, ?]]
  implicit def functionApplicative[R]: Applicative[R => ?]

  def map3[F[_]: Applicative, A, B, C, D](fa: F[A], fb: F[B], fc: F[C])(f: (A, B, C) => D): F[D]
  def tuple2[F[_]: Applicative, A, B](fa: F[A], fb: F[B]): F[(A, B)]
  def productL[F[_]: Applicative, A, B](fa: F[A], fb: F[B]): F[A]
  def productR[F[_]: Applicative, A, B](fa: F[A], fb: F[B]): F[B]
  def unit[F[_]: Applicative]: F[Unit]

  implicit val zipListApply: Apply[ZipList]
  implicit def composeApplicative[F[_]: Applicative, G[_]: Applicative]: Applicative[Compose[F, G, ?]]

  ////////////////////////
  // 3. Monad
  ////////////////////////

  implicit val listMonad: Monad[List]
  implicit val optionMonad: Monad[Option]
  implicit def eitherMonad[E]: Monad[Either[E, ?]]
  implicit def mapFlatMap[K]: FlatMap[Map[K, ?]]
  implicit val idMonad: Monad[Id]
  implicit def functionMonad[R]: Monad[R => ?]

  def flatten[F[_]: Monad, A](ffa: F[F[A]]): F[A]
  def flatTap[F[_]: Monad, A, B](fa: F[A])(f: A => F[B]): F[A]
  def ifM[F[_]: Monad, A](cond: F[Boolean])(ifTrue: => F[A], ifFalse: => F[A]): F[A]
  def whileM_[F[_]: Monad, A](cond: F[Boolean])(fa: => F[A]): F[Unit]
  def forever[F[_]: Monad, A](fa: F[A]): F[Nothing]


  ////////////////////////
  // 4. Traverse
  ////////////////////////

  implicit val listTraverse: Traverse[List]
  implicit val optionTraverse: Traverse[Option]
  implicit def eitherTraverse[E]: Traverse[Either[E, ?]]
  implicit def mapTraverse[K]: Traverse[Map[K, ?]]
  implicit val idTraverse: Traverse[Id]
  implicit def constTraverse[R]: Traverse[Const[R, ?]]

}
