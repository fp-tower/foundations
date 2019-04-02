package exercises.types

import answers.types.TypeAnswers
import exercises.types.ACardinality.{Finite, Infinite}
import exercises.types.TypeExercises.{Branch, Func, One, Pair, Zero}

// Inspired by https://typelevel.org/blog/2018/11/02/semirings.html
trait Cardinality[A] {
  def cardinality: ACardinality
}

object ACardinality {
  case class Finite(value: BigInt) extends ACardinality
  case object Infinite extends ACardinality
}

sealed trait ACardinality {
  def *(other: ACardinality): ACardinality =
    (this, other) match {
      case (Finite(x), Finite(y)) => Finite(x * y)
      case (Finite(x), Infinite ) => if(x == 0) Finite(0) else Infinite
      case (Infinite , Finite(x)) => if(x == 0) Finite(0) else Infinite
      case (Infinite , Infinite ) => Infinite
    }

  def +(other: ACardinality): ACardinality =
    (this, other) match {
      case (Finite(x), Finite(y)) => Finite(x + y)
      case (Finite(_), Infinite) | (Infinite, Finite(_)) | (Infinite, Infinite) => Infinite
    }

  def ^(other: ACardinality): ACardinality =
    (this, other) match {
      case (Finite(x), Finite(y)) =>
        if(x == 0 || x == 1) Finite(x)
        else if(y == 0) Finite(1)
        else if(y.isValidInt) Finite(x.pow(y.toInt))
        else Infinite // TODO more precise, maybe capture Pow(x, y)
      case (Finite(x), Infinite ) => if(x == 0 || x == 1) Finite(x) else Infinite
      case (Infinite , Finite(x)) => if(x == 0) Finite(1) else Infinite
      case (Infinite , Infinite ) => Infinite
    }

  override def toString: String = this match {
    case Finite(x) => x.toString()
    case Infinite  => "∞"
  }
}



object Cardinality {
  def of[A: Cardinality]: ACardinality = apply[A].cardinality

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
    def cardinality: ACardinality = Finite(BigInt(2).pow(64))
  }

  implicit val intCardinality: Cardinality[Int] =
    TypeAnswers.int

  implicit val shortCardinality: Cardinality[Short] = new Cardinality[Short] {
    def cardinality: ACardinality = Finite(BigInt(2).pow(16))
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
      def cardinality: ACardinality = Cardinality.of[(A, B)]
    }

  implicit def branchCardinality[A: Cardinality, B: Cardinality]: Cardinality[Branch[A, B]] =
    new Cardinality[Branch[A, B]] {
      def cardinality: ACardinality = Cardinality.of[Either[A, B]]
    }

  implicit def funcCardinality[A: Cardinality, B: Cardinality]: Cardinality[Func[A, B]] =
    new Cardinality[Func[A, B]] {
      def cardinality: ACardinality = Cardinality.of[A => B]
    }

  implicit val zeroCardinality: Cardinality[Zero] =
    new Cardinality[Zero] {
      def cardinality: ACardinality = Cardinality.of[Nothing]
    }

  implicit val oneCardinality: Cardinality[One.type] =
    new Cardinality[One.type] {
      def cardinality: ACardinality = Cardinality.of[Unit]
    }
}
