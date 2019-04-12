package exercises.typeclass

import cats.data.NonEmptyList
import Monoid.syntax._
import toimpl.typeclass.TypeclassToImpl

import scala.annotation.tailrec

object TypeclassApp extends App {
  import TypeclassExercises._

  println(0.3.combine(10.7))
  println("foo" |+| "bar")
}

object TypeclassExercises extends TypeclassToImpl {

  /////////////////////////////
  // 1. Monoid Instances
  /////////////////////////////

  implicit val intMonoid: Monoid[Int] = new Monoid[Int] {
    def combine(x: Int, y: Int): Int = x + y
    def empty: Int = 0
  }

  implicit val doubleMonoid: Monoid[Double] = new Monoid[Double] {
    def combine(x: Double, y: Double): Double = x + y
    def empty: Double = 0.0
  }

  implicit val stringMonoid: Monoid[String] = new Monoid[String] {
    def combine(x: String, y: String): String = x + y
    def empty: String = ""
  }

  // 1a. Implement an instance of Monoid for Unit
  implicit val unitMonoid: Monoid[Unit] = new Monoid[Unit] {
    def combine(x: Unit, y: Unit): Unit = ???
    def empty: Unit = ???
  }

  // 1c. Implement an instance of Monoid for  List, Vector, Set
  implicit def listMonoid[A]: Monoid[List[A]] = new Monoid[List[A]] {
    def combine(x: List[A], y: List[A]): List[A] = ???
    def empty: List[A] = ???
  }

  implicit def vectorMonoid[A]: Monoid[Vector[A]] = new Monoid[Vector[A]] {
    def combine(x: Vector[A], y: Vector[A]): Vector[A] = ???
    def empty: Vector[A] = ???
  }

  implicit def setMonoid[A]: Monoid[Set[A]] = new Monoid[Set[A]] {
    def combine(x: Set[A], y: Set[A]): Set[A] = ???
    def empty: Set[A] = ???
  }

  // 1d. Implement an instance of Monoid for (Int, String)
  // such as combine((5, "hello"), (4, "world")) == (9, "helloworld")
  implicit val intAndStringMonoid: Monoid[(Int, String)] = new Monoid[(Int, String)] {
    def combine(x: (Int, String), y: (Int, String)): (Int, String) = ???
    def empty: (Int, String) = ???
  }

  // 1e. Implement an instance of Monoid for (A, B)
  implicit def tuple2Monoid[A: Monoid, B: Monoid]: Monoid[(A, B)] = new Monoid[(A, B)] {
    def combine(x: (A, B), y: (A, B)): (A, B) = ???
    def empty: (A, B) = ???
  }

  // 1f. Implement an instance of Monoid for Either[Int, String]
  implicit val intOrStringMonoid: Monoid[Either[Int, String]] = new Monoid[Either[Int, String]] {
    def combine(x: Either[Int, String], y: Either[Int, String]): Either[Int, String] = ???
    def empty: Either[Int, String] = ???
  }

  // 1g. Implement an instance of Monoid for Option
  // such as combine(Some(3), Some(4)) == Some(7)
  // but     combine(Some(3), None   ) == None
  // and     combine(None   , Some(4)) == None
  implicit def optionMonoid[A: Semigroup]: Monoid[Option[A]] = new Monoid[Option[A]] {
    def combine(x: Option[A], y: Option[A]): Option[A] = ???
    def empty: Option[A] = ???
  }

  // 1h. Implement an instance of Monoid for Map
  // such as combine(Map("abc" -> 3, "xxx" -> 5), Map("xxx" -> 2, "aaa" -> 1)) == Map("abc" -> 3, "xxx" -> 7, "aaa" -> 1)
  implicit def mapMonoid[K, A: Semigroup]: Monoid[Map[K, A]] = new Monoid[Map[K, A]] {
    def combine(x: Map[K, A], y: Map[K, A]): Map[K, A] = ???
    def empty: Map[K, A] = ???
  }

  /////////////////////////////
  // 2. Monoid usage
  /////////////////////////////

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
  // e.g. averageWordLength(List("a", "ab", "abcd", "abc")) == 2.5
  def averageWordLength(xs: List[String]): Double = ???


  // 2c. Implement isEmpty
  // such as isEmpty(0) == true, isEmpty(5) == false
  //         isEmpty("") == true, isEmpty("hello") == false
  def isEmpty[A: Monoid](x: A): Boolean = ???


  // 2d. Implement ifEmpty
  // such as ifEmpty("")("hello") == "hello"
  //         ifEmpty("bar")("hello") == "bar"
  def ifEmpty[A: Monoid](x: A)(other: => A): A = ???


