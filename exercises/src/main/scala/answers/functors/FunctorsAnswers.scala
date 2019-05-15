package answers.functors

import answers.typeclass.TypeclassAnswers._
import exercises.functors.Applicative.syntax._
import exercises.functors.Functor.syntax._
import exercises.functors.Traverse.syntax._
import exercises.functors._
import exercises.typeclass.Foldable.syntax._
import exercises.typeclass.Semigroup.syntax._
import exercises.typeclass.{Dual, Endo, Monoid}
import toimpl.functors.FunctorsToImpl

object FunctorsAnswers extends FunctorsToImpl {

  ////////////////////////
  // 1. Functor
  ////////////////////////

  trait DefaultFunctor[F[_]] extends Functor[F] {
    def as[A, B](fa: F[A])(value: B): F[B] = map(fa)(_ => value)

    def void[A](fa: F[A]): F[Unit] = as(fa)(())

    def widen[A, B >: A](fa: F[A]): F[B] = map(fa)(identity)

    def tupleL[A, B](fa: F[A])(value: B): F[(B, A)] = map(fa)(value -> _)
    def tupleR[A, B](fa: F[A])(value: B): F[(A, B)] = map(fa)(_     -> value)

    def lift[A, B](f: A => B): F[A] => F[B] = map(_)(f)
  }

  implicit val listFunctor: Functor[List] = new DefaultFunctor[List] {
    def map[A, B](fa: List[A])(f: A => B): List[B] = fa.map(f)
  }

  implicit val optionFunctor: Functor[Option] = new DefaultFunctor[Option] {
    def map[A, B](fa: Option[A])(f: A => B): Option[B] = fa.map(f)
  }

  implicit def eitherFunctor[E]: Functor[Either[E, ?]] = new DefaultFunctor[Either[E, ?]] {
    def map[A, B](fa: Either[E, A])(f: A => B): Either[E, B] = fa.map(f)
  }

  implicit def mapFunctor[K]: Functor[Map[K, ?]] = new DefaultFunctor[Map[K, ?]] {
    def map[A, B](fa: Map[K, A])(f: A => B): Map[K, B] = fa.map { case (k, v) => k -> f(v) }
  }

  implicit val idFunctor: Functor[Id] = new DefaultFunctor[Id] {
    def map[A, B](fa: Id[A])(f: A => B): Id[B] = Id(f(fa.value))
  }

  implicit def constFunctor[R]: Functor[Const[R, ?]] = new DefaultFunctor[Const[R, ?]] {
    def map[A, B](fa: Const[R, A])(f: A => B): Const[R, B] = fa.as[B]
  }

  implicit def functionFunctor[R]: Functor[Function[R, ?]] = new DefaultFunctor[Function[R, ?]] {
    def map[A, B](fa: R => A)(f: A => B): R => B = f compose fa
  }

  implicit val predicateContravariantFunctor: ContravariantFunctor[Predicate] = new ContravariantFunctor[Predicate] {
    def contramap[A, B](fa: Predicate[A])(f: B => A): Predicate[B] =
      Predicate(f andThen fa.condition)
  }

  implicit val stringCodecInvariantFunctor: InvariantFunctor[StringCodec] = new InvariantFunctor[StringCodec] {
    def imap[A, B](fa: StringCodec[A])(f: A => B)(g: B => A): StringCodec[B] =
      StringCodec(
        mkString = g andThen fa.mkString,
        parse = fa.parse(_).map(f)
      )
  }

  implicit def composeFunctor[F[_]: Functor, G[_]: Functor]: Functor[Compose[F, G, ?]] =
    new DefaultFunctor[Compose[F, G, ?]] {
      def map[A, B](fa: Compose[F, G, A])(f: A => B): Compose[F, G, B] =
        Compose(fa.getCompose.map(_.map(f)))
    }

  ////////////////////////
  // 2. Applicative
  ////////////////////////

  trait DefaultApplicative[F[_]] extends Applicative[F] with DefaultFunctor[F] {
    def map[A, B](fa: F[A])(f: A => B): F[B] = map2(fa, pure(()))((a, _) => f(a))

