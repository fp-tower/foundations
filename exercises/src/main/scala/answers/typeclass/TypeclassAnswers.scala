package answers.typeclass

import cats.data.NonEmptyList
import exercises.typeclass.Eq.syntax._
import exercises.typeclass.Foldable.syntax._
import exercises.typeclass.Monoid.syntax._
import exercises.typeclass.Semigroup.syntax._
import exercises.typeclass._
import org.scalacheck.{Arbitrary, Prop}
import toimpl.typeclass.TypeclassToImpl

import scala.annotation.tailrec
import scala.math.Ordering.Implicits._

object TypeclassAnswers extends TypeclassToImpl {

  /////////////////////////////
  // 1. Monoid Instances
  /////////////////////////////

  implicit val intMonoid: Monoid[Int] = new Monoid[Int] {
    def combine(x: Int, y: Int): Int = x + y
    def empty: Int                   = 0
  }

  implicit val doubleMonoid: Monoid[Double] = new Monoid[Double] {
    def combine(x: Double, y: Double): Double = x + y
    def empty: Double                         = 0.0
  }

  implicit val stringMonoid: Monoid[String] = new Monoid[String] {
    def combine(x: String, y: String): String = x + y
    def empty: String                         = ""
  }

  implicit val longMonoid: Monoid[Long] = new Monoid[Long] {
    def combine(x: Long, y: Long): Long = x + y
    def empty: Long                     = 0L
  }

  implicit def listMonoid[A]: Monoid[List[A]] = new Monoid[List[A]] {
    def combine(x: List[A], y: List[A]): List[A] = x ++ y
    def empty: List[A]                           = Nil
  }

  implicit def vectorMonoid[A]: Monoid[Vector[A]] = new Monoid[Vector[A]] {
    def combine(x: Vector[A], y: Vector[A]): Vector[A] = x ++ y
    def empty: Vector[A]                               = Vector.empty
  }

  implicit def setMonoid[A]: Monoid[Set[A]] = new Monoid[Set[A]] {
    def combine(x: Set[A], y: Set[A]): Set[A] = x ++ y
    def empty: Set[A]                         = Set.empty
  }

  implicit val intAndStringMonoid: Monoid[(Int, String)] = tuple2Monoid[Int, String]

  implicit def tuple2Monoid[A: Monoid, B: Monoid]: Monoid[(A, B)] = new Monoid[(A, B)] {
    def combine(x: (A, B), y: (A, B)): (A, B) = (x._1 |+| y._1, x._2 |+| y._2)
    def empty: (A, B)                         = (mempty[A], mempty[B])
  }

  implicit def optionMonoid[A: Semigroup]: Monoid[Option[A]] = new Monoid[Option[A]] {
    def combine(x: Option[A], y: Option[A]): Option[A] =
      (x, y) match {
        case (Some(a1), Some(a2)) => Some(Semigroup[A].combine(a1, a2))
        case _                    => x.orElse(y)
      }
    def empty: Option[A] = None
  }

  implicit def mapMonoid[K, A: Semigroup]: Monoid[Map[K, A]] = new Monoid[Map[K, A]] {
    def combine(x: Map[K, A], y: Map[K, A]): Map[K, A] =
      (x.keySet ++ y.keySet)
        .map(k => k -> (x.get(k) |+| y.get(k)))
        .collect { case (k, Some(v)) => k -> v }
        .toMap

    def empty: Map[K, A] = Map.empty
  }

  implicit val unitMonoid: Monoid[Unit] = new Monoid[Unit] {
    def combine(x: Unit, y: Unit): Unit = ()
    def empty: Unit                     = ()
  }

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

  def sum(xs: List[Int]): Int = fold(xs)

  def averageWordLength(xs: List[String]): Double =
    if (xs.isEmpty) 0.0 else fold(xs.map(_.length)) / xs.size.toDouble

  def isEmpty[A: Monoid](x: A): Boolean =
    Monoid[A].empty == x

  def ifEmpty[A: Monoid](x: A)(other: => A): A =
    if (isEmpty(x)) other else x

  def repeat[A: Monoid](n: Int)(x: A): A =
    fold(List.fill(n)(x))

  def intercalate[A](xs: List[A], before: A, between: A, after: A)(implicit ev: Monoid[A]): A =
    if (xs.isEmpty) mempty[A]
    else fold(before +: xs.init.map(_ |+| between) :+ xs.last :+ after)

  def intercalate[A](xs: List[A], between: A)(implicit ev: Monoid[A]): A =
    intercalate(xs, mempty[A], between, mempty[A])

  def scsvFormat(xs: List[String]): String =
    intercalate(xs, ";")

  def tupleFormat(xs: List[String]): String =
    intercalate(xs, "(", ",", ")")

