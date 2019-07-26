package exercises.functors

import cats.data.NonEmptyList
import cats.effect.IO
import exercises.errorhandling.Validated
import exercises.errorhandling.Validated._
import exercises.typeclass.{Monoid, Semigroup}
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

  trait DefaultFunctor[F[_]] extends Functor[F] {
    ///////////////////////////////////////////////////
    // Within this trait you CANNOT use Functor syntax
    // you CANNOT use:      fa.map(f)
    // instead you need to: map(fa)(f)
    ///////////////////////////////////////////////////

    // 1a. Implement as using map
    // such as List(1,2,3).as(0) == List(0,0,0)
    def as[A, B](fa: F[A])(value: B): F[B] = ???

    // 1b. Implement void
    // such as void(List(1,2,3)) == List((),(),())
    // use case:
    // val response: IO[Response] = post(Request(...))
    // response.void: IO[Unit]
    def void[A](fa: F[A]): F[Unit] = ???

    // 1c. Implement widen using map
    // use case:
    // val circles: List[Circle] = List(Circle(4), Circle(10))
    // val shapes: List[Shape] = circles.widen
    def widen[A, B >: A](fa: F[A]): F[B] = ???

    // 1d. Implement tupleL and tupleR using map
    // such as tupleL(Some(4))("hello")  == Some(("hello", 4))
    //         tupleR(Some(4))("hello") == Some((4, "hello"))
    // but     tupleL(None)("hello")  == None
    //         tupleR(None)("hello") == None
    // use case:
    // getUser(UserId(123)).flatMap(user =>
    //   getAccount(user.accountId).tupleL(user)
    // )
    def tupleL[A, B](fa: F[A])(value: B): F[(B, A)] = ???
    def tupleR[A, B](fa: F[A])(value: B): F[(A, B)] = ???

    // 1e. implement lift using map
    // use case:
    // val inc = (x: Int) => x + 1
    // inc.lift[List]: List[Int] => List[Int]
    // inc.lift[Either[String, ?]]: Either[String, Int] => Either[String, Int]
    def lift[A, B](f: A => B): F[A] => F[B] = ???
  }

  // 1e. Implement the following instances
  // you can reuse methods from the standard library
  implicit val listFunctor: Functor[List] = new DefaultFunctor[List] {
    def map[A, B](fa: List[A])(f: A => B): List[B] = fa.map(f)
  }

  implicit val optionFunctor: Functor[Option] = new DefaultFunctor[Option] {
    def map[A, B](fa: Option[A])(f: A => B): Option[B] = ???
  }

  implicit def eitherFunctor[E]: Functor[Either[E, ?]] = new DefaultFunctor[Either[E, ?]] {
    def map[A, B](fa: Either[E, A])(f: A => B): Either[E, B] = ???
  }

  implicit def validatedFunctor[E]: Functor[Validated[E, ?]] = new DefaultFunctor[Validated[E, ?]] {
    def map[A, B](fa: Validated[E, A])(f: A => B): Validated[E, B] = ???
  }

  implicit def mapFunctor[K]: Functor[Map[K, ?]] = new DefaultFunctor[Map[K, ?]] {
    def map[A, B](fa: Map[K, A])(f: A => B): Map[K, B] = ???
  }

  implicit val idFunctor: Functor[Id] = new DefaultFunctor[Id] {
    def map[A, B](fa: Id[A])(f: A => B): Id[B] = ???
  }

  implicit def constFunctor[R]: Functor[Const[R, ?]] = new DefaultFunctor[Const[R, ?]] {
    def map[A, B](fa: Const[R, A])(f: A => B): Const[R, B] = ???
  }

  implicit def functionFunctor[R]: Functor[R => ?] = new DefaultFunctor[Function[R, ?]] {
    def map[A, B](fa: R => A)(f: A => B): R => B = ???
  }

  // 1f. Implement a Functor instance for Predicate
  val isEven: Predicate[Int] = Predicate(x => x % 2 == 0)

  implicit val predicateFunctor: Functor[Predicate] = new DefaultFunctor[Predicate] {
    def map[A, B](fa: Predicate[A])(f: A => B): Predicate[B] = ???
  }

  // or
  implicit val predicateContravariantFunctor: ContravariantFunctor[Predicate] = new ContravariantFunctor[Predicate] {
    def contramap[A, B](fa: Predicate[A])(f: B => A): Predicate[B] = ???
  }

  // 1g. Implement a Functor instance for StringCodec
  implicit def stringCodecFunctor: Functor[StringCodec] = new DefaultFunctor[StringCodec] {
    def map[A, B](fa: StringCodec[A])(f: A => B): StringCodec[B] = ???
  }

  // or
  implicit val stringCodecContravariantFunctor: ContravariantFunctor[StringCodec] =
    new ContravariantFunctor[StringCodec] {
      def contramap[A, B](fa: StringCodec[A])(f: B => A): StringCodec[B] = ???
    }

  // or
  implicit val stringCodecInvariantFunctor: InvariantFunctor[StringCodec] = new InvariantFunctor[StringCodec] {
    def imap[A, B](fa: StringCodec[A])(f: A => B)(g: B => A): StringCodec[B] = ???
  }

  // 1h. Implement a Functor instance for Compose
  // e.g. Functor[Compose[Option, List, ?]]
  implicit def composeFunctor[F[_]: Functor, G[_]: Functor]: Functor[Compose[F, G, ?]] =
    new DefaultFunctor[Compose[F, G, ?]] {
      def map[A, B](fa: Compose[F, G, A])(f: A => B): Compose[F, G, B] = ???
    }

  ////////////////////////
  // 2. Applicative
  ////////////////////////

  trait DefaultApplicative[F[_]] extends Applicative[F] with DefaultFunctor[F] {
    //////////////////////////////////////////////////////
    // Within this trait you CANNOT use Applicative syntax
    // you CANNOT use:      (fa, fb).map2(f)
    // instead you need to: map2(fa, fb)(f)
    //////////////////////////////////////////////////////

    // 2a. Implement tuple2 using map2
    // such as tuple2(List(1,2,3), List('a','b')) == List((1, 'a'), (1, 'b'), (2, 'a'), (2, 'b'), (3, 'a'), (3, 'b'))
    def tuple2[A, B](fa: F[A], fb: F[B]): F[(A, B)] = ???

    // 2b. Implement unit
    // such as unit[List] == List(())
    //         unit[Either[String, ?]] == Right(())
    // use case:
    // optUser match {
    //   case None       => unit
    //   case Some(user) => saveUser(user)
    // }
    def unit: F[Unit] = ???

    // 2c. Implement map3 using previous functions
    def map3[A, B, C, D](fa: F[A], fb: F[B], fc: F[C])(f: (A, B, C) => D): F[D] = ???

    // 2d. Implement productL, productR using previous functions
    // such as productL(Option(1), Option("hello")) == Some(1)
    //         productR(Option(1), Option("hello")) == Some("hello")
    // but     productL(Option(1), None)            == None
    //         productR(Option(1), None)            == None
    // use cases:
    // parseSpaces(2) *> parseInt <* newLine would parse successfully "  123\n" to 123
    // getUser(userId) <* log.info(s"Got user $userId") would call getUser then log and return the fetched user
    def productL[A, B](fa: F[A], fb: F[B]): F[A] = ???
    def productR[A, B](fa: F[A], fb: F[B]): F[B] = ???

    // 2e. Implement map using map2
    def map[A, B](fa: F[A])(f: A => B): F[B] = ???
  }

  // 2f. Implement the following instances
  // you can use methods from the standard library
  implicit val listApplicative: Applicative[List] = new DefaultApplicative[List] {
    def map2[A, B, C](fa: List[A], fb: List[B])(f: (A, B) => C): List[C] =
      for {
        a <- fa
        b <- fb
      } yield f(a, b)
    def pure[A](a: A): List[A] =
      List(a)
  }

  implicit val optionApplicative: Applicative[Option] = new DefaultApplicative[Option] {
    def map2[A, B, C](fa: Option[A], fb: Option[B])(f: (A, B) => C): Option[C] = ???
    def pure[A](a: A): Option[A]                                               = ???
  }

  implicit def eitherApplicative[E]: Applicative[Either[E, ?]] = new DefaultApplicative[Either[E, ?]] {
    def map2[A, B, C](fa: Either[E, A], fb: Either[E, B])(f: (A, B) => C): Either[E, C] = ???
    def pure[A](a: A): Either[E, A]                                                     = ???
  }

  implicit def validatedApplicative[E: Semigroup]: Applicative[Validated[E, ?]] =
    new DefaultApplicative[Validated[E, ?]] {
      def pure[A](a: A): Validated[E, A]                                                           = ???
      def map2[A, B, C](fa: Validated[E, A], fb: Validated[E, B])(f: (A, B) => C): Validated[E, C] = ???
    }

  implicit def mapApplicative[K]: Applicative[Map[K, ?]] = new DefaultApplicative[Map[K, ?]] {
    def map2[A, B, C](fa: Map[K, A], fb: Map[K, B])(f: (A, B) => C): Map[K, C] = ???
    def pure[A](a: A): Map[K, A]                                               = ???
  }

  implicit def mapApply[K]: Apply[Map[K, ?]] = new Apply[Map[K, ?]] with DefaultFunctor[Map[K, ?]] {
    def map[A, B](fa: Map[K, A])(f: A => B): Map[K, B]                         = mapFunctor[K].map(fa)(f)
    def map2[A, B, C](fa: Map[K, A], fb: Map[K, B])(f: (A, B) => C): Map[K, C] = ???
  }

  implicit val idApplicative: Applicative[Id] = new DefaultApplicative[Id] {
    def map2[A, B, C](fa: Id[A], fb: Id[B])(f: (A, B) => C): Id[C] = ???
    def pure[A](a: A): Id[A]                                       = ???
  }

  implicit def constApplicative[R: Monoid]: Applicative[Const[R, ?]] = new DefaultApplicative[Const[R, ?]] {
    def map2[A, B, C](fa: Const[R, A], fb: Const[R, B])(f: (A, B) => C): Const[R, C] = ???
    def pure[A](a: A): Const[R, A]                                                   = ???
  }

  //  val inc: Int => Int      = _ + 1
  //  val even: Int => Boolean = _ % 2 == 0
  //  val combined: Int => (Int, Boolean) = (inc, even).tuple2
  //  combined(10) // (11, true)
  implicit def functionApplicative[R]: Applicative[R => ?] = new DefaultApplicative[R => ?] {
    def map2[A, B, C](fa: R => A, fb: R => B)(f: (A, B) => C): R => C = ???
    def pure[A](a: A): R => A                                         = ???
  }

  // 2h. Implement an Applicative instance for ZipList
  // such as map2 "zip" the two List instead of doing the cartesian product
  // e.g. map2(ZipList(1,2,3), ZipList(2,2))(_ + _) == ZipList(3,4)
  implicit val zipListApplicative: Applicative[ZipList] = new DefaultApplicative[ZipList] {
    def map2[A, B, C](fa: ZipList[A], fb: ZipList[B])(f: (A, B) => C): ZipList[C] = ???
    def pure[A](a: A): ZipList[A]                                                 = ???
  }

  // 2i. Implement an Apply instance for ZipList
  implicit val zipListApply: Apply[ZipList] = new Apply[ZipList] with DefaultFunctor[ZipList] {
    def map[A, B](fa: ZipList[A])(f: A => B): ZipList[B]                          = ???
    def map2[A, B, C](fa: ZipList[A], fb: ZipList[B])(f: (A, B) => C): ZipList[C] = ???
  }

  // 2j. Why does the default Applicative instance of List do the cartesian product instead of zip?

  // 2k. Implement an Applicative instance for Compose
  // e.g. Applicative[Compose[IO, Either[String, ?], ?]]
  implicit def composeApplicative[F[_]: Applicative, G[_]: Applicative]: Applicative[Compose[F, G, ?]] =
    new DefaultApplicative[Compose[F, G, ?]] {
      def map2[A, B, C](fa: Compose[F, G, A], fb: Compose[F, G, B])(f: (A, B) => C): Compose[F, G, C] = ???
      def pure[A](a: A): Compose[F, G, A]                                                             = ???
    }

  ////////////////////////
  // 3. Monad
  ////////////////////////

  trait DefaultMonad[F[_]] extends Monad[F] with DefaultApplicative[F] {
    //////////////////////////////////////////////////////
    // Within this trait you CANNOT use Monad syntax
    // you CANNOT use:      fa.flatMap(f)
    // instead you need to: flatMap(fa)(f)
    //////////////////////////////////////////////////////

    // 3a. Implement flatten using flatMap
    // such as flatten(List(List(1,2), List(3,4,5)))  == List(1,2,3,4,5)
    //         flatten((x: Int) => (y: Int) => x + y) == (x: Int) => x + x
    def flatten[A](ffa: F[F[A]]): F[A] = ???

    // 3b. Implement ifM using flatMap
    // such as val func = ifM((x: Int) => x > 0)(_ * 2, _.abs)
    //         func(-10) == 10
    //         func(  3) == 6
    // use case:
    // checkUserAccess(userId).ifM(
    //   getUserAccount(userId),
    //   IO.raiseError(new Exception("Insufficient access"))
    // )
    def ifM[A](cond: F[Boolean])(ifTrue: => F[A], ifFalse: => F[A]): F[A] = ???

    // 3c. Implement flatTap using previous functions
    // such as flatTap(Option(10))(x => if(x > 0) unit[Option] else None) == Some(10)
    //         flatTap(Option(-5))(x => if(x > 0) unit[Option] else None) == None
    // use case:
    // getUser(userId).flatTap(user => log.info(s"Fetched $user")): IO[User]
    def flatTap[A, B](fa: F[A])(f: A => F[B]): F[A] = ???

    // 3d. Implement forever using previous functions
    // use case:
    // (getCurrentTime.flatMap(log.info) <* sleep(2.seconds)).forever
    //
    // (for {
    //   event <- queue.pull(1)
    //   _     <- handleRequest(event)
    // yield ()).forever
    //
    def forever[A](fa: F[A]): F[Nothing] = ???

    // 3e. Implement map using flatMap
    override def map[A, B](fa: F[A])(f: A => B): F[B] = ???

    // 3f. Implement map2 using flatMap
    def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] = ???
  }

  // 3g. Implement the following instances
  // you can use methods from the standard library
  implicit val listMonad: Monad[List] = new DefaultMonad[List] {
    def pure[A](a: A): List[A]                               = listApplicative.pure(a)
    def flatMap[A, B](fa: List[A])(f: A => List[B]): List[B] = fa.flatMap(f)
  }

  implicit val optionMonad: Monad[Option] = new DefaultMonad[Option] {
    def pure[A](a: A): Option[A]                                   = optionApplicative.pure(a)
    def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] = ???
  }

  implicit def eitherMonad[E]: Monad[Either[E, ?]] = new DefaultMonad[Either[E, ?]] {
    def pure[A](a: A): Either[E, A]                                         = eitherApplicative.pure(a)
    def flatMap[A, B](fa: Either[E, A])(f: A => Either[E, B]): Either[E, B] = ???
  }

  implicit def validatedMonad[E: Semigroup]: Monad[Validated[E, ?]] = new DefaultMonad[Validated[E, ?]] {
    def pure[A](a: A): Validated[E, A]                                               = validatedApplicative.pure(a)
    def flatMap[A, B](fa: Validated[E, A])(f: A => Validated[E, B]): Validated[E, B] = ???
  }

  implicit def mapFlatMap[K]: FlatMap[Map[K, ?]] = new FlatMap[Map[K, ?]] with DefaultFunctor[Map[K, ?]] {
    def map[A, B](fa: Map[K, A])(f: A => B): Map[K, B]             = mapFunctor[K].map(fa)(f)
    def flatMap[A, B](fa: Map[K, A])(f: A => Map[K, B]): Map[K, B] = ???
  }

  implicit val idMonad: Monad[Id] = new DefaultMonad[Id] {
    def pure[A](a: A): Id[A]                           = idApplicative.pure(a)
    def flatMap[A, B](fa: Id[A])(f: A => Id[B]): Id[B] = ???
  }

  implicit def constMonad[R: Monoid]: Monad[Const[R, ?]] = new DefaultMonad[Const[R, ?]] {
    def pure[A](a: A): Const[R, A]                                       = ???
    def flatMap[A, B](fa: Const[R, A])(f: A => Const[R, B]): Const[R, B] = ???
  }

  //  val even: Int => Boolean = _ % 2 == 0
  //  val inc: Int => Int      = _ + 1
  //  val dec: Int => Int      = _ + 1
  //  val combined: Int => Int = even.flatMap(b => if(b) inc else dec)
  //  combined(10) // 11
  //  combined(11) // 10
  implicit def functionMonad[R]: Monad[Function[R, ?]] = new DefaultMonad[Function[R, ?]] {
    def pure[A](a: A): Function[R, A]                                             = functionApplicative.pure(a)
    def flatMap[A, B](fa: Function[R, A])(f: A => Function[R, B]): Function[R, B] = ???
  }

  // 3h. Implement an Monad instance for Compose
  implicit def composeMonad[F[_]: Monad, G[_]: Monad]: Monad[Compose[F, G, ?]] =
    new DefaultMonad[Compose[F, G, ?]] {
      def pure[A](a: A): Compose[F, G, A]                                                 = ???
      def flatMap[A, B](fa: Compose[F, G, A])(f: A => Compose[F, G, B]): Compose[F, G, B] = ???
    }

  ////////////////////////
  // 4. Traverse
  ////////////////////////

  trait DefaultTraverse[F[_]] extends Traverse[F] with DefaultFunctor[F] {
    //////////////////////////////////////////////////////
    // Within this trait you CANNOT use Traverse syntax
    // you CANNOT use:      fa.traverse(f)
    // instead you need to: traverse(fa)(f)
    //////////////////////////////////////////////////////

    // 4a. Implement traverse using sequence
    def traverse[G[_]: Applicative, A, B](fa: F[A])(f: A => G[B]): G[F[B]] = ???

    // 4b. Implement sequence using traverse
    def sequence[G[_]: Applicative, A](fga: F[G[A]]): G[F[A]] = ???

    // 4c. Implement traverse_ using traverse
    // List(1,3,5).traverse_(a => if(a % 2 == 1) Some(a) else None) == Some(())
    // List(1,2,5).traverse_(a => if(a % 2 == 1) Some(a) else None) == None
    // bonus: try to implement this method using only Foldable methods
    def traverse_[G[_]: Applicative, A, B](fa: F[A])(f: A => G[B]): G[Unit] = ???

    // 4d. Implement foldMapM using traverse
    // List(1,3,5).foldMapM(a => if(a % 2 == 1) Some(a) else None) == Some(9)
    // List(1,2,5).foldMapM(a => if(a % 2 == 1) Some(a) else None) == None
    def foldMapM[G[_]: Applicative, A, B: Monoid](fa: F[A])(f: A => G[B]): G[B] = ???

    // 4e. Implement flatSequence and flatTraverse using previous functions
    def flatSequence[G[_]: Applicative, A](fgfa: F[G[F[A]]])(implicit ev: Monad[F]): G[F[A]] = ???

    def flatTraverse[G[_]: Applicative, A, B](fa: F[A])(f: A => G[F[B]])(implicit ev: Monad[F]): G[F[B]] = ???

    // 4f. Implement map using traverse
    def map[A, B](fa: F[A])(f: A => B): F[B] = ???

    // 4g. Implement foldMap using traverse
    override def foldMap[A, B: Monoid](fa: F[A])(f: A => B): B = ???

    // 4h. Implement foldLeft using foldMap
    // hint: try to use a newtype we saw in chapter 3
    def foldLeft[A, B](fa: F[A], z: B)(f: (B, A) => B): B = ???

    // 4i. Implement foldRight using foldMap
    // hint: try to use a newtype we saw in chapter 3
    def foldRight[A, B](fa: F[A], z: B)(f: (A, => B) => B): B = ???
  }

  // 4j. Implement the following instance
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
    override def traverse[G[_]: Applicative, A, B](fa: Map[K, A])(f: A => G[B]): G[Map[K, B]] = ???
  }

  implicit val idTraverse: Traverse[Id] = new DefaultTraverse[Id] {
    override def traverse[G[_]: Applicative, A, B](fa: Id[A])(f: A => G[B]): G[Id[B]] = ???
  }

  implicit def constTraverse[R]: Traverse[Const[R, ?]] = new DefaultTraverse[Const[R, ?]] {
    override def traverse[G[_]: Applicative, A, B](fa: Const[R, A])(f: A => G[B]): G[Const[R, B]] = ???
  }

  // 4k. Implement parseNumber, try to use traverse, parseDigit and digitsToBigInt
  // such as parseNumber("1052")   == Some(1052)
  //         parseNumber("10xx52") == None
  //         parseNumber("hello")  == None
  def parseNumber(value: String): Option[BigInt] = ???

  def digitsToBigInt(digits: List[Int]): BigInt =
    digits.reverse.zipWithIndex.foldLeft(BigInt(0)) {
      case (acc, (digit, index)) => acc + (digit * BigInt(10).pow(index))
    }

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

  // 4l. Implement checkAllUsersAdult that checks if each user is an adult
  // if all of all users are adults return Right(())
  // if one or more users are not an adult return a failure for the first one
  // bonus: does your implementation fail early? if not, can you make it so?
  def checkAllUsersAdult(users: List[User]): Either[String, Unit] = ???

  sealed trait Country
  case object US      extends Country
  case object France  extends Country
  case object Germany extends Country

  case class User(name: String, age: Int, country: Country)

  def checkUserAdult(user: User): Either[String, Unit] =
    if (user.age < adultAge(user.country)) Left(s"${user.name} is not an adult")
    else Right(())

  def adultAge(country: Country): Int =
    country match {
      case US               => 21
      case Germany | France => 18
    }

  // 4m. Implement checkAllUsersAdult_v2 that checks if each user is an adult
  // if all of all users are adults return Right(())
  // if one or more users are not an adult return a failure for each invalid user
  def checkAllUsersAdult_v2(users: List[User]): Either[NonEmptyList[String], Unit] = ???

  // 4n. Implement getUsers such as it calls sequentially getOneUser
  // if any call to getOneUser fails, getUsers should fail
  // such as getUsers(List("Laura", "Bob")).unsafeRun == List(User("Laura", ...), User("Bob", ...))
  // but     getUsers(List("John", "Chris", "hello")).unsafeRun == throw a serialisation error because Chris fails first
  def getUsers(names: List[String]): IO[List[User]] = ???

  private val db: Map[String, Either[String, User]] =
    Map(
      "John"  -> Right(User("John", 14, France)),
      "Laura" -> Right(User("Laura", 14, France)),
      "Bob"   -> Right(User("Bob", 14, France)),
      "Anna"  -> Left("network issue"),
      "Chris" -> Left("serialisation error"),
    )

  def getOneUser(name: String): IO[User] =
    db.get(name) match {
      case None           => IO.raiseError(new Exception(s"user $name not found"))
      case Some(Left(e))  => IO.raiseError(new Exception(e))
      case Some(Right(a)) => IO.pure(a)
    }

  // 4o. Implement getUsers_v2
  // if an error occurs, it should return it
  // if no error occurs but a user is not found, it should return None
  // such as getUsers_v2(List("Laura", "Bob")).unsafeRun == List(User("Laura", ...), User("Bob", ...))
  // but     getUsers_v2(List("John", "hello", "Chris")).unsafeRun == throw a serialisation error
  //         getUsers_v2(List("John", "hello", "xxx")).unsafeRun   == None
  def getUsers_v2(names: List[String]): IO[Option[List[User]]] = ???

  def getOneUser_v2(name: String): IO[Option[User]] =
    db.get(name) match {
      case None           => IO.pure(None)
      case Some(Left(e))  => IO.raiseError(new Exception(e))
      case Some(Right(a)) => IO.pure(Some(a))
    }

  // 4p. What if we wanted to stop iterating through the list of names if we encountered a user not found?
  // How would you change your implementation?

  // 4q. Implement an Traverse instance for Compose
  // e.g. Traverse[Compose[List, Option, ?]]
  implicit def composeTraverse[F[_]: Traverse, G[_]: Traverse]: Traverse[Compose[F, G, ?]] =
    new DefaultTraverse[Compose[F, G, ?]] {
      override def traverse[H[_]: Applicative, A, B](fa: Compose[F, G, A])(f: A => H[B]): H[Compose[F, G, B]] = ???
    }
}
