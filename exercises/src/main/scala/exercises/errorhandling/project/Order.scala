package exercises.errorhandling.project

import exercises.errorhandling.project.OrderError._

import java.time.{Duration, Instant}

case class Order(
  id: String,
  status: String,                   // "Draft", "Checkout", "Submitted" or "Delivered"
  basket: List[Item],               // basket can be modified only in "Draft" or "Checkout"
  deliveryAddress: Option[Address], // can only be set during "Checkout"
  createdAt: Instant,               // set when the order is created ("Draft")
  submittedAt: Option[Instant],     // set when the order is moved to "Submitted"
  deliveredAt: Option[Instant]      // set when the order is moved to "Delivered"
) {

  // Adds an `Item` to the basket.
  // This action is only permitted if the `Order` is in "Draft" or "Checkout" statuses.
  // If the `Order` is in "Checkout" status, move it back to "Draft".
  // Note: We don't verify if the `Item` is already in the basket.
  def addItem(item: Item): Either[OrderError, Order] =
    status match {
      case "Draft" | "Checkout" => Right(copy(status = "Draft", basket = basket :+ item))
      case _                    => Left(InvalidStatus(status))
    }

  // 1. Implement `checkout` which attempts to move the `Order` to "Checkout" status.
  // `checkout` requires the order to be in the "Draft" status, otherwise it returns an `InvalidStatus` error.
  // `checkout` requires the order to contain at least one item, otherwise it returns an `EmptyBasket` error.
  def checkout: Either[OrderError, Order] =
    ???

  def updateDeliveryAddress(address: Address): Either[OrderError, Order] =
    status match {
      case "Checkout" => Right(copy(deliveryAddress = Some(address)))
      case _          => Left(InvalidStatus(status))
    }

  // 2. Implement `submit` which attempts to move the `Order` to "Submitted" status.
  // `submit` requires the order to be in the "Checkout" status and to have a delivery address.
  // If `submit` succeeds, the resulting order must be in "Submitted" status and
  // have the field `submittedAt` defined.
  // Note: You may need to extend `OrderError`
  def submit(now: Instant): Either[OrderError, Order] =
    ???

  // 3. Implement `deliver` which attempts to move the `Order` to "Delivered" status.
  // `deliver` requires the order to be in the "Submitted" status.
  // If `deliver` succeeds, the resulting order must be in "Delivered" status and
  // have the field `deliveredAt` defined.
  // If `deliver` succeeds, it also returns the time it took to deliver the order (duration
  // between `submittedAt` and `deliveredAt`).
  // Note: You may need to extend `OrderError`
  def deliver(now: Instant): Either[OrderError, (Order, Duration)] =
    ???
}

object Order {
  // Creates an empty draft order.
  def empty(id: String, now: Instant): Order =
    Order(
      id = id,
      status = "Draft",
      basket = Nil,
      deliveryAddress = None,
      createdAt = now,
      submittedAt = None,
      deliveredAt = None
    )
}
