package exercises.errorhandling

import java.time.{Duration, Instant}

import org.scalacheck.Prop.forAll
import org.scalacheck.{Arbitrary, Properties}
import toimpl.errorhandling.UnrepresentableToImpl

object UnrepresentableExercises extends UnrepresentableToImpl {

  ////////////////////////
  // 1. Order
  ////////////////////////

  // All orders have an id and a status
  // a status can be:
  // * "Draft" before checkout items can be empty
  // * "Checkout" items cannot be empty
  // * "Submitted" delivery address cannot empty, capture submission time
  // * "Cancelled" cancelled a submitted items can be cancelled, capture cancel time
  // * "Delivered" received by the customer, cannot be updated (no return)
  case class Item(id: String, quantity: Int, unitPrice: Double)
  case class Order(
    id: String,
    status: String,
    items: List[Item],
    deliveryAddress: Option[String],
    submittedAt: Option[Instant],
    cancelledAt: Option[Instant],
    deliveredAt: Option[Instant]
  )

  // 1a. Implement checkout such as it encodes the transition PreCheckout -> Checkout
  // What are the conditions for the transition to be successful?
  // For now throw an exception if a condition is not respected
  def checkout(order: Order): Order = ???

  // 1b. Implement submit such as it encodes the transition Checkout -> Submitted
  // What are the conditions for the transition to be successful?
  // For now throw an exception if a condition is not respected
  def submit(order: Order, now: Instant): Order = ???

  // 1c. Implement deliver such as it encodes the transition Submitted -> Delivered
  // Return an updated order and the time it took to deliver (between submittedAt and now)
  // What are the conditions for the transition to be successful?
  // For now throw an exception if a condition is not respected
  def deliver(order: Order, now: Instant): (Order, Duration) = ???

  // 1d. How would you refactor Order to reduce the number of possible exceptions?

  ////////////////////////
  // 2. Item
  ////////////////////////

  val redBook = Item("12345", 2, 17.99)

  // 2a. Implement totalItem
  // such as totalItem(redBook) = 35.98
  def totalItem(item: Item): Double = ???

  // 2b. What property based tests would you write for totalItem?
  // Try to find 1-2 different properties, will your implementation pass these tests?
  def totalItemProperties(implicit arb: Arbitrary[Item]): Properties =
    new Properties("totalItem") {
      property("example") = forAll((item: Item) => item.quantity == 2) // to change
    }

  // 2c. What scenario are impossible in real life but are permitted by Item encoding?
  // How could you constrain Item to make these scenario impossible?
  // Check refined: https://github.com/fthomas/refined
  // and singleton: https://github.com/fthomas/singleton-ops
}
