package answers.types

import answers.types.Comparison._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric._
import exercises.types.ACardinality.{Finite, Infinite}
import exercises.types.TypeExercises.{Branch, Func, One, Pair}
import exercises.types._
import toimpl.types.TypeToImpl

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

  val intAndBoolean: Cardinality[IntAndBoolean] = new Cardinality[IntAndBoolean] {
    def cardinality: ACardinality = int.cardinality * boolean.cardinality
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

  val any: Cardinality[Any] = new Cardinality[Any] {
    def cardinality: ACardinality = Infinite
  }

  def option[A](a: Cardinality[A]): Cardinality[Option[A]] =
    new Cardinality[Option[A]] {
      def cardinality: ACardinality = a.cardinality + Finite(1)
    }

  def list[A](a: Cardinality[A]): Cardinality[List[A]] =
    new Cardinality[List[A]] {
      def cardinality: ACardinality =
        if(a.cardinality == Finite(0)) Finite(1)
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
        acc + (char.cardinality ^ Finite(i))
      )
  }

  def func[A, B](a: Cardinality[A], b: Cardinality[B]): Cardinality[A => B] =
    new Cardinality[A => B] {
      def cardinality: ACardinality = b.cardinality ^ a.cardinality
    }


  def aUnitToA[A]: Iso[(A, Unit), A] = Iso(_._1, (_, ()))

  def aOrNothingToA[A]: Iso[Either[A, Nothing], A] =
    Iso(_.fold(identity, absurd), Left(_))

  def absurd[A](x: Nothing): A = sys.error("Impossible")

  def optionToEitherUnit[A]: Iso[Option[A], Either[Unit, A]] =
    Iso(_.toRight(()), _.fold(_ => None, Some(_)))

  def power1[A]: Iso[Unit => A, A] =
    Iso(f => f(()), a => _ => a)

  def distributeTuple[A, B, C]: Iso[(A, Either[B, C]), Either[(A, B), (A, C)]] =
    Iso(
      {
        case (a, bOrC) =>
          bOrC.fold(
            b => Left((a, b)),
            c => Right((a, c))
          )
      },
      {
        case Left((a, b))  => (a, Left(b))
        case Right((a, c)) => (a, Right(c))
      }
    )

  def isAdult(age: Int): Boolean = age >= 18


  def isAdult_v2(i: Int Refined Positive): Boolean =
    i.value >= 18

  def compareInt(x: Int, y: Int): Int =
    if(x < y) -1
    else if(x > y) 1
    else 0

  def compareInt_v2(x: Int, y: Int): Comparison = {
    if(x < y)       LessThan
    else if (x > y) GreaterThan
    else            EqualTo
  }
}
