package answers.errorhandling.project
import answers.errorhandling.project.OrderGenerator._
import answers.errorhandling.project.OrderStatus.Delivered
import org.scalacheck.Gen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class OrderTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  def updateDeliveryAddressInvalid(state: String, gen: Gen[Order]): Unit =
    test(s"updateDeliveryAddress invalid for $state") {
      forAll(gen, addressGen) { (order, deliveryAddress) =>
        assert(order.updateDeliveryAddress(deliveryAddress).isLeft)
      }
    }

  updateDeliveryAddressInvalid("Draft", draftOrderGen)
  updateDeliveryAddressInvalid("Submitted", submittedOrderGen)
  updateDeliveryAddressInvalid("Delivered", deliveredOrderGen)

  test("happy path") {
    forAll(orderIdGen, instantGen, durationGen, durationGen, nelOf(itemGen), addressGen) {
      (orderId, now, delay1, delay2, items, deliveryAddress) =>
        val submittedAt = now.plus(delay1)
        val deliveredAt = submittedAt.plus(delay2)
        val order       = Order.empty(orderId, now)

        val result = for {
          order <- order.addItem(items)
          order <- order.checkout
          order <- order.updateDeliveryAddress(deliveryAddress)
          order <- order.submit(submittedAt)
          order <- order.deliver(deliveredAt)
        } yield order

        assert(
          result == Right(
            Order(orderId, now, Delivered(items, deliveryAddress, submittedAt, deliveredAt))
          )
        )
    }

  }

}
