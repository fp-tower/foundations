package exercises.typeclass2

import exercises.typeclass.Monoid

object FTypeclassExercises {

  ////////////////////////
  // 1. Functor
  ////////////////////////

  // 1a. Implement the following instance
  implicit val listFunctor: Functor[List] = new Functor[List] {
    def map[A, B](fa: List[A])(f: A => B): List[B] = ???
  }

  implicit val optionFunctor: Functor[Option] = new Functor[Option] {
    def map[A, B](fa: Option[A])(f: A => B): Option[B] = ???
  }

  implicit def eitherFunctor[E]: Functor[Either[E, ?]] = new Functor[Either[E, ?]] {
    def map[A, B](fa: Either[E, A])(f: A => B): Either[E, B] = ???
  }

  implicit def mapFunctor[K]: Functor[Map[K, ?]] = new Functor[Map[K, ?]] {
    def map[A, B](fa: Map[K, A])(f: A => B): Map[K, B] = ???
  }

  implicit val idFunctor: Functor[Id] = new Functor[Id] {
    def map[A, B](fa: Id[A])(f: A => B): Id[B] = ???
  }

  implicit def constFunctor[R]: Functor[Const[R, ?]] = new Functor[Const[R, ?]] {
    def map[A, B](fa: Const[R, A])(f: A => B): Const[R, B] = ???
  }

  // 1b. Implement void
  // such as void(List(1,2,3)) == List((),(),())
  def void[F[_]: Functor, A](fa: F[A]): F[Unit] = ???


  // 1c. Implement as
  // such as as(List(1,2,3))(0) == List(0,0,0)
  def as[F[_]: Functor, A, B](fa: F[A])(value: B): F[B] = ???


  // 1d. Implement widen
  def widen[F[_]: Functor, A, B >: A](fa: F[A]): F[B] = ???


  // 1e. Implement tupleLeft
  // such as tupleLeft(Some(4))("hello") == Some(("hello", 4))
  //         tupleRight(Some(4))("hello") == Some((4, "hello"))
  // but     tupleRight(None)("hello") == None
  def tupleLeft [F[_]: Functor, A, B](fa: F[A])(value: B): F[(B, A)] = ???
  def tupleRight[F[_]: Functor, A, B](fa: F[A])(value: B): F[(A, B)] = ???



  ////////////////////////
  // 2. Applicative
  ////////////////////////

  trait DefaultApplicative[F[_]] extends Applicative[F] {
    // 2a. Show that map can be implemented using Applicative using map2
    def map[A, B](fa: F[A])(f: A => B): F[B] = ???
  }

  // 2b. Implement the following instance
  implicit val listApplicative: Applicative[List] = new DefaultApplicative[List] {
    def pure[A](a: A): List[A] = ???
    def map2[A, B, C](fa: List[A], fb: List[B])(f: (A, B) => C): List[C] = ???
  }

  implicit val optionApplicative: Applicative[Option] = new DefaultApplicative[Option] {
    def pure[A](a: A): Option[A] = ???
    def map2[A, B, C](fa: Option[A], fb: Option[B])(f: (A, B) => C): Option[C] = ???
  }

  implicit def eitherApplicative[E]: Applicative[Either[E, ?]] = new DefaultApplicative[Either[E, ?]] {
    def pure[A](a: A): Either[E, A] = ???
    def map2[A, B, C](fa: Either[E, A], fb: Either[E, B])(f: (A, B) => C): Either[E, C] = ???
  }

  implicit def mapApplicative[K]: Applicative[Map[K, ?]] = new DefaultApplicative[Map[K, ?]] {
    def pure[A](a: A): Map[K, A] = ???
    def map2[A, B, C](fa: Map[K, A], fb: Map[K, B])(f: (A, B) => C): Map[K, C] = ???
  }

  implicit val idApplicative: Applicative[Id] = new DefaultApplicative[Id] {
    def pure[A](a: A): Id[A] = ???
    def map2[A, B, C](fa: Id[A], fb: Id[B])(f: (A, B) => C): Id[C] = ???
  }

  implicit def constApplicative[R]: Applicative[Const[R, ?]] = new DefaultApplicative[Const[R, ?]] {
    def pure[A](a: A): Const[R, A] = ???
    def map2[A, B, C](fa: Const[R, A], fb: Const[R, B])(f: (A, B) => C): Const[R, C] = ???
  }

  // 2c. Implement map3
  def map3[F[_]: Applicative, A, B, C, D](fa: F[A], fb: F[B], fc: F[C])(f: (A, B, C) => D): F[D] = ???

  // 2d. Implement tuple2
  def tuple2[F[_]: Applicative, A, B](fa: F[A], fb: F[B]): F[(A, B)] = ???

  // 2e. Implement productL, productR
  def productL[F[_]: Applicative, A, B](fa: F[A], fb: F[B]): F[A] = ???
  def productR[F[_]: Applicative, A, B](fa: F[A], fb: F[B]): F[B] = ???


  // productL / productR is often alias to <* / *>
  // such as you can write fa <* fb or fa *> fb


  // 2f. Implement unit
  def unit[F[_]: Applicative]: F[Unit] = ???


  // 2h. Implement an Applicative instance for ZipList
  // such as map2 "zip" the two List instead of doing the cartesian product
  // e.g. map2(ZipList(1,2,3), ZipList(2,2,2))(_ + _) == ZipList(3,4,5)
  implicit val zipListApplicative: Applicative[ZipList] = new DefaultApplicative[ZipList] {
    def map2[A, B, C](fa: ZipList[A], fb: ZipList[B])(f: (A, B) => C): ZipList[C] = ???
    def pure[A](a: A): ZipList[A] = ???
  }


