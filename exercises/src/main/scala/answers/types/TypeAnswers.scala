package answers.types

import java.time.Instant
import java.util.UUID

import answers.types.Comparison._
import cats.data.NonEmptyList
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric._
import eu.timepit.refined.types.numeric.PosInt
import exercises.sideeffect.IOExercises.IO
import exercises.types.Card._
import exercises.types.{Card, Cardinality, Iso}

object TypeAnswers {

  ////////////////////////
  // 1. Misused types
  ////////////////////////

  def compareChar(x: Char, y: Char): Comparison =
    if (x < y) LessThan
    else if (x > y) GreaterThan
    else EqualTo

  case class BlogPost(id: String, title: String, createAt: Instant)

  def mostRecentBlogs(n: Int)(blogs: List[BlogPost]): List[BlogPost] =
    blogs.sortBy(_.createAt).take(n)

  def mostRecentBlogs_V2(n: PosInt)(blogs: List[BlogPost]): List[BlogPost] =
    blogs.sortBy(_.createAt).take(n.value)

  case class User(name: String, address: Option[UserAddress])

  case class UserAddress(streetNumber: Int, streetName: String, postCode: String) {
    def fullAddress: String = s"$streetNumber $streetName $postCode"
  }

  case class InvoiceItem(id: String, quantity: Int, price: Double) {
    def value: Double = quantity * price
  }
  case class Invoice(id: String, items: NonEmptyList[InvoiceItem]) {
    def total: Double = items.toList.map(_.value).sum

    def totalWithDiscountedFirstItem(discountPercent: Double): Double = {
      val discountedItem    = items.head.copy(price = items.head.price * (1 - discountPercent))
      val discountedInvoice = copy(items = NonEmptyList(discountedItem, items.tail))
      discountedInvoice.total
    }
  }

  def getItemCount(invoice: Invoice): Int =
    invoice.items.map(_.quantity).toList.sum

  ////////////////////////
  // 2. Data Encoding
  ////////////////////////

  case class OrderId(value: UUID)
  case class Order(id: OrderId, createdAt: Instant, status: OrderStatus)

  sealed trait OrderStatus
  object OrderStatus {
    case class Draft(basket: List[Item])                                                             extends OrderStatus
    case class Checkout(basket: NonEmptyList[Item], deliveryAddress: Option[Address])                extends OrderStatus
    case class Submitted(basket: NonEmptyList[Item], deliveryAddress: Address, submittedAt: Instant) extends OrderStatus
    case class Delivered(basket: NonEmptyList[Item],
                         deliveryAddress: Address,
                         submittedAt: Instant,
                         deliveredAt: Instant)
        extends OrderStatus
    case class Cancelled(previousState: Either[Checkout, Submitted], cancelledAt: Instant) extends OrderStatus
  }

  case class ItemId(value: UUID)
  case class Item(id: ItemId, quantity: Long, price: BigDecimal)

  case class Address(streetNumber: Int, postCode: String)

  import answers.types.TypeAnswers.OrderStatus._

  def submit(order: Order, now: Instant): Either[OrderError, Order] =
    order.status match {
      case x: Checkout =>
        x.deliveryAddress match {
          case None => Left(OrderError.MissingDeliveryAddress(x))
          case Some(address) =>
            val newStatus = Submitted(x.basket, address, submittedAt = now)
            Right(order.copy(status = newStatus))
        }
      case _: Draft | _: Submitted | _: Delivered | _: Cancelled =>
        Left(OrderError.InvalidStatus(order.status))
    }

  def deliver(order: Order, now: Instant): Either[OrderError.InvalidStatus, Order] =
    order.status match {
      case x: Submitted =>
        val newStatus = Delivered(x.basket, x.deliveryAddress, x.submittedAt, deliveredAt = now)
        Right(order.copy(status = newStatus))
      case _: Draft | _: Checkout | _: Delivered | _: Cancelled =>
        Left(OrderError.InvalidStatus(order.status))
    }

  def cancel(order: Order, now: Instant): Either[OrderError.InvalidStatus, Order] =
    order.status match {
      case x: Checkout =>
        val newStatus = Cancelled(Left(x), cancelledAt = now)
        Right(order.copy(status = newStatus))
      case x: Submitted =>
        val newStatus = Cancelled(Right(x), cancelledAt = now)
        Right(order.copy(status = newStatus))
      case _: Draft | _: Delivered | _: Cancelled =>
        Left(OrderError.InvalidStatus(order.status))
    }

