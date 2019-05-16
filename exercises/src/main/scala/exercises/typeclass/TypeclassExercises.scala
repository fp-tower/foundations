package exercises.typeclass

import cats.data.NonEmptyList
import exercises.typeclass.Eq.syntax._
import exercises.typeclass.Foldable.syntax._
import exercises.typeclass.Monoid.syntax._
import exercises.typeclass.Semigroup.syntax._
import org.scalacheck.{Arbitrary, Prop}
import toimpl.typeclass.TypeclassToImpl

import scala.annotation.tailrec

object TypeclassApp extends App {
  import TypeclassExercises._

  println(2.combine(10))
  println("foo" |+| "bar")
}

object TypeclassExercises extends TypeclassToImpl {

  /////////////////////////////
  // 1. Monoid Instances
  /////////////////////////////

  implicit val intMonoid: Monoid[Int] = new Monoid[Int] {
    def combine(x: Int, y: Int): Int = x + y
    def empty: Int                   = 0
  }

  implicit val stringMonoid: Monoid[String] = new Monoid[String] {
    def combine(x: String, y: String): String = x + y
    def empty: String                         = ""
  }

  // 1a. Implement an instance of Monoid for Long
  implicit val longMonoid: Monoid[Long] = new Monoid[Long] {
    def combine(x: Long, y: Long): Long = ???
    def empty: Long                     = ???
  }

  // 1b. Implement an instance of Monoid for List
  // such as combine(List(1,2,3), List(4,5)) == List(1,2,3,4,5)
  implicit def listMonoid[A]: Monoid[List[A]] = new Monoid[List[A]] {
    def combine(x: List[A], y: List[A]): List[A] = ???
    def empty: List[A]                           = ???
  }

  // 1c. Implement an instance of Monoid for Vector
  // such as combine(Vector(1,2,3), Vector(4,5)) == Vector(1,2,3,4,5)
  implicit def vectorMonoid[A]: Monoid[Vector[A]] = new Monoid[Vector[A]] {
    def combine(x: Vector[A], y: Vector[A]): Vector[A] = ???
    def empty: Vector[A]                               = ???
  }

  // 1d Implement an instance of Monoid for Set
  // such as combine(Set(1,2,3), Set(3,4,5)) == Set(1,2,3,4,5)
  implicit def setMonoid[A]: Monoid[Set[A]] = new Monoid[Set[A]] {
    def combine(x: Set[A], y: Set[A]): Set[A] = ???
    def empty: Set[A]                         = ???
  }

  // 1e. Implement an instance of Monoid for (Int, String)
  // such as combine((5, "hello"), (4, "world")) == (9, "helloworld")
  implicit val intAndStringMonoid: Monoid[(Int, String)] = new Monoid[(Int, String)] {
    def combine(x: (Int, String), y: (Int, String)): (Int, String) = ???
    def empty: (Int, String)                                       = ???
  }

  // 1f. Implement an instance of Monoid for (A, B)
  implicit def tuple2Monoid[A: Monoid, B: Monoid]: Monoid[(A, B)] = new Monoid[(A, B)] {
    def combine(x: (A, B), y: (A, B)): (A, B) = ???
    def empty: (A, B)                         = ???
  }

  // 1g. Implement an instance of Monoid for Either[Int, String]
  implicit val intOrStringMonoid: Monoid[Either[Int, String]] = new Monoid[Either[Int, String]] {
    def combine(x: Either[Int, String], y: Either[Int, String]): Either[Int, String] = ???
    def empty: Either[Int, String]                                                   = ???
  }

  // 1h. Implement an instance of Monoid for Option
  // such as combine(Some(3), Some(4)) == Some(7)
  // but     combine(Some(3), None   ) == Some(3)
  // and     combine(None   , Some(4)) == Some(4)
  implicit def optionMonoid[A: Semigroup]: Monoid[Option[A]] = new Monoid[Option[A]] {
    def combine(x: Option[A], y: Option[A]): Option[A] = ???
    def empty: Option[A]                               = ???
  }

