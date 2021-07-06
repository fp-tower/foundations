package answers.errorhandling.domain
import answers.errorhandling.domain.DomainModellingAnswers.OrderError._
import answers.errorhandling.domain.DomainModellingAnswers.OrderStatus._

import java.time.{Duration, Instant}
import java.util.UUID
import scala.util.control.NoStackTrace

object DomainModellingAnswers {

  case class OrderId(value: UUID)
  case class ItemId(value: UUID)
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
    ) extends OrderStatus {
      val deliveryDuration: Duration = Duration.between(submittedAt, deliveredAt)
    }
  }

  case class Item(id: ItemId, quantity: Long, price: BigDecimal)
  case class Address(streetNumber: Int, postCode: String)

  def newOrder(id: OrderId, now: Instant): Order =
    Order(
      id = id,
      createdAt = now,
      Draft(Nil)
    )

  def checkout(order: Order): Either[OrderError, Order] =
    order.status match {
      case Draft(items) =>
        Nel.fromList(items) match {
          case None      => Left(EmptyBasket)
          case Some(nel) => Right(order.copy(status = Checkout(nel, None)))
        }
      case _: Checkout | _: Submitted | _: Delivered | _: Cancelled => Left(InvalidStatus(order.status))
    }

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

  sealed abstract class OrderError(message: String) extends NoStackTrace {
    override def getMessage: String = message
  }

  object OrderError {
    case object EmptyBasket                                extends OrderError("Basket is empty")
    case class MissingDeliveryAddress(status: OrderStatus) extends OrderError("Delivery address is missing")
    case class InvalidStatus(status: OrderStatus)
        extends OrderError(s"Invalid order status: ${status.getClass.getSimpleName}")
  }

}
