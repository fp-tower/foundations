package exercises.errorhandling.domain

import exercises.errorhandling.domain.OrderError._

import java.time.{Duration, Instant}

// All order statuses
// "Draft"    : initial state, the user can add `Item` to the basket.
// "Checkout" : basket is non-empty, the user must enter their delivery address.
// "Submitted": the order is complete and it will be shipped shortly.
// "Delivered": the order has been delivered to the user.
case class Order(
  id: String,
  status: String,
  basket: List[Item],
  deliveryAddress: Option[Address],
  createdAt: Instant,
  submittedAt: Option[Instant],
  deliveredAt: Option[Instant]
) {

  // Adds an `Item` to the basket.
  // This action is only allowed if the `Order` is in "Draft" or "Checkout" statuses.
  // If the `Order` is in "Checkout" status, move it back to "Draft".
  // Note: We don't verify if the `Item` is already in the basket.
  def addItem(item: Item): Either[OrderError, Order] =
    status match {
      case "Draft" | "Checkout" => Right(copy(status = "Draft", basket = basket :+ item))
      case _                    => Left(InvalidStatus(status))
    }

  // 1. Implement `checkout` which moves the `Order` into "Checkout" status.
  // `checkout` requires the order to be in the "Draft" status, otherwise it returns an `InvalidStatus` error.
  // `checkout` requires the order to contain at least one item, otherwise it returns an `EmptyBasket` error.
  def checkout: Either[OrderError, Order] =
    ???

  def updateDeliveryAddress(address: Address): Either[OrderError, Order] =
    status match {
      case "Checkout" => Right(copy(deliveryAddress = Some(address)))
      case _          => Left(InvalidStatus(status))
    }

  // 2. Implement `submit` which moves the `Order` into "Submitted" status.
  // `submit` requires the order to be in the "Checkout" status and to have a delivery address.
  // If `submit` succeeds, the resulting order must be in "Submitted" status and
  // have the field `submittedAt` defined.
  // Note: You may need to extend `OrderError`
  def submit(now: Instant): Either[OrderError, Order] =
    ???

  // 3. Implement `deliver` which moves the `Order` into "Delivered" status.
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
