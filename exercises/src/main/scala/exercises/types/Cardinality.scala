package exercises.types

import cats.implicits._
import answers.types.TypeAnswers
import exercises.types.Card._
import exercises.types.TypeExercises.{Branch, Func, One, Pair, Zero}

// Inspired by https://typelevel.org/blog/2018/11/02/semirings.html
trait Cardinality[A] {
  def cardinality: Card
}

sealed trait Card {

  def +(other: Card): Card = Plus(this, other)
  def *(other: Card): Card = Times(this, other)
  def ^(other: Card): Card = Pow(this, other)

  def eval: Option[BigInt] =
    simplify match {
      case Lit(x)      => x.some
      case Plus(x, y)  => (x.eval, y.eval).mapN(_ + _)
      case Times(x, y) => (x.eval, y.eval).mapN(_ * _)
      case Pow(x, y)   => (x.eval, y.eval.filter(_.isValidInt).map(_.toInt)).mapN(_ pow _)
      case Inf         => None
    }

  def simplify: Card =
    this match {
      case Lit(_) => this
      case Plus(x, y) =>
        val xs = x.simplify
        val ys = y.simplify
        if (xs == Lit(0)) ys
        else if (ys == Lit(0)) xs
        else if (xs == Inf || ys == Inf) Inf
        else Plus(xs, ys)
      case Times(x, y) =>
        val xs = x.simplify
        val ys = y.simplify
        if (xs == Lit(0) || ys == Lit(0)) Lit(0)
        else if (xs == Lit(1)) ys
        else if (ys == Lit(1)) xs
        else if (xs == Inf || ys == Inf) Inf
        else Times(xs, ys)
      case Pow(x, y) =>
        val xs = x.simplify
        val ys = y.simplify
        if (ys == Lit(0)) Lit(1)
        else if (xs == Lit(0) || xs == Lit(1)) xs
        else if (xs == Inf || ys == Inf) Inf
        else Pow(xs, ys)
      case Inf => Inf
    }

  override def toString: String = simplify match {
    case Lit(x)      => x.toString()
    case Plus(x, y)  => s"($x + $y)"
    case Times(x, y) => s"($x * $y)"
    case Pow(x, y)   => s"($x ^ $y)"
    case Inf         => "∞"
  }
}

object Card {
  case class Lit(value: BigInt)          extends Card
  case class Plus(lhs: Card, rhs: Card)  extends Card
  case class Times(lhs: Card, rhs: Card) extends Card
  case class Pow(lhs: Card, rhs: Card)   extends Card
  case object Inf                        extends Card
}

object Cardinality {
  def of[A: Cardinality]: Card = apply[A].cardinality

  def apply[A: Cardinality]: Cardinality[A] = implicitly

  //////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////
  //////
  //////
  //////
  //////   SPOILER: don't look below before completing TypeExercises
  //////
  //////
  //////
  //////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////

  implicit def tuple2Cardinality[A: Cardinality, B: Cardinality]: Cardinality[(A, B)] =
    TypeAnswers.tuple2(implicitly, implicitly)

  implicit def eitherCardinality[A: Cardinality, B: Cardinality]: Cardinality[Either[A, B]] =
    TypeAnswers.either(implicitly, implicitly)

  implicit def functionCardinality[A: Cardinality, B: Cardinality]: Cardinality[A => B] =
    TypeAnswers.func(implicitly, implicitly)

  implicit def option[A: Cardinality]: Cardinality[Option[A]] =
    TypeAnswers.option(implicitly)

  implicit val booleanCardinality: Cardinality[Boolean] =
    TypeAnswers.boolean

  implicit val longCardinality: Cardinality[Long] = new Cardinality[Long] {
    def cardinality: Card = Lit(2) ^ Lit(64)
  }

  implicit val intCardinality: Cardinality[Int] =
    TypeAnswers.int

  implicit val shortCardinality: Cardinality[Short] = new Cardinality[Short] {
    def cardinality: Card = Lit(2) ^ Lit(16)
  }

  implicit val byteCardinality: Cardinality[Byte] =
    TypeAnswers.byte

  implicit val unitCardinality: Cardinality[Unit] =
    TypeAnswers.unit

  implicit val nothingCardinality: Cardinality[Nothing] =
    TypeAnswers.nothing

  implicit val anyCardinality: Cardinality[Any] =
    TypeAnswers.any

  implicit def pairCardinality[A: Cardinality, B: Cardinality]: Cardinality[Pair[A, B]] =
    new Cardinality[Pair[A, B]] {
      def cardinality: Card = Cardinality.of[(A, B)]
    }

  implicit def branchCardinality[A: Cardinality, B: Cardinality]: Cardinality[Branch[A, B]] =
    new Cardinality[Branch[A, B]] {
      def cardinality: Card = Cardinality.of[Either[A, B]]
    }

  implicit def funcCardinality[A: Cardinality, B: Cardinality]: Cardinality[Func[A, B]] =
    new Cardinality[Func[A, B]] {
      def cardinality: Card = Cardinality.of[A => B]
    }

  implicit val zeroCardinality: Cardinality[Zero] =
    new Cardinality[Zero] {
      def cardinality: Card = Cardinality.of[Nothing]
    }

  implicit val oneCardinality: Cardinality[One.type] =
    new Cardinality[One.type] {
      def cardinality: Card = Cardinality.of[Unit]
    }
}
