package answers.types

import exercises.types.{IntOrBoolean, Point}
import toimpl.types.ACardinality.{Finite, Infinite}
import exercises.types.TypeExercises.{Branch, Func, One, Pair}
import toimpl.types.{ACardinality, Cardinality, TypeToImpl}

object TypeAnswers extends TypeToImpl {

  type Two    = Branch[One, One]
  type Three  = Branch[One, Two]
  type Four_1 = Pair[Two, Two]
  type Four_2 = Branch[Two, Two]
  type Five_1 = Branch[Four_1, One]
  type Five_2 = Branch[Three, Two]
  type Eight  = Func[Three, Two]

  val boolean: Cardinality[Boolean] = new Cardinality[Boolean] {
    def cardinality: ACardinality = Finite(2)
  }

  val unit: Cardinality[Unit] = new Cardinality[Unit] {
    def cardinality: ACardinality = Finite(1)
  }

  val byte: Cardinality[Byte] = new Cardinality[Byte] {
    def cardinality: ACardinality = Finite(BigInt(2).pow(8))
  }

  val char: Cardinality[Char] = new Cardinality[Char] {
    def cardinality: ACardinality = Finite(BigInt(2).pow(16))
  }

  val int: Cardinality[Int] = new Cardinality[Int] {
    def cardinality: ACardinality = Finite(BigInt(2).pow(32))
  }

  val optUnit: Cardinality[Option[Unit]] = new Cardinality[Option[Unit]] {
    def cardinality: ACardinality = unit.cardinality + Finite(1)
  }

  val optBoolean: Cardinality[Option[Boolean]] = new Cardinality[Option[Boolean]] {
    def cardinality: ACardinality = boolean.cardinality + Finite(1)
  }

  val intOrBoolean: Cardinality[IntOrBoolean] = new Cardinality[IntOrBoolean] {
    def cardinality: ACardinality = int.cardinality + boolean.cardinality
  }

  val boolUnit: Cardinality[(Boolean, Unit)] = new Cardinality[(Boolean, Unit)] {
    def cardinality: ACardinality = boolean.cardinality
  }

  val boolByte: Cardinality[(Boolean, Byte)] = new Cardinality[(Boolean, Byte)] {
    def cardinality: ACardinality = boolean.cardinality * byte.cardinality
  }

  val point: Cardinality[Point] = new Cardinality[Point] {
    def cardinality: ACardinality = int.cardinality * int.cardinality
  }

  val listUnit: Cardinality[List[Unit]] = new Cardinality[List[Unit]] {
    def cardinality: ACardinality = Infinite
  }

  val nothing: Cardinality[Nothing] = new Cardinality[Nothing] {
    def cardinality: ACardinality = Finite(0)
  }

  val optNothing: Cardinality[Option[Nothing]] = new Cardinality[Option[Nothing]] {
    def cardinality: ACardinality = nothing.cardinality + Finite(1)
  }

  val boolNothing: Cardinality[(Boolean, Nothing)] = new Cardinality[(Boolean, Nothing)] {
    def cardinality: ACardinality = boolean.cardinality * nothing.cardinality
  }

  def option[A](a: Cardinality[A]): Cardinality[Option[A]] =
    new Cardinality[Option[A]] {
      def cardinality: ACardinality = a.cardinality + Finite(1)
    }

  def list[A](a: Cardinality[A]): Cardinality[List[A]] =
    new Cardinality[List[A]] {
      def cardinality: ACardinality =
        if(a.cardinality == Finite(0)) a.cardinality
        else Infinite
    }

  def either[A, B](a: Cardinality[A], b: Cardinality[B]): Cardinality[Either[A, B]] =
    new Cardinality[Either[A, B]] {
      def cardinality: ACardinality = a.cardinality + b.cardinality
    }

  def tuple2[A, B](a: Cardinality[A], b: Cardinality[B]): Cardinality[(A, B)] =
    new Cardinality[(A, B)] {
      def cardinality: ACardinality = a.cardinality * b.cardinality
    }

  val string: Cardinality[String] = new Cardinality[String] {
    def cardinality: ACardinality =
      0.to(Int.MaxValue).foldLeft(Finite(BigInt(0)): ACardinality)((acc, i) =>
        acc + (char.cardinality * Finite(i))
      )
  }

  def func[A, B](a: Cardinality[A], b: Cardinality[B]): Cardinality[A => B] =
    new Cardinality[A => B] {
      def cardinality: ACardinality = b.cardinality ^ a.cardinality
    }

  def aUnitToA[A](tuple: (A, Unit)): A = tuple._1

  def aToAUnit[A](a: A): (A, Unit) = (a, ())

  def aOrNothingToA[A](either: Either[A, Nothing]): A =
    either match {
      case Left(a)  => a
      case Right(x) => absurd(x)
    }

  def absurd[A](x: Nothing): A = sys.error("Impossible")

  def aToAOrNothing[A](a: A): Either[A, Nothing] =
    Left(a)

  def optionToEitherUnit[A](option: Option[A]): Either[Unit, A] =
    option.toRight(())

  def eitherUnitToOption[A](either: Either[Unit, A]): Option[A] =
    either.fold(_ => None, Some(_))

  def distributeBranchTo[A, B, C](value: (A, Either[B, C])): Either[(A, B), (A, C)] = {
    val (a, bOrC) = value
    bOrC.fold(
      b => Left((a, b)),
      c => Right((a, c))
    )
  }

  def distributeBranchFrom[A, B, C](value: Either[(A, B), (A, C)]): (A, Either[B, C]) =
    value.fold(
      { case (a, b) => (a, Left(b)) },
      { case (a, c) => (a, Right(c)) }
    )
}
