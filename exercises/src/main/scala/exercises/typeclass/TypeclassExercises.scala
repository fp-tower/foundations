package exercises.typeclass

import cats.data.NonEmptyList
import cats.kernel.{Monoid, Order}

object TypeclassApp extends App {
  import TypeclassExercises._
  import Monoid.syntax._

  println(0.3.plus(10.7))
}

object TypeclassExercises {

  // 1. Basic instances

  // 1a. Implement an instance of Monoid for Double
  implicit val doubleMonoid: Monoid[Double] = new Monoid[Double] {
    def plus(a1: Double, a2: Double): Double = ???
    def zero: Double = ???
  }

  // 1b. Implement an instance of Monoid for MyId
  implicit val myIdMonoid: Monoid[MyId] = new Monoid[MyId] {
    def plus(a1: MyId, a2: MyId): MyId = ???
    def zero: MyId = ???
  }

  // 1c. Implement an instance of Monoid for  List
  implicit def listMonoid[A]: Monoid[List[A]] = new Monoid[List[A]] {
    def plus(a1: List[A], a2: List[A]): List[A] = ???
    def zero: List[A] = ???
  }

  // 1d. Implement an instance of Monoid for (Int, String)
  implicit val intAndStringMonoid: Monoid[(Int, String)] = new Monoid[(Int, String)] {
    def plus(a1: (Int, String), a2: (Int, String)): (Int, String) = ???
    def zero: (Int, String) = ???
  }

  // 1e. Implement an instance of Monoid for (A, B)
  implicit def tuple2Monoid[A, B]: Monoid[(A, B)] = new Monoid[(A, B)] {
    def plus(a1: (A, B), a2: (A, B)): (A, B) = ???
    def zero: (A, B) = ???
  }

  // 1f. Implement an instance of Monoid for Either[Int, String]
  implicit val intOrStringMonoid: Monoid[Either[Int, String]] = new Monoid[Either[Int, String]] {
    def plus(a1: Either[Int, String], a2: Either[Int, String]): Either[Int, String] = ???
    def zero: Either[Int, String] = ???
  }


  // 2. Monoid usage

  // 2a. Implement fold
  def fold[A](xs: List[A])(implicit ev: Monoid[A]): A = ???


  val words = List("Monoid", "are", "awesome", "!")
  val upTo10 = 0.to(10).toList

  // 2b. Use fold to sum up
  def sum(xs: List[Int]): Int = ???

  // 2c. Use fold with tuple2 to calculate the aver
  def averageWordLength(xs: List[String]): Double = ???


  // 2d. Implement intercalate
  def intercalate[A](xs: List[A], before: A, between: A, after: A)(implicit ev: Monoid[A]): A = ???


  def intercalate[A](xs: List[A], between: A)(implicit ev: Monoid[A]): A = ???


  // 2e. Implement csvFormat using intercalate such as
  // scsvFormat(List("foo", "bar", "buzz")) == "foo;bar;buzz"
  def scsvFormat(xs: List[String]): String = ???

  // 2f. Implement tupleFormat using intercalate such as
  // tupleFormat(List("foo", "bar")) == "(foo,bar)"
  def tupleFormat(xs: List[String]): String = ???

  // 2g. Implement folddMap
  def foldMap[A, B](xs: List[A])(f: A => B)(implicit ev: Monoid[B]): B = ???


  // 2h. Implement charSequence such as
  // charSequence(List("foo", "bar")) == List('f','o','o','b','a','r')
  def charSequence(xs: List[String]): List[Char] = ???


  // 2i. Re-implement fold in terms of foldMap
  def fold2[A](xs: List[A])(implicit ev: Monoid[A]): A = ???


  // 3. Typeclass hierarchy

  // 3a. Implement an instance of Monoid for NonEmptyList
  implicit def nelMonoid[A]: Monoid[NonEmptyList[A]] = new Monoid[NonEmptyList[A]] {
    def plus(a1: NonEmptyList[A], a2: NonEmptyList[A]): NonEmptyList[A] = ???
    def zero: NonEmptyList[A] = ???
  }


  // 3b. A NonEmptyList can be concatenated yet we cannot implement a Monoid instance
  // What can we do to make NonEmptyList fit in?


  // 4. Advanced instances

  // 4a. Implement an instance of Monoid for Boolean
  implicit val booleanMonoid: Monoid[Boolean] = new Monoid[Boolean] {
    def plus(a1: Boolean, a2: Boolean): Boolean = ???
    def zero: Boolean = ???
  }