  // 2i. Implement an Apply instance for ZipList
  implicit val zipListApply: Apply[ZipList] = new Apply[ZipList]{
    def map[A, B](fa: ZipList[A])(f: A => B): ZipList[B] = ???
    def map2[A, B, C](fa: ZipList[A], fb: ZipList[B])(f: (A, B) => C): ZipList[C] = ???
  }

  // 2i. Why does the Applicative instance of List does the cartesian product instead of zip?


  ////////////////////////
  // 3. Monad
  ////////////////////////

  trait DefaultMonad[F[_]] extends Monad[F] {
    // 3a. Show that map can be implemented using Monad using flatMap
    def map[A, B](fa: F[A])(f: A => B): F[B] = ???

    // 3b. Show that map2 can be implemented using Monad using flatMap
    def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] = ???
  }

  // 3c. Implement the following instance
  implicit val listMonad: Monad[List] = new DefaultMonad[List] {
    def pure[A](a: A): List[A] = listApplicative.pure(a)
    def flatMap[A, B](fa: List[A])(f: A => List[B]): List[B] = ???
  }

  implicit val optionMonad: Monad[Option] = new DefaultMonad[Option] {
    def pure[A](a: A): Option[A] = optionApplicative.pure(a)
    def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] = ???
  }

  implicit def eitherMonad[E]: Monad[Either[E, ?]] = new DefaultMonad[Either[E, ?]] {
    def pure[A](a: A): Either[E, A] = eitherApplicative.pure(a)
    def flatMap[A, B](fa: Either[E, A])(f: A => Either[E, B]): Either[E, B] = ???
  }

  implicit def mapMonad[K]: Monad[Map[K, ?]] = new DefaultMonad[Map[K, ?]] {
    def pure[A](a: A): Map[K, A] = mapApplicative.pure(a)
    def flatMap[A, B](fa: Map[K, A])(f: A => Map[K, B]): Map[K, B] = ???
  }

  implicit val idMonad: Monad[Id] = new DefaultMonad[Id] {
    def pure[A](a: A): Id[A] = idApplicative.pure(a)
    def flatMap[A, B](fa: Id[A])(f: A => Id[B]): Id[B] = ???
  }

  implicit def constMonad[R]: Monad[Const[R, ?]] = new DefaultMonad[Const[R, ?]] {
    def pure[A](a: A): Const[R, A] = constApplicative.pure(a)
    def flatMap[A, B](fa: Const[R, A])(f: A => Const[R, B]): Const[R, B] = ???
  }

  // 3d. Implement flatten
  def flatten[F[_]: Monad, A](ffa: F[F[A]]): F[A] = ???

  // 3e. Implement flatTap
  def flatTap[F[_]: Monad, A, B](fa: F[A])(f: A => F[B]): F[A] = ???

  // 3f. Implement ifM
  def ifM[F[_]: Monad, A](cond: F[Boolean])(ifTrue: => F[A], ifFalse: => F[A]): F[A] = ???

  // 3g. Implement whileM
  def whileM[F[_]: Monad, A](cond: F[Boolean])(fa: => F[A]): F[A] = ???

  // 2h. Implement forever
  def forever[F[_]: Monad, A](fa: F[A]): F[Nothing] = ???


  ////////////////////////
  // 4. Traverse
  ////////////////////////

  trait DefaultTraverse[F[_]] extends Traverse[F] {
    // 4a. Show that traverse can be implemented using sequence
    def traverse[G[_]: Applicative, A, B](fa: F[A])(f: A => G[B]): G[F[B]] = ???

    // 4b. Show that traverse can be implemented using sequence
    def sequence[G[_]: Applicative, A](fga: F[G[A]]): G[F[A]] = ???

    // 4c. Show that map can be implemented using traverse
    def map[A, B](fa: F[A])(f: A => B): F[B] = ???

    // 4d. Show that foldMap can be implemented using traverse
    override def foldMap[A, B: Monoid](fa: F[A])(f: A => B): B = ???

    // 4e. Show that foldLeft can be implemented using foldMap
    def foldLeft[A, B](fa: F[A], z: B)(f: (B, A) => B): B = ???

    // 4f. Show that foldLeft can be implemented using foldMap
    def foldRight[A, B](fa: F[A], z: B)(f: (A, => B) => B): B = ???
  }

  // 4g. Implement the following instance
  implicit val listTraverse: Traverse[List] = new DefaultTraverse[List] {
    override def traverse[G[_]: Applicative, A, B](fa: List[A])(f: A => G[B]): G[List[B]] = ???
  }

  implicit val optionTraverse: Traverse[Option] = new DefaultTraverse[Option] {
    override def traverse[G[_]: Applicative, A, B](fa: Option[A])(f: A => G[B]): G[Option[B]] = ???
  }

  implicit def eitherTraverse[E]: Traverse[Either[E, ?]] = new DefaultTraverse[Either[E, ?]] {
    override def traverse[G[_]: Applicative, A, B](fa: Either[E, A])(f: A => G[B]): G[Either[E, B]] = ???
  }

  implicit def mapTraverse[K]: Traverse[Map[K, ?]] = new DefaultTraverse[Map[K, ?]] {
    override def traverse[G[_] : Applicative, A, B](fa: Map[K, A])(f: A => G[B]): G[Map[K, B]] = ???
  }

  implicit val idTraverse: Traverse[Id] = new DefaultTraverse[Id] {
    override def traverse[G[_] : Applicative, A, B](fa: Id[A])(f: A => G[B]): G[Id[B]] = ???
  }

  implicit def constTraverse[R]: Traverse[Const[R, ?]] = new DefaultTraverse[Const[R, ?]] {
    override def traverse[G[_]: Applicative, A, B](fa: Const[R, A])(f: A => G[B]): G[Const[R, B]] = ???
  }

}