  // 1i. Implement an instance of Monoid for Map
  // such as combine(Map("abc" -> 3, "xxx" -> 5), Map("xxx" -> 2, "aaa" -> 1)) == Map("abc" -> 3, "xxx" -> 7, "aaa" -> 1)
  implicit def mapMonoid[K, A: Semigroup]: Monoid[Map[K, A]] = new Monoid[Map[K, A]] {
    def combine(x: Map[K, A], y: Map[K, A]): Map[K, A] = ???
    def empty: Map[K, A]                               = ???
  }

  // 1j. Implement an instance of Monoid for Unit
  implicit val unitMonoid: Monoid[Unit] = new Monoid[Unit] {
    def combine(x: Unit, y: Unit): Unit = ???
    def empty: Unit                     = ???
  }

  // 1k. Implement an instance of Monoid for Nothing
  implicit val nothingMonoid: Monoid[Nothing] = new Monoid[Nothing] {
    def combine(x: Nothing, y: Nothing): Nothing = ???
    def empty: Nothing                           = ???
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

  val words  = List("Monoid", "are", "awesome", "!")
  val upTo10 = 0.to(10).toList

  // 2a. Use fold to sum up a List of Int
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

  // 2g. Implement foldMap
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
    def empty: String                         = ???
  }

  // 3b. What will be the result of foldWords?
  val foldWords = fold(List("hello", "world", "", ""))

  // 3c. Can you think of properties for Monoid that will fail for instances like stringSpaceMonoid
  // Implement your ideas in monoidLaws and verify all instances we defined so far are valid
  // Try to be as restrictive possible
  def monoidLaws[A: Arbitrary: Monoid: Eq]: RuleSet = {
    val p = Monoid[A]

    new SimpleRuleSet("Monoid",
                      "associative" -> Prop.forAll((x: A, y: A, z: A) => ((x |+| y) |+| z) === (x |+| (y |+| z))),
                      "fail"        -> Prop.forAll((a: A) => ???))
  }

  // 3d. combine from Monoid is also associative, x combine (y combine z) == (x combine y) combine z
  // this property is very useful to split the work in several batches
  // e.g. fold(List(1,2, ..., 100, 101, ..., 200, 201, ..., 300)) ==
  //      fold(List(1,2, ..., 100)) combine fold(List(101, ..., 200)) combine fold(List(301, ..., 300))
  def splitFold[A: Monoid](xs: List[A])(split: List[A] => List[List[A]]): A = ???

  // 3e. What other property do you think would be useful to parallelize work?
  // is it satisfied by any instance defined so far?

  /////////////////////////////
  // 4. Instance uniqueness
  /////////////////////////////

  // 4a. Can you implement a lawful Monoid for Int multiplication?
  // can you think of other lawful Monoid for Int?
  val productIntMonoid: Monoid[Int] = new Monoid[Int] {
    def combine(x: Int, y: Int): Int = ???
    def empty: Int                   = ???
  }

  // 4b. Implement a lawful instance of Monoid for Boolean
  // how many different instances can you think of?
  // what about Option?
  val booleanMonoid: Monoid[Boolean] = new Monoid[Boolean] {
    def combine(x: Boolean, y: Boolean): Boolean = ???
    def empty: Boolean                           = ???
  }

  // 4c. Implement an instance of Monoid for Product
  // such as combine(Product(3), Product(5)) == Product(15)
  // Use Product to implement product
  implicit val productMonoid: Monoid[Product] = new Monoid[Product] {
    def combine(x: Product, y: Product): Product = ???
    def empty: Product                           = ???
  }

  def product(xs: List[Int]): Int = ???

  // 4e. Implement an instance of Monoid for All
  // such as combine(All(true), All(true))  == All(true)
  //         combine(All(true), All(false)) == All(false)
  // Use All to implement forAll
  implicit val allMonoid: Monoid[All] = new Monoid[All] {
    def combine(x: All, y: All): All = ???
    def empty: All                   = ???
  }