  // 4b. Implement an instance of Monoid for Option
  implicit def optionMonoid[A]: Monoid[Option[A]] = new Monoid[Option[A]] {
    def plus(a1: Option[A], a2: Option[A]): Option[A] = ???
    def zero: Option[A] = ???
  }

  // 4c. Implement an instance of Monoid for Map
  implicit def mapMonoid[K, A]: Monoid[Map[K, A]] = new Monoid[Map[K, A]] {
    def plus(a1: Map[K, A], a2: Map[K, A]): Map[K, A] = ???
    def zero: Map[K, A] = ???
  }

  // 4d. What properties Monoid should have? What can you say about plus and empty for all A?
  // Implement these properties to MonoidLaws and check your instances pass those laws


  // 4e. Refactor String instance of Monoid such as plus add a single space in between
  // e.g. plus("foo", "bar") == plus("foo bar")
  // does it respect the MonoidLaws?


  // 4f. What property Monoid[Int] or Monoid[Boolean] have but say Monoid[String] doesn't
  // Add it to StrongMonoidLaws



  // 5. higher kinded typeclass

  // 5a. Implement foldMap for Vector
  def foldMap[A, B](xs: Vector[A])(f: A => B)(implicit ev: Monoid[B]): B = ???

  // 5b. Implement foldMap for Option
  def foldMap[A, B](xs: Option[A])(f: A => B)(implicit ev: Monoid[B]): B = ???

  // 5c. Implement foldMap for Either
  def foldMap[E, A, B](xs: Either[E, A])(f: A => B)(implicit ev: Monoid[B]): B = ???

  // 5d. Implement foldMap for Map (keys are not used)
  def foldMap[K, A, B](xs: Map[K, A])(f: A => B)(implicit ev: Monoid[B]): B = ???

  // 5e. Implement Foldable instance for List
  implicit val listFoldable: Foldable[List] = new Foldable[List] {
    def foldLeft[A, B](fa: List[A], z: B)(f: (B, A) => B): B = ???
    def foldRight[A, B](fa: List[A], z: B)(f: (A, => B) => B): B = ???
  }

  // 5f. Implement Foldable instance for Option
  implicit val optionFoldable: Foldable[Option] = new Foldable[Option] {
    def foldLeft[A, B](fa: Option[A], z: B)(f: (B, A) => B): B = ???
    def foldRight[A, B](fa: Option[A], z: B)(f: (A, => B) => B): B = ???
  }

  // 5g. Implement Foldable instance for Option
  implicit def eitherFoldable[E]: Foldable[Either[E, ?]] = new Foldable[Either[E, ?]] {
    def foldLeft[A, B](fa: Either[E, A], z: B)(f: (B, A) => B): B = ???
    def foldRight[A, B](fa: Either[E, A], z: B)(f: (A, => B) => B): B = ???
  }

  // 5h. Implement Foldable instance for Map
  implicit def mapFoldable[K]: Foldable[Map[K, ?]] = new Foldable[Map[K, ?]] {
    def foldLeft[A, B](fa: Map[K, A], z: B)(f: (B, A) => B): B = ???
    def foldRight[A, B](fa: Map[K, A], z: B)(f: (A, => B) => B): B = ???
  }

  // 5i. Implement isEmpty
  def isEmpty[F[_], A](fa: F[A])(implicit ev: Foldable[F]): Boolean = ???

  // 5j. Implement size
  def size[F[_], A](fa: F[A])(implicit ev: Foldable[F]): Int = ???

  // 5k. Implement headOption
  // try to implement it using foldMap with a newtype
  def headOption[F[_], A](fa: F[A])(implicit ev: Foldable[F]): Option[A] = ???

  // 5l. Implement find
  def find[F[_], A](fa: F[A])(implicit foldable: Foldable[F]): Option[A] = ???

  // 5m. Implement minimumOption
  def minimumOption[F[_], A](fa: F[A])(implicit foldable: Foldable[F], ev: Order[A]): Option[A] = ???

  // 5n. Implement lookup
  def lookup[F[_], A](fa: F[A], index: Int)(implicit ev: Foldable[F]): Option[A] = ???


  // 5o. What is the difference between implementing a function here or inside Foldable trait?
  // When will it be preferable to do one or the other?


  // 5p. Implement splitReduce which:
  // - split F[A] into several sub-sections then
  // - reduce each sub-section to single "total" value using A using f then
  // - reduce each sub-section "total" value using f
  //
  // What properties do you from F and A? update the signature if required
  def splitReduce[F[_], A](fa: F[A])(split: F[A] => List[F[A]])(f: (A, A) => A): A = ???



}
