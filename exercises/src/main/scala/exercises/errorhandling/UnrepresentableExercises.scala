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

  // 1d. Now we will try to reduce the number of possible exceptions?
  // Start by defining a case class for each Order State like Draft, Checkout, etc ...
  // Each class should have as precise fields as possible, i.e. if a field is mandatory, it shouldn't be an Option
  sealed trait Order_V2

  object Order_V2 {
    case class Draft()    extends Order_V2
    case class Checkout() extends Order_V2
  }

  // 1e. Implement checkout_V2, submit_V2 and deliver_V2
  // They should have less cases where you need to throw Exception
  def checkout_V2(order: Order_V2): Order_V2 = ???

  def submit_V2(order: Order_V2, now: Instant): Order_V2 = ???

  def deliver_V2(order: Order_V2): (Order_V2, Duration) = ???

  // 1f. The main source of errors is caused by the order not being in a valid state to make an action
  // How could you change the signature or checkout / submit / deliver functions to avoid these cases
  // Define and implement checkout_V3
  def checkout_V3 = ???

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
