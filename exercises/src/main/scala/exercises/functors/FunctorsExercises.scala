package exercises.functors

import exercises.typeclass.Monoid
import exercises.functors.Applicative.syntax._
import exercises.functors.Functor.syntax._
import exercises.functors.Monad.syntax._
import exercises.functors.Traverse.syntax._
import exercises.functors._
import exercises.typeclass.Foldable.syntax._
import exercises.typeclass.Monoid.syntax._
import toimpl.functors.FunctorsToImpl

object FunctorsExercises extends FunctorsToImpl {

  ////////////////////////
  // 1. Functor
  ////////////////////////

  // 1a. Implement the following instances
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

  implicit def functionFunctor[R]: Functor[R => ?] = new Functor[Function[R, ?]] {
    def map[A, B](fa: R => A)(f: A => B): R => B = ???
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


  // 1f. Implement a Functor instance for Compose
  implicit def composeFunctor[F[_]: Functor, G[_]: Functor]: Functor[Compose[F, G, ?]] = new Functor[Compose[F, G, ?]] {
    def map[A, B](fa: Compose[F, G, A])(f: A => B): Compose[F, G, B] = ???
  }

  // 1g. Implement a Functor instance for Predicate
  implicit def predicateFunctor: Functor[Predicate] = new Functor[Predicate] {
    def map[A, B](fa: Predicate[A])(f: A => B): Predicate[B] = ???
  }

  // 1h. Implement a Functor instance for StringEncoder
  implicit def stringEncoderFunctor: Functor[StringEncoder] = new Functor[StringEncoder] {
    def map[A, B](fa: StringEncoder[A])(f: A => B): StringEncoder[B] = ???
  }



  ////////////////////////
  // 2. Applicative
  ////////////////////////

  trait DefaultApplicative[F[_]] extends Applicative[F] {
    // 2a. Show that map can be implemented using Applicative using map2
    def map[A, B](fa: F[A])(f: A => B): F[B] = ???
  }

  // 2b. Implement the following instances
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

  implicit def mapApply[K]: Apply[Map[K, ?]] = new Apply[Map[K, ?]] {
    def map[A, B](fa: Map[K, A])(f: A => B): Map[K, B] = mapFunctor[K].map(fa)(f)
    def map2[A, B, C](fa: Map[K, A], fb: Map[K, B])(f: (A, B) => C): Map[K, C] = ???
  }

  implicit val idApplicative: Applicative[Id] = new DefaultApplicative[Id] {
    def pure[A](a: A): Id[A] = ???
    def map2[A, B, C](fa: Id[A], fb: Id[B])(f: (A, B) => C): Id[C] = ???
  }

  implicit def constApplicative[R: Monoid]: Applicative[Const[R, ?]] = new DefaultApplicative[Const[R, ?]] {
    def pure[A](a: A): Const[R, A] = ???
    def map2[A, B, C](fa: Const[R, A], fb: Const[R, B])(f: (A, B) => C): Const[R, C] = ???
  }

  implicit def functionApplicative[R]: Applicative[R => ?] = new DefaultApplicative[R => ?] {
    def pure[A](a: A): R => A = ???
    def map2[A, B, C](fa: R => A, fb: R => B)(f: (A, B) => C): R => C = ???
  }

  // 2c. Implement map3
  def map3[F[_]: Applicative, A, B, C, D](fa: F[A], fb: F[B], fc: F[C])(f: (A, B, C) => D): F[D] = ???

  // 2d. Implement tuple2
  // such as tuple2(List(1,2,3), List('a','b')) == List((1, 'a'), (1, 'b'), (2, 'a'), (2, 'b'), (3, 'a'), (3, 'b'))
  def tuple2[F[_]: Applicative, A, B](fa: F[A], fb: F[B]): F[(A, B)] = ???

  // 2e. Implement productL, productR
  // such as productL(Option(1), Option("hello")) == Some(1)
  //         productR(Option(1), Option("hello")) == Some("hello")
  // but     productR(Option(1), None)            == None
  def productL[F[_]: Applicative, A, B](fa: F[A], fb: F[B]): F[A] = ???
  def productR[F[_]: Applicative, A, B](fa: F[A], fb: F[B]): F[B] = ???


  // productL / productR is often alias to <* / *>
  // such as you can write fa <* fb or fa *> fb


  // 2f. Implement unit
  // such as unit[List] == List(())
  //         unit[Either[String, ?]] == Right(())
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

  // 2j. Why does the Applicative instance of List does the cartesian product instead of zip?
  // Why most implementations of map2 use flatMap?


  // 2k. Implement an Applicative instance for Compose
  implicit def composeApplicative[F[_]: Applicative, G[_]: Applicative]: Applicative[Compose[F, G, ?]] =
    new DefaultApplicative[Compose[F, G, ?]] {
      def pure[A](a: A): Compose[F, G, A] = ???
      def map2[A, B, C](fa: Compose[F, G, A], fb: Compose[F, G, B])(f: (A, B) => C): Compose[F, G, C] = ???
    }


  ////////////////////////
  // 3. Monad
  ////////////////////////

  trait DefaultMonad[F[_]] extends Monad[F] {
    // 3a. Show that map can be implemented using Monad using flatMap
    def map[A, B](fa: F[A])(f: A => B): F[B] = ???

    // 3b. Show that map2 can be implemented using Monad using flatMap
    def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] = ???
  }