    def map3[A, B, C, D](fa: F[A], fb: F[B], fc: F[C])(f: (A, B, C) => D): F[D] =
      map2(map2(fa, fb)((a, b) => f(a, b, _)), fc)(_(_))

    def tuple2[A, B](fa: F[A], fb: F[B]): F[(A, B)] =
      map2(fa, fb)((_, _))

    def productL[A, B](fa: F[A], fb: F[B]): F[A] = map2(fa, fb)((a, _) => a)
    def productR[A, B](fa: F[A], fb: F[B]): F[B] = map2(fa, fb)((_, b) => b)

    def unit: F[Unit] = pure(())
  }

  // 2b. Implement the following instance
  implicit val listApplicative: Applicative[List] = new DefaultApplicative[List] {
    def pure[A](a: A): List[A] = List(a)
    def map2[A, B, C](fa: List[A], fb: List[B])(f: (A, B) => C): List[C] =
      for {
        a <- fa
        b <- fb
      } yield f(a, b)
  }

  implicit val optionApplicative: Applicative[Option] = new DefaultApplicative[Option] {
    def pure[A](a: A): Option[A] = Some(a)
    def map2[A, B, C](fa: Option[A], fb: Option[B])(f: (A, B) => C): Option[C] =
      for {
        a <- fa
        b <- fb
      } yield f(a, b)
  }

  implicit def eitherApplicative[E]: Applicative[Either[E, ?]] = new DefaultApplicative[Either[E, ?]] {
    def pure[A](a: A): Either[E, A] = Right(a)
    def map2[A, B, C](fa: Either[E, A], fb: Either[E, B])(f: (A, B) => C): Either[E, C] =
      for {
        a <- fa
        b <- fb
      } yield f(a, b)
  }

  implicit def mapApply[K]: Apply[Map[K, ?]] = new Apply[Map[K, ?]] with DefaultFunctor[Map[K, ?]] {
    def map[A, B](fa: Map[K, A])(f: A => B): Map[K, B] = fa.map { case (k, v) => k -> f(v) }
    def map2[A, B, C](fa: Map[K, A], fb: Map[K, B])(f: (A, B) => C): Map[K, C] =
      fa.flatMap { case (k, a) => fb.get(k).map(b => k -> f(a, b)) }
  }

  implicit val idApplicative: Applicative[Id] = new DefaultApplicative[Id] {
    def pure[A](a: A): Id[A]                                       = Id(a)
    def map2[A, B, C](fa: Id[A], fb: Id[B])(f: (A, B) => C): Id[C] = Id(f(fa.value, fb.value))
  }

  implicit def constApplicative[R: Monoid]: Applicative[Const[R, ?]] = new DefaultApplicative[Const[R, ?]] {
    def pure[A](a: A): Const[R, A] = Const(Monoid[R].empty)
    def map2[A, B, C](fa: Const[R, A], fb: Const[R, B])(f: (A, B) => C): Const[R, C] =
      Const(fa.getConst |+| fb.getConst)
  }

  implicit def functionApplicative[R]: Applicative[Function[R, ?]] = new DefaultApplicative[Function[R, ?]] {
    def pure[A](a: A): R => A = _ => a
    def map2[A, B, C](fa: R => A, fb: R => B)(f: (A, B) => C): R => C =
      r => f(fa(r), fb(r))
  }

  implicit val zipListApply: Apply[ZipList] = new Apply[ZipList] with DefaultFunctor[ZipList] {
    def map[A, B](fa: ZipList[A])(f: A => B): ZipList[B] = ZipList(fa.getZipList.map(f))
    def map2[A, B, C](fa: ZipList[A], fb: ZipList[B])(f: (A, B) => C): ZipList[C] =
      ZipList(fa.getZipList.zip(fb.getZipList).map { case (a, b) => f(a, b) })
  }

