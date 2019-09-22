package answers.types

import java.time.Instant
import java.util.UUID

import answers.types.Comparison._
import cats.data.NonEmptyList
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric._
import exercises.sideeffect.IOExercises.IO
import exercises.types.Card._
import exercises.types.TypeExercises.{Branch, Func, One, Pair}
import exercises.types._
import toimpl.types.TypeToImpl

object TypeAnswers extends TypeToImpl {

  ////////////////////////
  // 2. Data Encoding
  ////////////////////////

  case class OrderId(value: UUID)
  case class Order(id: OrderId, createdAt: Instant, status: OrderStatus)

  sealed trait OrderStatus {
    case class Draft(basket: List[Item], deliveryAddress: Option[Address])                           extends OrderStatus
    case class Submitted(basket: NonEmptyList[Item], deliveryAddress: Address, submittedAt: Instant) extends OrderStatus
    case class Delivered(basket: NonEmptyList[Item],
                         deliveryAddress: Address,
                         submittedAt: Instant,
                         deliveredAt: Instant)
        extends OrderStatus
    case class Cancelled(basket: NonEmptyList[Item], cancelledAt: Instant) extends OrderStatus
  }

  case class ItemId(value: UUID)
  case class Item(id: ItemId, quantity: Long, price: BigDecimal)

  case class Address(streetNumber: Int, postCode: String)

  ////////////////////////
  // 3. Cardinality
  ////////////////////////

  val boolean: Cardinality[Boolean] = new Cardinality[Boolean] {
    def cardinality: Card = Lit(2)
  }

  val int: Cardinality[Int] = new Cardinality[Int] {
    def cardinality: Card = Lit(2) ^ Lit(32)
  }

  val any: Cardinality[Any] = new Cardinality[Any] {
    def cardinality: Card = Inf
  }

  val nothing: Cardinality[Nothing] = new Cardinality[Nothing] {
    def cardinality: Card = Lit(0)
  }

  val unit: Cardinality[Unit] = new Cardinality[Unit] {
    def cardinality: Card = Lit(1)
  }

  val ioUnit: Cardinality[IO[Unit]] = new Cardinality[IO[Unit]] {
    def cardinality: Card = Inf
  }

  val byte: Cardinality[Byte] = new Cardinality[Byte] {
    def cardinality: Card = Lit(2) ^ Lit(8)
  }

  val char: Cardinality[Char] = new Cardinality[Char] {
    def cardinality: Card = Lit(2) ^ Lit(16)
  }

  val optUnit: Cardinality[Option[Unit]] = new Cardinality[Option[Unit]] {
    def cardinality: Card = unit.cardinality + Lit(1)
  }

  val optBoolean: Cardinality[Option[Boolean]] = new Cardinality[Option[Boolean]] {
    def cardinality: Card = boolean.cardinality + Lit(1)
  }

  val intOrBoolean: Cardinality[IntOrBoolean] = new Cardinality[IntOrBoolean] {
    def cardinality: Card = int.cardinality + boolean.cardinality
  }

  val boolUnit: Cardinality[(Boolean, Unit)] = new Cardinality[(Boolean, Unit)] {
    def cardinality: Card = boolean.cardinality
  }

  val boolByte: Cardinality[(Boolean, Byte)] = new Cardinality[(Boolean, Byte)] {
    def cardinality: Card = boolean.cardinality * byte.cardinality
  }

  val intAndBoolean: Cardinality[IntAndBoolean] = new Cardinality[IntAndBoolean] {
    def cardinality: Card = int.cardinality * boolean.cardinality
  }

  val listUnit: Cardinality[List[Unit]] = new Cardinality[List[Unit]] {
    def cardinality: Card = Inf
  }

  val optNothing: Cardinality[Option[Nothing]] = new Cardinality[Option[Nothing]] {
    def cardinality: Card = nothing.cardinality + Lit(1)
  }

  val boolNothing: Cardinality[(Boolean, Nothing)] = new Cardinality[(Boolean, Nothing)] {
    def cardinality: Card = boolean.cardinality * nothing.cardinality
  }

  def option[A](a: Cardinality[A]): Cardinality[Option[A]] =
    new Cardinality[Option[A]] {
      def cardinality: Card = a.cardinality + Lit(1)
    }

  def list[A](a: Cardinality[A]): Cardinality[List[A]] =
    new Cardinality[List[A]] {
      def cardinality: Card =
        if (a.cardinality == Lit(0)) Lit(1)
        else Inf
    }

  def either[A, B](a: Cardinality[A], b: Cardinality[B]): Cardinality[Either[A, B]] =
    new Cardinality[Either[A, B]] {
      def cardinality: Card = a.cardinality + b.cardinality
    }

  def tuple2[A, B](a: Cardinality[A], b: Cardinality[B]): Cardinality[(A, B)] =
    new Cardinality[(A, B)] {
      def cardinality: Card = a.cardinality * b.cardinality
    }

  val string: Cardinality[String] = new Cardinality[String] {
    def cardinality: Card =
      0.to(Int.MaxValue).foldLeft(Lit(BigInt(0)): Card)((acc, i) => acc + (char.cardinality ^ Lit(i)))
  }

  def func[A, B](a: Cardinality[A], b: Cardinality[B]): Cardinality[A => B] =
    new Cardinality[A => B] {
      def cardinality: Card = b.cardinality ^ a.cardinality
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
      }, {
        case Left((a, b))  => (a, Left(b))
        case Right((a, c)) => (a, Right(c))
      }
    )

  def isAdult(age: Int): Boolean = age >= 18

  def isAdult_v2(i: Int Refined Positive): Boolean =
    i.value >= 18

  def compareInt(x: Int, y: Int): Int =
    if (x < y) -1
    else if (x > y) 1
    else 0

  def compareInt_v2(x: Int, y: Int): Comparison =
    if (x < y) LessThan
    else if (x > y) GreaterThan
    else EqualTo

  type Two    = Branch[One, One]
  type Three  = Branch[One, Two]
  type Four_1 = Pair[Two, Two]
  type Four_2 = Branch[Two, Two]
  type Five_1 = Branch[Four_1, One]
  type Five_2 = Branch[Three, Two]
  type Eight  = Func[Three, Two]

}
