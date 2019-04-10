package exercises.typeclass2

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

  // 1a. Implement the following instance
  implicit val listApplicative: Applicative[List] = new Applicative[List] {
    def map[A, B](fa: List[A])(f: A => B): List[B] = listFunctor.map(fa)(f)

    def pure[A](a: A): List[A] = ???

    def map2[A, B, C](fa: List[A], fb: List[B])(f: (A, B) => C): List[C] = ???
  }

  implicit val optionApplicative: Applicative[Option] = new Applicative[Option] {
    def map[A, B](fa: Option[A])(f: A => B): Option[B] = optionFunctor.map(fa)(f)

    def pure[A](a: A): Option[A] = ???

    def map2[A, B, C](fa: Option[A], fb: Option[B])(f: (A, B) => C): Option[C] = ???
  }

  implicit def eitherApplicative[E]: Applicative[Either[E, ?]] = new Applicative[Either[E, ?]] {
    def map[A, B](fa: Either[E, A])(f: A => B): Either[E, B] = eitherFunctor.map(fa)(f)

    def pure[A](a: A): Either[E, A] = ???

    def map2[A, B, C](fa: Either[E, A], fb: Either[E, B])(f: (A, B) => C): Either[E, C] = ???
  }

  implicit def mapApplicative[K]: Applicative[Map[K, ?]] = new Applicative[Map[K, ?]] {
    def map[A, B](fa: Map[K, A])(f: A => B): Map[K, B] = mapFunctor.map(fa)(f)

    def pure[A](a: A): Map[K, A] = ???

    def map2[A, B, C](fa: Map[K, A], fb: Map[K, B])(f: (A, B) => C): Map[K, C] = ???
  }

  implicit val idApplicative: Applicative[Id] = new Applicative[Id] {
    def map[A, B](fa: Id[A])(f: A => B): Id[B] = idFunctor.map(fa)(f)

    def pure[A](a: A): Id[A] = ???

    def map2[A, B, C](fa: Id[A], fb: Id[B])(f: (A, B) => C): Id[C] = ???
  }

  implicit def constApplicative[R]: Applicative[Const[R, ?]] = new Applicative[Const[R, ?]] {
    def map[A, B](fa: Const[R, A])(f: A => B): Const[R, B] = constFunctor.map(fa)(f)

    def pure[A](a: A): Const[R, A] = ???

    def map2[A, B, C](fa: Const[R, A], fb: Const[R, B])(f: (A, B) => C): Const[R, C] = ???
  }

  // 2b. Show that map can be implemented using Applicative using map2
  def mapFromApplicative[F[_]: Applicative, A, B](fa: F[A])(f: A => B): F[B] = ???

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
  implicit val zipListApplicative: Applicative[ZipList] = new Applicative[ZipList] {
    def map[A, B](fa: ZipList[A])(f: A => B): ZipList[B] = ???

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

  // 2a. Show that map can be implemented using Monad using flatMap
  def mapFromMonad[F[_]: Applicative, A, B](fa: F[A])(f: A => B): F[B] = ???

  // 2a. Show that map2 can be implemented using Monad using flatMap
  def map2FromMonad[F[_]: Applicative, A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] = ???


}
