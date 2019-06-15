package exercises.errorhandling

import java.time.{Duration, Instant}

import toimpl.errorhandling.UnrepresentableToImpl

object UnrepresentableExercises extends UnrepresentableToImpl {

  ////////////////////////
  // 1. Order
  ////////////////////////

  case class Item(id: String, quantity: Int, unitPrice: Double)

  val redBook = Item("12345", 2, 17.99)

  // 1a. Implement totalItem
  // such as totalItem(redBook) = 35.98
  def totalItem(item: Item): Double = ???

  // 1b. What scenario are impossible in real life but are permitted by Item encoding?
  // How could you constrain Item to make these scenario impossible? Check refined scala library

  // All orders have an id and a status
  // a status can be:
  // * "Draft" before checkout items can be empty
  // * "Checkout" items cannot be empty
  // * "Submitted" delivery address cannot empty, capture submission time
  // * "Cancelled" cancelled a submitted items can be cancelled, capture cancel time
  // * "Delivered" received by the customer, cannot be updated (no return)
  case class Order(
    id: String,
    status: String,
    items: List[Item],
    deliveryAddress: Option[String],
    submittedAt: Option[Instant],
    cancelledAt: Option[Instant],
    deliveredAt: Option[Instant]
  )

  // 1c. Implement checkout such as it encodes the transition PreCheckout -> Checkout
  // What are the conditions for the transition to be successful?
  // For now throw an exception if a condition is not respected
  def checkout(order: Order): Order = ???

  // 1d. Implement submit such as it encodes the transition Checkout -> Submitted
  // What are the conditions for the transition to be successful?
  // For now throw an exception if a condition is not respected
  def submit(order: Order, now: Instant): Order = ???

  // 1d. Implement deliver such as it encodes the transition Submitted -> Delivered
  // Return an updated order and the time it took to deliver (between submittedAt and now)
  // What are the conditions for the transition to be successful?
  // For now throw an exception if a condition is not respected
  def deliver(order: Order, now: Instant): (Order, Duration) = ???

  // 1e. How would you refactor Order to reduce the number of possible exceptions?
}