  // 3c. Implement the following instances
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

  implicit def mapFlatMap[K]: FlatMap[Map[K, ?]] = new FlatMap[Map[K, ?]] {
    def map[A, B](fa: Map[K, A])(f: A => B): Map[K, B] = mapFunctor[K].map(fa)(f)
    def flatMap[A, B](fa: Map[K, A])(f: A => Map[K, B]): Map[K, B] = ???
  }

  implicit val idMonad: Monad[Id] = new DefaultMonad[Id] {
    def pure[A](a: A): Id[A] = idApplicative.pure(a)
    def flatMap[A, B](fa: Id[A])(f: A => Id[B]): Id[B] = ???
  }

  implicit def constMonad[R]: FlatMap[Const[R, ?]] = new FlatMap[Const[R, ?]] {
    def map[A, B](fa: Const[R, A])(f: A => B): Const[R, B] = constFunctor[R].map(fa)(f)
    def flatMap[A, B](fa: Const[R, A])(f: A => Const[R, B]): Const[R, B] = ???
  }

  implicit def functionMonad[R]: Monad[Function[R, ?]] = new DefaultMonad[Function[R, ?]] {
    def pure[A](a: A): Function[R, A] = functionApplicative.pure(a)
    def flatMap[A, B](fa: Function[R, A])(f: A => Function[R, B]): Function[R, B] = ???
  }

  // 3d. Implement flatten
  // such as flatten(List(List(1,2), List(3,4,5)))  == List(1,2,3,4,5)
  //         flatten((x: Int) => (y: Int) => x + y) == (x: Int) => x + x
  def flatten[F[_]: Monad, A](ffa: F[F[A]]): F[A] = ???

  // 3e. Implement flatTap
  // such as flatTap(Option(10))(x => if(x > 0) unit[Option] else None) == Some(10)
  //         flatTap(Option(-5))(x => if(x > 0) unit[Option] else None) == None
  def flatTap[F[_]: Monad, A, B](fa: F[A])(f: A => F[B]): F[A] = ???

  // 3f. Implement ifM
  // such as val func = ifM((x: Int) => x > 0)(_ * 2, _.abs)
  //         func(-10) == 10
  //         func(  3) == 6
  def ifM[F[_]: Monad, A](cond: F[Boolean])(ifTrue: => F[A], ifFalse: => F[A]): F[A] = ???

  // 3g. Implement whileM
  def whileM_[F[_]: Monad, A](cond: F[Boolean])(fa: => F[A]): F[Unit] = ???

  // 3h. Implement forever
  def forever[F[_]: Monad, A](fa: F[A]): F[Nothing] = ???


  // 3i. Implement an Monad instance for Compose
  implicit def composeMonad[F[_]: Monad, G[_]: Monad]: Monad[Compose[F, G, ?]] =
    new DefaultMonad[Compose[F, G, ?]] {
      def pure[A](a: A): Compose[F, G, A] = ???
      def flatMap[A, B](fa: Compose[F, G, A])(f: A => Compose[F, G, B]): Compose[F, G, B] = ???
    }


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

  // 4h. Implement parseNumber, try to use traverse and parseDigit
  // such as parseNumber("1052") == Some(1052)
  //         parseNumber("hello") == None
  def parseNumber(value: String): Option[BigInt] = ???

  def parseDigit(value: Char): Option[Int] =
    value match {
      case '0' => Some(0)
      case '1' => Some(1)
      case '2' => Some(2)
      case '3' => Some(3)
      case '4' => Some(4)
      case '5' => Some(5)
      case '6' => Some(6)
      case '7' => Some(7)
      case '8' => Some(8)
      case '9' => Some(9)
      case _   => None
    }

  // 4i. Implement an Traverse instance for Compose
  implicit def composeTraverse[F[_]: Traverse, G[_]: Traverse]: Traverse[Compose[F, G, ?]] =
    new DefaultTraverse[Compose[F, G, ?]] {
      override def traverse[H[_] : Applicative, A, B](fa: Compose[F, G, A])(f: A => H[B]): H[Compose[F, G, B]] = ???
    }
}
