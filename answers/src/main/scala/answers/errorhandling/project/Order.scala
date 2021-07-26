package answers.errorhandling.project
import answers.errorhandling.NEL
import answers.errorhandling.project.OrderError._
import answers.errorhandling.project.OrderStatus._

import java.time.Instant
import java.util.UUID

case class Order(id: OrderId, createdAt: Instant, status: OrderStatus) {
  def addItem(item: Item): Either[OrderError, Order] =
    status match {
      case Draft(items) =>
        Right(copy(status = Draft(items :+ item)))
      case x: Checkout =>
        Right(copy(status = Draft(x.basket.toList :+ item)))
      case _: Submitted | _: Delivered | _: Cancelled =>
        Left(InvalidStatus(status))
    }

  def addItem(items: NEL[Item]): Either[OrderError, Order] =
    items.toList.foldLeft[Either[OrderError, Order]](Right(this)) { (state, item) =>
      state.flatMap(_.addItem(item))
    }

  def checkout: Either[OrderError, Order] =
    status match {
      case Draft(items) =>
        NEL.fromList(items) match {
          case None      => Left(EmptyBasket)
          case Some(nel) => Right(copy(status = Checkout(nel, None)))
        }
      case _: Checkout | _: Submitted | _: Delivered | _: Cancelled =>
        Left(InvalidStatus(status))
    }

  def updateDeliveryAddress(address: Address): Either[OrderError, Order] =
    status match {
      case x: Checkout =>
        val newStatus = x.copy(deliveryAddress = Some(address))
        Right(copy(status = newStatus))
      case _: Draft | _: Submitted | _: Delivered | _: Cancelled =>
        Left(InvalidStatus(status))
    }

  def submit(now: Instant): Either[OrderError, Order] =
    status match {
      case x: Checkout =>
        x.deliveryAddress match {
          case None => Left(MissingDeliveryAddress(x))
          case Some(address) =>
            val newStatus = Submitted(x.basket, address, submittedAt = now)
            Right(copy(status = newStatus))
        }
      case _: Draft | _: Submitted | _: Delivered | _: Cancelled =>
        Left(InvalidStatus(status))
    }

  def deliver(now: Instant): Either[InvalidStatus, Order] =
    status match {
      case x: Submitted =>
        val newStatus = Delivered(x.basket, x.deliveryAddress, x.submittedAt, deliveredAt = now)
        Right(copy(status = newStatus))
      case _: Draft | _: Checkout | _: Delivered | _: Cancelled =>
        Left(OrderError.InvalidStatus(status))
    }

  def cancel(now: Instant): Either[InvalidStatus, Order] =
    status match {
      case x: Checkout =>
        val newStatus = Cancelled(Left(x), cancelledAt = now)
        Right(copy(status = newStatus))
      case x: Submitted =>
        val newStatus = Cancelled(Right(x), cancelledAt = now)
        Right(copy(status = newStatus))
      case _: Draft | _: Delivered | _: Cancelled =>
        Left(InvalidStatus(status))
    }
}

object Order {
  def empty(id: OrderId, now: Instant): Order =
    Order(
      id = id,
      createdAt = now,
      Draft(Nil)
    )
}

case class OrderId(value: UUID)
case class ItemId(value: UUID)
case class Item(id: ItemId, quantity: Long, price: BigDecimal)
case class Address(streetNumber: Int, postCode: String)