  def foldMap[A, B](fa: List[A])(f: A => B)(implicit ev: Monoid[B]): B = {
    @tailrec
    def loop(xs: List[A], acc: B): B = xs match {
      case Nil     => acc
      case x :: xs => loop(xs, ev.combine(acc, f(x)))
    }

    loop(fa, ev.empty)
  }

  def charSequence(xs: List[String]): List[Char] =
    foldMap(xs)(_.toList)

  def fold2[A](xs: List[A])(implicit ev: Monoid[A]): A =
    foldMap(xs)(identity)

  /////////////////////////////
  // 3. Typeclass laws
  /////////////////////////////

  val stringSpaceMonoid: Monoid[String] = new Monoid[String] {
    def combine(x: String, y: String): String = s"$x $y"
    def empty: String                         = ""
  }

  def monoidLaws[A: Arbitrary: Monoid: Eq]: RuleSet =
    new DefaultRuleSet(
      "Monoid",
      Some(semigroupLaws[A]),
      "left identity" ->
        Prop.forAll((a: A) => (mempty[A] |+| a) === a),
      "right identity" ->
        Prop.forAll((a: A) => (a |+| mempty[A]) === a)
    )

  def splitFold[A: Monoid](xs: List[A])(split: List[A] => List[List[A]]): A =
    fold(split(xs).map(fold(_)))

  /////////////////////////////
  // 4. Instance uniqueness
  /////////////////////////////

  val productIntMonoid: Monoid[Int] = new Monoid[Int] {
    def combine(x: Int, y: Int): Int = x * y
    def empty: Int                   = 1
  }

  val booleanMonoid: Monoid[Boolean] = new Monoid[Boolean] {
    def combine(x: Boolean, y: Boolean): Boolean = x || y
    def empty: Boolean                           = false
  }

  implicit val productMonoid: Monoid[Product] = new Monoid[Product] {
    def combine(x: Product, y: Product): Product = Product(x.getProduct * y.getProduct)
    def empty: Product                           = Product(1)
  }

  def product(xs: List[Int]): Int = foldMap(xs)(Product(_)).getProduct

  def forAll(xs: List[Boolean]): Boolean = foldMap(xs)(All(_)).getAll

  implicit val allMonoid: Monoid[All] = new Monoid[All] {
    def combine(x: All, y: All): All = All(x.getAll && y.getAll)
    def empty: All                   = All(true)
  }

  implicit def endoMonoid[A]: Monoid[Endo[A]] = new Monoid[Endo[A]] {
    def combine(x: Endo[A], y: Endo[A]): Endo[A] = Endo(x.getEndo compose y.getEndo)
    def empty: Endo[A]                           = Endo(identity)
  }

  def pipe[A](xs: List[A => A]): A => A = foldMap(xs)(Endo(_)).getEndo

  /////////////////////////////
  // 5. Typeclass hierarchy
  ////////////////////////////

  implicit def nelSemigroup[A]: Semigroup[NonEmptyList[A]] = new Semigroup[NonEmptyList[A]] {
    def combine(x: NonEmptyList[A], y: NonEmptyList[A]): NonEmptyList[A] = x ::: y
  }

  def semigroupLaws[A: Arbitrary: Semigroup: Eq]: RuleSet =
    new SimpleRuleSet(
      "Semigroup",
      "associative" ->
        Prop.forAll((x: A, y: A, z: A) => ((x |++| y) |++| z) === (x |++| (y |++| z)))
    )

  def reduceMap[A, B: Semigroup](fa: List[A])(f: A => B): Option[B] =
    fa match {
      case Nil     => None
      case x :: xs => Some(xs.foldLeft(f(x))((acc, a) => Semigroup[B].combine(acc, f(a))))
    }

  implicit def minSemigroup[A: Ordering]: Semigroup[Min[A]] = new Semigroup[Min[A]] {
    def combine(x: Min[A], y: Min[A]): Min[A] =
      if (x.getMin < y.getMin) x else y
  }

  def minOptionList[A: Ordering](xs: List[A]): Option[A] =
    reduceMap(xs)(Min(_)).map(_.getMin)

  implicit def firstSemigroup[A]: Semigroup[First[A]] = new Semigroup[First[A]] {
    def combine(x: First[A], y: First[A]): First[A] = x
  }

  def headOptionList[A](xs: List[A]): Option[A] =
    reduceMap(xs)(First(_)).map(_.getFirst)

  implicit def dualSemigroup[A: Semigroup]: Semigroup[Dual[A]] = new Semigroup[Dual[A]] {
    def combine(x: Dual[A], y: Dual[A]): Dual[A] = Dual(Semigroup[A].combine(y.getDual, x.getDual))
  }