  // 2e. Implement repeat
  // such as repeat(3)("hello") == "hellohellohello"
  //         repeat(0)("hello") == ""
  //         repeat(2)(3)       == 6
  def repeat[A: Monoid](n: Int)(x: A): A = ???


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
  // such as foldMap(List("abc", "a", "abcde"))(_.size) == 9
  def foldMap[A, B](xs: List[A])(f: A => B)(implicit ev: Monoid[B]): B = ???


  // 2h. Implement charSequence such as
  // charSequence(List("foo", "bar")) == List('f','o','o','b','a','r')
  def charSequence(xs: List[String]): List[Char] = ???


  // 2i. Re-implement fold in terms of foldMap
  def fold2[A](xs: List[A])(implicit ev: Monoid[A]): A = ???


  /////////////////////////////
  // 3. Typeclass laws
  /////////////////////////////

  // 3a. Implement another Monoid for String where combine add an empty space between words
  // e.g. combine("hello", "world")
  val stringSpaceMonoid: Monoid[String] = new Monoid[String] {
    def combine(x: String, y: String): String = ???
    def empty: String = ???
  }

  // 3b. What will be the result of foldWords?
  val foldWords = fold(List("hello", "world", "", ""))


  // 3c. Can you think of properties for Monoid that will fail for instances like stringSpaceMonoid
  // Implement your ideas in MonoidLaws and verify all instances we defined so far are valid
  // Try to be as restrictive possible


  // 3d. combine from Monoid is also associative, x combine (y combine z) == (x combine y) combine z
  // this property is very useful to split the work in several batches
  // e.g. fold(List(1,2, ..., 100, 101, ..., 200, 201, ..., 300)) ==
  //      fold(List(1,2, ..., 100)) combine fold(List(101, ..., 200)) combine fold(List(301, ..., 300))
  def splitFold[A: Monoid](xs: List[A])(split: List[A] => List[List[A]]): A = ???


  // 3e. Wht other property do you think would be useful to parallelize work?
  // is it satisfied by any instance defined so far?

  /////////////////////////////
  // 4. Instance uniqueness
  /////////////////////////////

  // 4a. Can you implement a lawful Monoid for Int multiplication?
  // can you think of other lawful Monoid for Int?
  val productIntMonoid: Monoid[Int] = new Monoid[Int] {
    def combine(x: Int, y: Int): Int = ???
    def empty: Int = ???
  }

  // 4b. Implement a lawful instance of Monoid for Boolean
  // how many different instances can you think of?
  val booleanMonoid: Monoid[Boolean] = new Monoid[Boolean] {
    def combine(x: Boolean, y: Boolean): Boolean = ???
    def empty: Boolean = ???
  }

  // 4c. Implement an instance of Monoid for Product
  // and use it to implement product
  implicit val productMonoid: Monoid[Product] = new Monoid[Product] {
    def combine(x: Product, y: Product): Product = ???
    def empty: Product = ???
  }

  def product(xs: List[Int]): Int = ???

  // 4e. Implement an instance of Monoid for All
  // and use it to implement forAll
  implicit val allMonoid: Monoid[All] = new Monoid[All] {
    def combine(x: All, y: All): All = ???
    def empty: All = ???
  }

  def forAll(xs: List[Boolean]): Boolean = ???

  // 4f. Implement an instance of Monoid for Endo
  // and use it to implement pipe
  implicit def endoMonoid[A]: Monoid[Endo[A]] = new Monoid[Endo[A]] {
    def combine(x: Endo[A], y: Endo[A]): Endo[A] = ???
    def empty: Endo[A] = ???
  }

  def pipe[A](xs: List[A => A]): A => A = ???


  /////////////////////////////
  // 5. Typeclass hierarchy
  ////////////////////////////

  // 5a. Implement an instance of Monoid for NonEmptyList
  implicit def nelMonoid[A]: Monoid[NonEmptyList[A]] = new Monoid[NonEmptyList[A]] {
    def combine(x: NonEmptyList[A], y: NonEmptyList[A]): NonEmptyList[A] = ???
    def empty: NonEmptyList[A] = ???
  }



  // 5b. A NonEmptyList can be concatenated but we cannot implement a Monoid instance
  // change Monoid to extend Semigroup and implement an instance for NonEmptyList
  implicit def nelSemigroup[A]: Semigroup[NonEmptyList[A]] = new Semigroup[NonEmptyList[A]] {
    def combine(x: NonEmptyList[A], y: NonEmptyList[A]): NonEmptyList[A] = ???
  }

  // 5c. What laws Semigroup should satisfy? Implement them in SemigroupLaws


  // 5d. Implement reduceMap
  // also foldMap(xs)(f) == reduceMap(xs)(f).getOrElse(mempty)
  def reduceMap[A, B: Semigroup](xs: List[A])(f: A => B): Option[B] = ???


