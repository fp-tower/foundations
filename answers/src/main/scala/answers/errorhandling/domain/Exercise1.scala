package answers.errorhandling.domain

import answers.errorhandling.Nel
import answers.errorhandling.domain.Exercise1.OrderError._
import answers.errorhandling.domain.Exercise1.OrderStatus._

import java.time.Instant
import java.util.UUID

object Exercise1 {

  case class OrderId(value: UUID)
  case class Order(id: OrderId, createdAt: Instant, status: OrderStatus)

  sealed trait OrderStatus
  object OrderStatus {
    case class Draft(basket: List[Item])                                                    extends OrderStatus
    case class Checkout(basket: Nel[Item], deliveryAddress: Option[Address])                extends OrderStatus
    case class Submitted(basket: Nel[Item], deliveryAddress: Address, submittedAt: Instant) extends OrderStatus
    case class Cancelled(previousState: Either[Checkout, Submitted], cancelledAt: Instant)  extends OrderStatus
    case class Delivered(
      basket: Nel[Item],
      deliveryAddress: Address,
      submittedAt: Instant,
      deliveredAt: Instant
    ) extends OrderStatus
  }

  case class ItemId(value: UUID)
  case class Item(id: ItemId, quantity: Long, price: BigDecimal)
  case class Address(streetNumber: Int, postCode: String)

  def submit(order: Order, now: Instant): Either[OrderError, Order] =
    order.status match {
      case x: Checkout =>
        x.deliveryAddress match {
          case None => Left(MissingDeliveryAddress(x))
          case Some(address) =>
            val newStatus = Submitted(x.basket, address, submittedAt = now)
            Right(order.copy(status = newStatus))
        }
      case _: Draft | _: Submitted | _: Delivered | _: Cancelled =>
        Left(InvalidStatus(order.status))
    }

  def deliver(order: Order, now: Instant): Either[InvalidStatus, Order] =
    order.status match {
      case x: Submitted =>
        val newStatus = Delivered(x.basket, x.deliveryAddress, x.submittedAt, deliveredAt = now)
        Right(order.copy(status = newStatus))
      case _: Draft | _: Checkout | _: Delivered | _: Cancelled =>
        Left(OrderError.InvalidStatus(order.status))
    }

  def cancel(order: Order, now: Instant): Either[InvalidStatus, Order] =
    order.status match {
      case x: Checkout =>
        val newStatus = Cancelled(Left(x), cancelledAt = now)
        Right(order.copy(status = newStatus))
      case x: Submitted =>
        val newStatus = Cancelled(Right(x), cancelledAt = now)
        Right(order.copy(status = newStatus))
      case _: Draft | _: Delivered | _: Cancelled =>
        Left(InvalidStatus(order.status))
    }

  sealed trait OrderError
  object OrderError {
    case class MissingDeliveryAddress(status: OrderStatus) extends OrderError
    case class InvalidStatus(status: OrderStatus)          extends OrderError
  }

}