  implicit def dualMonoid[A: Monoid]: Monoid[Dual[A]] = new Monoid[Dual[A]] {
    def empty: Dual[A]                           = Dual(Monoid[A].empty)
    def combine(x: Dual[A], y: Dual[A]): Dual[A] = Dual(Semigroup[A].combine(y.getDual, x.getDual))
  }

  def lastOptionList[A: Ordering](xs: List[A]): Option[A] =
    reduceMap(xs)(x => Dual(First(x))).map(_.getDual.getFirst)

  def strongMonoidLaws[A: Arbitrary: StrongMonoid: Eq]: RuleSet =
    new DefaultRuleSet(
      "StrongMonoid",
      Some(monoidLaws[A]),
      "commutative" ->
        Prop.forAll((x: A, y: A) => (x |+| y) === (y |+| x))
    )

  //////////////////////////////
  // 6. Higher kinded typeclass
  //////////////////////////////

  def foldMap[A, B: Monoid](xs: Vector[A])(f: A => B) =
    xs.foldLeft(Monoid[B].empty)((acc, a) => acc |+| f(a))

  def foldMap[A, B](xs: Option[A])(f: A => B)(implicit ev: Monoid[B]): B =
    xs.fold(Monoid[B].empty)(f)

  def foldMap[E, A, B](xs: Either[E, A])(f: A => B)(implicit ev: Monoid[B]): B =
    xs.fold(_ => Monoid[B].empty, f)

  def foldMap[K, A, B](xs: Map[K, A])(f: A => B)(implicit ev: Monoid[B]): B =
    xs.foldLeft(Monoid[B].empty) { case (acc, (_, a)) => acc |+| f(a) }

  implicit val listFoldable: Foldable[List] = new Foldable[List] {
    def foldLeft[A, B](fa: List[A], z: B)(f: (B, A) => B): B = fa.foldLeft(z)(f)
    def foldRight[A, B](fa: List[A], z: B)(f: (A, => B) => B): B =
      fa match {
        case Nil     => z
        case x :: xs => f(x, foldRight(xs, z)(f))
      }
  }

  implicit val optionFoldable: Foldable[Option] = new Foldable[Option] {
    def foldLeft[A, B](fa: Option[A], z: B)(f: (B, A) => B): B     = fa.fold(z)(f(z, _))
    def foldRight[A, B](fa: Option[A], z: B)(f: (A, => B) => B): B = fa.fold(z)(f(_, z))
  }

  implicit def eitherFoldable[E]: Foldable[Either[E, ?]] = new Foldable[Either[E, ?]] {
    def foldLeft[A, B](fa: Either[E, A], z: B)(f: (B, A) => B): B     = fa.fold(_ => z, f(z, _))
    def foldRight[A, B](fa: Either[E, A], z: B)(f: (A, => B) => B): B = fa.fold(_ => z, f(_, z))
  }

  implicit def mapFoldable[K]: Foldable[Map[K, ?]] = new Foldable[Map[K, ?]] {
    def foldLeft[A, B](fa: Map[K, A], z: B)(f: (B, A) => B): B     = fa.foldLeft(z) { case (acc, (_, a)) => f(acc, a) }
    def foldRight[A, B](fa: Map[K, A], z: B)(f: (A, => B) => B): B = listFoldable.foldRight(fa.values.toList, z)(f)
  }

  def isEmptyF[F[_]: Foldable, A](fa: F[A]): Boolean =
    fa.foldMap(_ => All(false)).getAll

  def size[F[_]: Foldable, A](fa: F[A]): Int =
    fa.foldMap(_ => 1)

  def headOption[F[_]: Foldable, A](fa: F[A]): Option[A] =
    fa.reduceMap(First(_)).map(_.getFirst)

  def lastOption[F[_]: Foldable, A](fa: F[A]): Option[A] =
    fa.reduceMap(x => Dual(First(x))).map(_.getDual.getFirst)

  def find[F[_]: Foldable, A](fa: F[A])(p: A => Boolean): Option[A] =
    fa.foldMap(a => if (p(a)) Option(First(a)) else None).map(_.getFirst)

  def minimumOption[F[_]: Foldable, A: Ordering](fa: F[A]): Option[A] =
    fa.reduceMap(Min(_)).map(_.getMin)

  def foldLeftFromFoldMap[F[_]: Foldable, A, B](fa: F[A], z: B)(f: (B, A) => B): B =
    fa.foldMap(a => Endo[B](b => f(b, a))).getEndo(z)

  def foldRightFromFoldMap[F[_]: Foldable, A, B](fa: F[A], z: B)(f: (A, => B) => B): B =
    fa.foldMap(a => Endo[B](b => f(a, b))).getEndo(z)
}