  // 5e. Implement an instance of Semigroup for Min
  // and use it to implement minOption
  implicit def minSemigroup[A: Ordering]: Semigroup[Min[A]] = new Semigroup[Min[A]] {
    def combine(x: Min[A], y: Min[A]): Min[A] = ???
  }

  def minOption[A: Ordering](xs: List[A]): Option[A] = ???


  // 5f. Implement an instance of Semigroup for First
  // and use it to implement headOption
  implicit def firstSemigroup[A]: Semigroup[First[A]] = new Semigroup[First[A]] {
    def combine(x: First[A], y: First[A]): First[A] = ???
  }

  def headOption[A](xs: List[A]): Option[A] = ???


  // 5g. Implement an instance of Semigroup for Dual
  // and use it to implement maxOption and lastOption
  implicit def dualSemigroup[A: Semigroup]: Semigroup[Dual[A]] = new Semigroup[Dual[A]] {
    def combine(x: Dual[A], y: Dual[A]): Dual[A] = ???
  }

  def lastOption[A: Ordering](xs: List[A]): Option[A] = ???



  // 5h. What would be the effect of foldMap(xs: List[A])(Dual(_))
  // when A is String?
  // When A is Int?



  //////////////////////////////
  // 6. Higher kinded typeclass
  //////////////////////////////


  // 6a. Implement foldMap for Vector
  def foldMap[A, B](xs: Vector[A])(f: A => B)(implicit ev: Monoid[B]): B = ???

  // 6b. Implement foldMap for Option
  def foldMap[A, B](xs: Option[A])(f: A => B)(implicit ev: Monoid[B]): B = ???

  // 6c. Implement foldMap for Either
  def foldMap[E, A, B](xs: Either[E, A])(f: A => B)(implicit ev: Monoid[B]): B = ???

  // 6d. Implement foldMap for Map (keys are not used)
  def foldMap[K, A, B](xs: Map[K, A])(f: A => B)(implicit ev: Monoid[B]): B = ???

  // 6e. Implement Foldable instance for List
  implicit val listFoldable: Foldable[List] = new Foldable[List] {
    def foldLeft[A, B](fa: List[A], z: B)(f: (B, A) => B): B = ???
    def foldRight[A, B](fa: List[A], z: B)(f: (A, => B) => B): B = ???
  }

  // 6f. Implement Foldable instance for Option
  implicit val optionFoldable: Foldable[Option] = new Foldable[Option] {
    def foldLeft[A, B](fa: Option[A], z: B)(f: (B, A) => B): B = ???
    def foldRight[A, B](fa: Option[A], z: B)(f: (A, => B) => B): B = ???
  }

  // 6g. Implement Foldable instance for Option
  implicit def eitherFoldable[E]: Foldable[Either[E, ?]] = new Foldable[Either[E, ?]] {
    def foldLeft[A, B](fa: Either[E, A], z: B)(f: (B, A) => B): B = ???
    def foldRight[A, B](fa: Either[E, A], z: B)(f: (A, => B) => B): B = ???
  }

  // 6h. Implement Foldable instance for Map
  implicit def mapFoldable[K]: Foldable[Map[K, ?]] = new Foldable[Map[K, ?]] {
    def foldLeft[A, B](fa: Map[K, A], z: B)(f: (B, A) => B): B = ???
    def foldRight[A, B](fa: Map[K, A], z: B)(f: (A, => B) => B): B = ???
  }

  // 6i. Implement isEmpty
  def isEmpty[F[_], A](fa: F[A])(implicit ev: Foldable[F]): Boolean = ???

  // 6j. Implement size
  def size[F[_], A](fa: F[A])(implicit ev: Foldable[F]): Int = ???

  // 6k. Implement headOption
  // try to implement it using foldMap with a newtype
  def headOption[F[_], A](fa: F[A])(implicit ev: Foldable[F]): Option[A] = ???

  // 6l. Implement find
  def find[F[_], A](fa: F[A])(implicit foldable: Foldable[F]): Option[A] = ???

  // 6m. Implement minimumOption
  def minimumOption[F[_], A](fa: F[A])(implicit foldable: Foldable[F], ev: Ordering[A]): Option[A] = ???

  // 6n. Implement lookup
  def lookup[F[_], A](fa: F[A], index: Int)(implicit ev: Foldable[F]): Option[A] = ???


  // 6o. What is the difference between implementing a function inside or outside of Foldable trait?
  // When will it be preferable to do one or the other?


  // 6p. Implement foldLeft in terms of foldMap
  def foldLeftFromFoldMap[F[_]: Foldable, A, B](fa: F[A], z: B)(f: (B, A) => B): B = ???

  // 6q. Implement foldRight in terms of foldMap
  // Is foldRight lazy implemented this way? If no, how can you change it?
  def foldRightFromFoldMap[F[_]: Foldable, A, B](fa: F[A], z: B)(f: (A, => B) => B): B = ???

}