  implicit def composeApplicative[F[_]: Applicative, G[_]: Applicative]: Applicative[Compose[F, G, ?]] =
    new DefaultApplicative[Compose[F, G, ?]] {
      def pure[A](a: A): Compose[F, G, A] = Compose(a.pure[G].pure[F])
      def map2[A, B, C](fa: Compose[F, G, A], fb: Compose[F, G, B])(f: (A, B) => C): Compose[F, G, C] =
        Compose(
          (fa.getCompose, fb.getCompose).map2((ga, gb) => (ga, gb).map2(f))
        )
    }

  ////////////////////////
  // 3. Monad
  ////////////////////////

  trait DefaultMonad[F[_]] extends Monad[F] with DefaultApplicative[F] {
    override def map[A, B](fa: F[A])(f: A => B): F[B] =
      flatMap(fa)(a => pure(f(a)))

    def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] =
      flatMap(fa)(a => map(fb)(f(a, _)))

    def flatten[A](ffa: F[F[A]]): F[A] =
      flatMap(ffa)(identity)

    def flatTap[A, B](fa: F[A])(f: A => F[B]): F[A] =
      flatMap(fa)(a => as(f(a))(a))

    def ifM[A](cond: F[Boolean])(ifTrue: => F[A], ifFalse: => F[A]): F[A] =
      flatMap(cond)(if (_) ifTrue else ifFalse)
  }

  implicit val listMonad: Monad[List] = new DefaultMonad[List] {
    def pure[A](a: A): List[A]                               = List(a)
    def flatMap[A, B](fa: List[A])(f: A => List[B]): List[B] = fa.flatMap(f)
  }

  implicit val optionMonad: Monad[Option] = new DefaultMonad[Option] {
    def pure[A](a: A): Option[A]                                   = Some(a)
    def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] = fa.flatMap(f)
  }

  implicit def eitherMonad[E]: Monad[Either[E, ?]] = new DefaultMonad[Either[E, ?]] {
    def pure[A](a: A): Either[E, A]                                         = Right(a)
    def flatMap[A, B](fa: Either[E, A])(f: A => Either[E, B]): Either[E, B] = fa.flatMap(f)
  }

  implicit def mapFlatMap[K]: FlatMap[Map[K, ?]] = new FlatMap[Map[K, ?]] with DefaultFunctor[Map[K, ?]] {
    def map[A, B](fa: Map[K, A])(f: A => B): Map[K, B] = mapFunctor[K].map(fa)(f)
    def flatMap[A, B](fa: Map[K, A])(f: A => Map[K, B]): Map[K, B] =
      fa.flatMap { case (k, a) => f(a).get(k).map(k -> _) }
  }

  implicit val idMonad: Monad[Id] = new DefaultMonad[Id] {
    def pure[A](a: A): Id[A]                           = Id(a)
    def flatMap[A, B](fa: Id[A])(f: A => Id[B]): Id[B] = f(fa.value)
  }

  implicit def functionMonad[R]: Monad[R => ?] = new DefaultMonad[R => ?] {
    def pure[A](a: A): R => A                             = _ => a
    def flatMap[A, B](fa: R => A)(f: A => R => B): R => B = r => f(fa(r))(r)
  }

  ////////////////////////
  // 4. Traverse
  ////////////////////////

  trait DefaultTraverse[F[_]] extends Traverse[F] with DefaultFunctor[F] {
    def traverse[G[_]: Applicative, A, B](fa: F[A])(f: A => G[B]): G[F[B]] =
      sequence(map(fa)(f))

    def sequence[G[_]: Applicative, A](fga: F[G[A]]): G[F[A]] =
      traverse(fga)(identity)

    def traverse_[G[_]: Applicative, A, B](fa: F[A])(f: A => G[B]): G[Unit] =
      foldRight(fa, unit[G])((a, acc) => f(a) *> acc)

    def flatSequence[G[_]: Applicative, A](fgfa: F[G[F[A]]])(implicit ev: Monad[F]): G[F[A]] =
      sequence(fgfa).map(ev.flatten)

    def flatTraverse[G[_]: Applicative, A, B](fa: F[A])(f: A => G[F[B]])(implicit ev: Monad[F]): G[F[B]] =
      traverse(fa)(f).map(ev.flatten)

    override def map[A, B](fa: F[A])(f: A => B): F[B] =
      traverse(fa)(a => Id(f(a))).value

    override def foldMap[A, B: Monoid](fa: F[A])(f: A => B): B =
      traverse(fa)(a => Const(f(a)).as[B]).getConst

    def foldLeft[A, B](fa: F[A], z: B)(f: (B, A) => B): B =
      foldMap(fa)(a => Dual(Endo[B](b => f(b, a)))).getDual.getEndo(z)

    def foldRight[A, B](fa: F[A], z: B)(f: (A, => B) => B): B =
      foldMap(fa)(a => Endo[B](b => f(a, b))).getEndo(z)
  }

  implicit val listTraverse: Traverse[List] = new DefaultTraverse[List] {
    override def traverse[G[_]: Applicative, A, B](fa: List[A])(f: A => G[B]): G[List[B]] =
      fa.foldRight(List.empty[B].pure[G])((a, acc) => (f(a), acc).map2(_ :: _))
  }

  implicit val optionTraverse: Traverse[Option] = new DefaultTraverse[Option] {
    override def traverse[G[_]: Applicative, A, B](fa: Option[A])(f: A => G[B]): G[Option[B]] =
      fa.fold(Option.empty[B].pure[G])(f(_).map(Some(_)))
  }

  implicit def eitherTraverse[E]: Traverse[Either[E, ?]] = new DefaultTraverse[Either[E, ?]] {
    override def traverse[G[_]: Applicative, A, B](fa: Either[E, A])(f: A => G[B]): G[Either[E, B]] =
      fa.fold(
        e => (Left(e): Either[E, B]).pure[G],
        a => f(a).map(Right(_))
      )
  }

  implicit def mapTraverse[K]: Traverse[Map[K, ?]] = new DefaultTraverse[Map[K, ?]] {
    override def traverse[G[_]: Applicative, A, B](fa: Map[K, A])(f: A => G[B]): G[Map[K, B]] =
      fa.foldLeft(
        Map.empty[K, B].pure[G]
      ) { case (acc, (k, a)) => (acc, f(a)).map2((m, b) => m + (k -> b)) }
  }

  implicit val idTraverse: Traverse[Id] = new DefaultTraverse[Id] {
    override def traverse[G[_]: Applicative, A, B](fa: Id[A])(f: A => G[B]): G[Id[B]] =
      f(fa.value).map(Id(_))
  }

  implicit def constTraverse[R]: Traverse[Const[R, ?]] = new DefaultTraverse[Const[R, ?]] {
    override def traverse[G[_]: Applicative, A, B](fa: Const[R, A])(f: A => G[B]): G[Const[R, B]] =
      fa.as[B].pure[G]
  }

  implicit val bigIntMonoid: Monoid[BigInt] = new Monoid[BigInt] {
    def combine(x: BigInt, y: BigInt): BigInt = x + y
    def empty: BigInt                         = BigInt(0)
  }

  def parseNumber(value: String): Option[BigInt] =
    value.toList
      .traverse(parseDigit)
      .map(
        digits =>
          digits.reverse.zipWithIndex.foldMap {
            case (digit, index) =>
              digit * BigInt(10).pow(index)
        }
      )

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

  def checkAllUsersAdult(country: CountryUsers.Country): Either[String, Unit] = {
    import CountryUsers._

    getUsers(country).flatMap(_.traverse_(checkAdult))
  }

  implicit def composeTraverse[F[_]: Traverse, G[_]: Traverse]: Traverse[Compose[F, G, ?]] =
    new DefaultTraverse[Compose[F, G, ?]] {
      override def traverse[H[_]: Applicative, A, B](fa: Compose[F, G, A])(f: A => H[B]): H[Compose[F, G, B]] =
        fa.getCompose.traverse(_.traverse(f)).map(Compose(_))
    }

}