  def forAll(xs: List[Boolean]): Boolean = ???

  // 4f. Implement an instance of Monoid for Endo
  // such as combine(inc, double)(5) == 11
  // Use Endo to implement pipe
  implicit def endoMonoid[A]: Monoid[Endo[A]] = new Monoid[Endo[A]] {
    def combine(x: Endo[A], y: Endo[A]): Endo[A] = ???
    def empty: Endo[A]                           = ???
  }

  def pipe[A](xs: List[A => A]): A => A = ???

  /////////////////////////////
  // 5. Typeclass hierarchy
  ////////////////////////////

  // 5a. Implement an instance of Monoid for NonEmptyList
  implicit def nelMonoid[A]: Monoid[NonEmptyList[A]] = new Monoid[NonEmptyList[A]] {
    def combine(x: NonEmptyList[A], y: NonEmptyList[A]): NonEmptyList[A] = ???
    def empty: NonEmptyList[A]                                           = ???
  }

  // 5b. A NonEmptyList can be concatenated but we cannot implement a Monoid instance
  // change Monoid to extend Semigroup and implement an instance for NonEmptyList
  implicit def nelSemigroup[A]: Semigroup[NonEmptyList[A]] = new Semigroup[NonEmptyList[A]] {
    def combine(x: NonEmptyList[A], y: NonEmptyList[A]): NonEmptyList[A] = ???
  }

  // 5c. What laws Semigroup should satisfy? Implement them in semigroupLaws
  def semigroupLaws[A: Arbitrary: Semigroup: Eq]: RuleSet = {
    val p = Semigroup[A]

    new SimpleRuleSet("Semigroup", "example" -> Prop.forAll((a: A) => a === a), "fail" -> Prop.forAll((a: A) => ???))
  }

  // 5d. Implement reduceMap
  // such as reduceMap(List("", "Hi", "World"))(_.size) == Some(7)
  // such as reduceMap(List.empty[String])(_.size) == None
  def reduceMap[A, B: Semigroup](xs: List[A])(f: A => B): Option[B] = ???

  // 5e. Implement an instance of Semigroup for Min
  // such as combine(Min(8), Min(0)) == Min(0)
  // Use Min it to implement minOptionList
  implicit def minSemigroup[A: Ordering]: Semigroup[Min[A]] = new Semigroup[Min[A]] {
    def combine(x: Min[A], y: Min[A]): Min[A] = ???
  }

  def minOptionList[A: Ordering](xs: List[A]): Option[A] = ???

  // 5f. Implement an instance of Semigroup for First
  // such as combine(First("hello"), First("world")) == First("hello")
  // Use First to implement headOptionList
  implicit def firstSemigroup[A]: Semigroup[First[A]] = new Semigroup[First[A]] {
    def combine(x: First[A], y: First[A]): First[A] = ???
  }

  def headOptionList[A](xs: List[A]): Option[A] = ???

  // 5g. Implement an instance of Semigroup for Dual
  // such as combine(Dual(1), Dual(2)) == Dual(combine(2, 1))
  // Use Dual to implement lastOptionList
  implicit def dualSemigroup[A: Semigroup]: Semigroup[Dual[A]] = new Semigroup[Dual[A]] {
    def combine(x: Dual[A], y: Dual[A]): Dual[A] = ???
  }

  def lastOptionList[A: Ordering](xs: List[A]): Option[A] = ???