  sealed trait OrderError
  object OrderError {
    case class MissingDeliveryAddress(status: OrderStatus) extends OrderError
    case class InvalidStatus(status: OrderStatus)          extends OrderError
  }

  case class Order_V2[A](id: OrderId, createdAt: Instant, value: A)

  def submit_V2(order: Order_V2[Checkout], deliveryAddress: Address, now: Instant): Order_V2[Submitted] = {
    val newStatus = Submitted(order.value.basket, deliveryAddress, submittedAt = now)
    order.copy(value = newStatus)
  }

  def deliver_V2(order: Order_V2[Submitted], now: Instant): Order_V2[Delivered] = {
    val Submitted(basket, deliveryAddress, submittedAt) = order.value
    val newStatus                                       = Delivered(basket, deliveryAddress, submittedAt, deliveredAt = now)
    order.copy(value = newStatus)
  }

  def cancelledCheckout(order: Order_V2[Checkout], now: Instant): Order_V2[Cancelled] = {
    val newStatus = Cancelled(Left(order.value), cancelledAt = now)
    order.copy(value = newStatus)
  }

  def cancelledSubmitted(order: Order_V2[Submitted], now: Instant): Order_V2[Cancelled] = {
    val newStatus = Cancelled(Right(order.value), cancelledAt = now)
    order.copy(value = newStatus)
  }

  ////////////////////////
  // 3. Cardinality
  ////////////////////////

  val boolean: Cardinality[Boolean] = new Cardinality[Boolean] {
    def cardinality: Card = Constant(2)
  }

  val int: Cardinality[Int] = new Cardinality[Int] {
    def cardinality: Card = Constant(2) ^ Constant(32)
  }

  val any: Cardinality[Any] = new Cardinality[Any] {
    def cardinality: Card = Inf
  }

  val nothing: Cardinality[Nothing] = new Cardinality[Nothing] {
    def cardinality: Card = Constant(0)
  }

  val unit: Cardinality[Unit] = new Cardinality[Unit] {
    def cardinality: Card = Constant(1)
  }

  val ioUnit: Cardinality[IO[Unit]] = new Cardinality[IO[Unit]] {
    def cardinality: Card = Inf
  }

  val byte: Cardinality[Byte] = new Cardinality[Byte] {
    def cardinality: Card = Constant(2) ^ Constant(8)
  }

  val char: Cardinality[Char] = new Cardinality[Char] {
    def cardinality: Card = Constant(2) ^ Constant(16)
  }

  val optUnit: Cardinality[Option[Unit]] = new Cardinality[Option[Unit]] {
    def cardinality: Card = unit.cardinality + Constant(1)
  }

  val optBoolean: Cardinality[Option[Boolean]] = new Cardinality[Option[Boolean]] {
    def cardinality: Card = boolean.cardinality + Constant(1)
  }

  val intOrBoolean: Cardinality[IntOrBoolean] = new Cardinality[IntOrBoolean] {
    def cardinality: Card = int.cardinality + boolean.cardinality
  }

  sealed trait IntOrBoolean
  object IntOrBoolean {
    case class AnInt(value: Int)        extends IntOrBoolean
    case class ABoolean(value: Boolean) extends IntOrBoolean
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

  case class IntAndBoolean(i: Int, b: Boolean)

  val listUnit: Cardinality[List[Unit]] = new Cardinality[List[Unit]] {
    def cardinality: Card = Inf
  }

  val optNothing: Cardinality[Option[Nothing]] = new Cardinality[Option[Nothing]] {
    def cardinality: Card = nothing.cardinality + Constant(1)
  }

  val boolNothing: Cardinality[(Boolean, Nothing)] = new Cardinality[(Boolean, Nothing)] {
    def cardinality: Card = boolean.cardinality * nothing.cardinality
  }

  def option[A](a: Cardinality[A]): Cardinality[Option[A]] =
    new Cardinality[Option[A]] {
      def cardinality: Card = a.cardinality + Constant(1)
    }

  def list[A](a: Cardinality[A]): Cardinality[List[A]] =
    new Cardinality[List[A]] {
      def cardinality: Card =
        if (a.cardinality == Constant(0)) Constant(1)
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
      0.to(Int.MaxValue).foldLeft(Constant(BigInt(0)): Card)((acc, i) => acc + (char.cardinality ^ Constant(i)))
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

}
