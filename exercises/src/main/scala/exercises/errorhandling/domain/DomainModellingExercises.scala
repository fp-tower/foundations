package exercises.errorhandling.domain

import exercises.errorhandling.domain.DomainModellingExercises.OrderError._

import java.time.{Duration, Instant}

object DomainModellingExercises {

  case class Item(id: String, quantity: Int, unitPrice: Double)
  case class Order(
    id: String,
    status: String, // Draft | Checkout | Submitted | Delivered
    basket: List[Item],
    deliveryAddress: Option[String],
    createdAt: Instant,
    submittedAt: Option[Instant],
    deliveredAt: Option[Instant]
  )

  // Create an empty new order.
  def newOrder(id: String, now: Instant): Order =
    Order(
      id = id,
      status = "Draft",
      basket = Nil,
      deliveryAddress = None,
      createdAt = now,
      submittedAt = None,
      deliveredAt = None
    )

  // 1. Implement `checkout` which moves an `Order` into "Checkout" status.
  // `checkout` requires the order to be in the "Draft" status, otherwise it returns an `InvalidStatus` error.
  // `checkout` requires the order to contain at least one item, otherwise it returns an `EmptyBasket` error.
  def checkout(order: Order): Either[OrderError, Order] =
    ???

  // 2. Implement `submit` which moves an `Order` into "Submitted" status.
  // `submit` requires the order to be in the "Checkout" status and to have a delivery address.
  // If `submit` succeeds, the resulting order must be in "Submitted" status and
  // have the field `submittedAt` defined.
  // Note: You may need to modify the signature and extend `OrderError`
  def submit(order: Order): Either[OrderError, Order] =
    ???

  // 3. Implement `deliver` which moves an `Order` into "Delivered" status.
  // `deliver` requires the order to be in the "Submitted" status.
  // If `deliver` succeeds, the resulting order must be in "Delivered" status and
  // have the field `deliveredAt` defined.
  // If `deliver` succeeds, it also returns the time it took to deliver the order (duration
  // between `submittedAt` and `deliveredAt`).
  // Note: You may need to modify the signature and extend `OrderError`
  def deliver(order: Order): Either[OrderError, (Order, Duration)] =
    ???

  sealed trait OrderError
  object OrderError {
    case object EmptyBasket                                         extends OrderError
    case class InvalidStatus(action: String, currentStatus: String) extends OrderError
  }

}
