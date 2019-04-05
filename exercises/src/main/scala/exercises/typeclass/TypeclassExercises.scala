package exercises.typeclass

import cats.data.NonEmptyList
import cats.kernel.Order
import Monoid.syntax._

import scala.annotation.tailrec

object TypeclassApp extends App {
  import TypeclassExercises._

  println(0.3.combine(10.7))
  println("foo" |+| "bar")
}

object TypeclassExercises {

  // 1. Basic instances
  implicit val int: Monoid[Int] = new Monoid[Int] {
    def combine(a1: Int, a2: Int): Int = a1 + a2
    def empty: Int = 0
  }

  implicit val doubleMonoid: Monoid[Double] = new Monoid[Double] {
    def combine(a1: Double, a2: Double): Double = a1 + a2
    def empty: Double = 0.0
  }

  implicit val string: Monoid[String] = new Monoid[String] {
    def combine(a1: String, a2: String): String = a1 + a2
    def empty: String = ""
  }

  // 1a. Implement an instance of Monoid for Long
  implicit val long: Monoid[Long] = new Monoid[Long] {
    def combine(a1: Long, a2: Long): Long = ???
    def empty: Long = ???
  }

  // 1b. Implement an instance of Monoid for MyId
  implicit val myIdMonoid: Monoid[MyId] = new Monoid[MyId] {
    def combine(a1: MyId, a2: MyId): MyId = ???
    def empty: MyId = ???
  }

  // 1c. Implement an instance of Monoid for  List
  implicit def listMonoid[A]: Monoid[List[A]] = new Monoid[List[A]] {
    def combine(a1: List[A], a2: List[A]): List[A] = ???
    def empty: List[A] = ???
  }

  // 1d. Implement an instance of Monoid for (Int, String)
  implicit val intAndStringMonoid: Monoid[(Int, String)] = new Monoid[(Int, String)] {
    def combine(a1: (Int, String), a2: (Int, String)): (Int, String) = ???
    def empty: (Int, String) = ???
  }

  // 1e. Implement an instance of Monoid for (A, B)
  implicit def tuple2Monoid[A, B]: Monoid[(A, B)] = new Monoid[(A, B)] {
    def combine(a1: (A, B), a2: (A, B)): (A, B) = ???
    def empty: (A, B) = ???
  }

  // 1f. Implement an instance of Monoid for Either[Int, String]
  implicit val intOrStringMonoid: Monoid[Either[Int, String]] = new Monoid[Either[Int, String]] {
    def combine(a1: Either[Int, String], a2: Either[Int, String]): Either[Int, String] = ???
    def empty: Either[Int, String] = ???
  }


  // 2. Monoid usage
  def fold[A](fa: List[A])(implicit ev: Monoid[A]): A = {
    @tailrec
    def loop(xs: List[A], acc: A): A = xs match {
      case Nil     => acc
      case x :: xs => loop(xs, ev.combine(acc, x))
    }

    loop(fa, ev.empty)
  }


  val words = List("Monoid", "are", "awesome", "!")
  val upTo10 = 0.to(10).toList

  // 2a. Use fold to sum up Int
  def sum(xs: List[Int]): Int = ???

  // 2b. Use fold to calculate the average word length
  // e.g. averageWordLength(List("", "ab", "abcd")) == 2.0
  def averageWordLength(xs: List[String]): Double = ???


  // 2c. Implement intercalate
  // such as intercalate(List("my", "hello", "world"), "x:", "--", ":x") == "x:my--hello--world:x"
  def intercalate[A](xs: List[A], before: A, between: A, after: A)(implicit ev: Monoid[A]): A = ???


  // 2d. Implement a simpler version of intercalate where before and after is empty
  def intercalate[A](xs: List[A], between: A)(implicit ev: Monoid[A]): A = ???


  // 2e. Implement csvFormat using intercalate such as
  // scsvFormat(List("foo", "bar", "buzz")) == "foo;bar;buzz"
  def scsvFormat(xs: List[String]): String = ???

  // 2f. Implement tupleFormat using intercalate such as
  // tupleFormat(List("foo", "bar")) == "(foo,bar)"
  def tupleFormat(xs: List[String]): String = ???


  // 2g. Implement folddMap
  // such as folddMap(List("abc", "a", "abcde"))(_.size) == 9
  def foldMap[A, B](xs: List[A])(f: A => B)(implicit ev: Monoid[B]): B = ???


  // 2h. Implement charSequence such as
  // charSequence(List("foo", "bar")) == List('f','o','o','b','a','r')
  def charSequence(xs: List[String]): List[Char] = ???


  // 2i. Re-implement fold in terms of foldMap
  def fold2[A](xs: List[A])(implicit ev: Monoid[A]): A = ???


  // 3. Typeclass hierarchy

  // 3a. Implement an instance of Monoid for NonEmptyList
  implicit def nelMonoid[A]: Monoid[NonEmptyList[A]] = new Monoid[NonEmptyList[A]] {
    def combine(a1: NonEmptyList[A], a2: NonEmptyList[A]): NonEmptyList[A] = ???
    def empty: NonEmptyList[A] = ???
  }


  // 3b. A NonEmptyList can be concatenated yet we cannot implement a Monoid instance
  // What can we do to make NonEmptyList fit in?


  // 4. Typeclass laws and advanced instances

  // 4a. Implement an instance of Monoid for Boolean
  implicit val booleanMonoid: Monoid[Boolean] = new Monoid[Boolean] {
    def combine(a1: Boolean, a2: Boolean): Boolean = ???
    def empty: Boolean = ???
  }

  // 4b. Implement an instance of Monoid for Option
  implicit def optionMonoid[A]: Monoid[Option[A]] = new Monoid[Option[A]] {
    def combine(a1: Option[A], a2: Option[A]): Option[A] = ???
    def empty: Option[A] = ???
  }

  // 4c. Implement an instance of Monoid for Map
  implicit def mapMonoid[K, A]: Monoid[Map[K, A]] = new Monoid[Map[K, A]] {
    def combine(a1: Map[K, A], a2: Map[K, A]): Map[K, A] = ???
    def empty: Map[K, A] = ???
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
