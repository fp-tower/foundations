package exercises.errorhandling

import java.time.Instant

object UnrepresentableStateExercises {

  ////////////////////////
  // 1. Order
  ////////////////////////

  // All orders have an id and a status
  // a status can be:
  // * "Pending"   before checkout
  // * "Submitted" sending to customer (cannot be empty)
  // * "Cancelled" when a submitted ordered is cancelled
  // * "Delivered" received by the customer (cannot be returned)
  // submittedAt and cancelledAt record when an order has been submitted / cancelled
  case class Order(
    id: String,
    status: String,
    items: List[Item],
    submittedAt: Option[Instant],
    cancelledAt: Option[Instant]
  )

  case class Item(id: String, quantity: Int, unitPrice: Double)

  // 1a. Implement total which calculate the total value of an order
  def total(order: Order): Double = ???

  // 1b. Implement submit
  def submit(order: Order, now: Instant) = ???
}
