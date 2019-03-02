package exercises.types

import exercises.types.ACardinality.{Finite, Infinite}
import exercises.types.TypeExercises.{Branch, Func, One, Pair, Zero}

// Inspired from https://typelevel.org/blog/2018/11/02/semirings.html
trait Cardinality[A] {
  def cardinality: ACardinality
}

object ACardinality {
  case class Finite(value: BigInt) extends ACardinality
  case object Infinite extends ACardinality
}

sealed trait ACardinality {
  def *(other: ACardinality): ACardinality =
    binOp(other)((a, b) => Finite(a * b))

  def +(other: ACardinality): ACardinality =
    binOp(other)((a, b) => Finite(a + b))

  def ^(other: ACardinality): ACardinality =
    binOp(other) { (a, b) =>
      if(a == 0) Finite(0)
      else if(a == 1) Finite(a)
      else if(b.isValidInt) Finite(a.pow(b.toInt))
      else Infinite // TODO more precise, maybe capture Pow(x, y)
    }

  private def binOp(other: ACardinality)(f: (BigInt, BigInt) => ACardinality): ACardinality =
    (this, other) match {
      case (Finite(x), Finite(y)) => f(x, y)
      case (Finite(_), Infinite) | (Infinite, Finite(_)) | (Infinite, Infinite) => Infinite
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
    new Cardinality[(A, B)] {
      def cardinality: ACardinality = Cardinality.of[A] * Cardinality.of[B]
    }

  implicit def eitherCardinality[A: Cardinality, B: Cardinality]: Cardinality[Either[A, B]] =
    new Cardinality[Either[A, B]] {
      def cardinality: ACardinality =
        Cardinality.of[A] + Cardinality.of[B]
    }

  implicit def functionCardinality[A: Cardinality, B: Cardinality]: Cardinality[A => B] =
    new Cardinality[A => B] {
      def cardinality: ACardinality = Cardinality.of[B] ^ Cardinality.of[A]
    }

  implicit def option[A: Cardinality]: Cardinality[Option[A]] =
    new Cardinality[Option[A]] {
      def cardinality: ACardinality = Cardinality.of[A] + Finite(1)
    }

  implicit val booleanCardinality: Cardinality[Boolean] = new Cardinality[Boolean] {
    def cardinality: ACardinality = Finite(BigInt(2))
  }

  implicit val longCardinality: Cardinality[Long] = new Cardinality[Long] {
    def cardinality: ACardinality = Finite(BigInt(2).pow(64))
  }

  implicit val intCardinality: Cardinality[Int] = new Cardinality[Int] {
    def cardinality: ACardinality = Finite(BigInt(2).pow(32))
  }

  implicit val shortCardinality: Cardinality[Short] = new Cardinality[Short] {
    def cardinality: ACardinality = Finite(BigInt(2).pow(16))
  }

  implicit val byteCardinality: Cardinality[Byte] = new Cardinality[Byte] {
    def cardinality: ACardinality = Finite(BigInt(2).pow(8))
  }

  implicit val unitCardinality: Cardinality[Unit] = new Cardinality[Unit] {
    def cardinality: ACardinality = Finite(1)
  }

  implicit val nothingCardinality: Cardinality[Nothing] = new Cardinality[Nothing] {
    def cardinality: ACardinality = Finite(0)
  }

  implicit val anyCardinality: Cardinality[Any] = new Cardinality[Any] {
    def cardinality: ACardinality = Infinite
  }

  implicit def pairCardinality[A: Cardinality, B: Cardinality]: Cardinality[Pair[A, B]] =
    new Cardinality[Pair[A, B]] {
      def cardinality: ACardinality = Cardinality.of[A] * Cardinality.of[B]
    }

  implicit def branchCardinality[A: Cardinality, B: Cardinality]: Cardinality[Branch[A, B]] =
    new Cardinality[Branch[A, B]] {
      def cardinality: ACardinality = Cardinality.of[A] + Cardinality.of[B]
    }

  implicit def funcCardinality[A: Cardinality, B: Cardinality]: Cardinality[Func[A, B]] =
    new Cardinality[Func[A, B]] {
      def cardinality: ACardinality = Cardinality.of[B] ^ Cardinality.of[A]
    }

  implicit val zeroCardinality: Cardinality[Zero] =
    new Cardinality[Zero] {
      def cardinality: ACardinality = Finite(0)
    }

  implicit val oneCardinality: Cardinality[One.type] =
    new Cardinality[One.type] {
      def cardinality: ACardinality = Finite(1)
    }
}