  // 5h. What would be the effect of foldMap(xs: List[A])(Dual(_))
  // when A is String?
  // When A is Int?
  // Encode this specific behaviour in strongMonoidLaws
  def strongMonoidLaws[A: Arbitrary: StrongMonoid: Eq]: RuleSet = {
    val p = StrongMonoid[A]

    new DefaultRuleSet("StrongMonoid", Some(monoidLaws[A]), "additional law" -> Prop.forAll((a: A) => ???))
  }

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
    def foldLeft[A, B](fa: List[A], z: B)(f: (B, A) => B): B     = ???
    def foldRight[A, B](fa: List[A], z: B)(f: (A, => B) => B): B = ???
  }

  // 6f. Implement Foldable instance for Option
  implicit val optionFoldable: Foldable[Option] = new Foldable[Option] {
    def foldLeft[A, B](fa: Option[A], z: B)(f: (B, A) => B): B     = ???
    def foldRight[A, B](fa: Option[A], z: B)(f: (A, => B) => B): B = ???
  }

  // 6g. Implement Foldable instance for Option
  implicit def eitherFoldable[E]: Foldable[Either[E, ?]] = new Foldable[Either[E, ?]] {
    def foldLeft[A, B](fa: Either[E, A], z: B)(f: (B, A) => B): B     = ???
    def foldRight[A, B](fa: Either[E, A], z: B)(f: (A, => B) => B): B = ???
  }

  // 6h. Implement Foldable instance for Map
  implicit def mapFoldable[K]: Foldable[Map[K, ?]] = new Foldable[Map[K, ?]] {
    def foldLeft[A, B](fa: Map[K, A], z: B)(f: (B, A) => B): B     = ???
    def foldRight[A, B](fa: Map[K, A], z: B)(f: (A, => B) => B): B = ???
  }

  // 6i. Implement isEmptyF
  // such as isEmptyF(List(1,2,3)) == false
  //         isEmptyF(Nil) == true
  // bonus: can you implement it using foldMap?
  def isEmptyF[F[_]: Foldable, A](fa: F[A]): Boolean = ???

  // 6j. Implement size
  // such as size(Option("hello")) == 1
  //         size(None) == 0
  // bonus: can you implement it using foldMap?
  def size[F[_]: Foldable, A](fa: F[A]): Int = ???

  // 6k. Implement headOption
  // such as headOption(List(1,2,3)) == Some(1)
  //         headOption(Nil) == None
  // bonus: can you implement it using foldMap?
  def headOption[F[_]: Foldable, A](fa: F[A]): Option[A] = ???

  // 6l. Implement lastOption
  // such as lastOption(List(1,2,3)) == Some(3)
  //         lastOption(Nil) == None
  // bonus: can you implement it using reduceMap?
  def lastOption[F[_]: Foldable, A](fa: F[A]): Option[A] = ???

  // 6m. Implement find
  // such as find(List(10, 8, 7, 4, 3))(_ % 2 == 1) == Some(7)
  //         find(List(10, 8, 6, 4, 2))(_ % 2 == 1) == None
  //         find(List.empty[Int])(_ % 2 == 1) == None
  // bonus: can you implement it using foldMap?
  def find[F[_]: Foldable, A](fa: F[A])(p: A => Boolean): Option[A] = ???

  // 6n. Implement minimumOption
  // such as minimumOption(List(5, 6, 2, 8, 0, 1)) == Some(0)
  //         minimumOption(List.empty[Int]) == None
  // bonus: can you implement it using foldMap?
  def minimumOption[F[_]: Foldable, A: Ordering](fa: F[A]): Option[A] = ???

  // 6o. What is the difference between implementing a function inside or outside of Foldable trait?
  // When will it be preferable to do one or the other?

  // 6p. Implement foldLeft in terms of foldMap
  // such as foldLeftFromFoldMap(List(1,2,3,4,5), 0)(_ + _) == 15
  //         foldLeftFromFoldMap(List.empty[Int], 0)(_ + _) == 0
  def foldLeftFromFoldMap[F[_]: Foldable, A, B](fa: F[A], z: B)(f: (B, A) => B): B = ???

  // 6q. Implement foldRight in terms of foldMap
  // such as foldRightFromFoldMap(List(1,2,3,4,5), 0)(_ + _) == 15
  //         foldRightFromFoldMap(List.empty[Int], 0)(_ + _) == 0
  // Is foldRight lazy implemented this way? If no, how can you change it?
  def foldRightFromFoldMap[F[_]: Foldable, A, B](fa: F[A], z: B)(f: (A, => B) => B): B = ???

}
