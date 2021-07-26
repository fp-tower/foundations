package answers.errorhandling.project
import answers.errorhandling.project.OrderGenerator._
import answers.errorhandling.project.OrderStatus.Delivered
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class OrderTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("Invalid actions for empty order") {
    forAll(orderIdGen, instantGen, durationGen, addressGen) { (orderId, now, delay, deliveryAddress) =>
      val order = Order.empty(orderId, now)
      val invalidActions: List[Order => Either[OrderError, Any]] = List(
        _.checkout,
        _.updateDeliveryAddress(deliveryAddress),
        _.submit(now.plus(delay)),
        _.deliver(now.plus(delay)),
        _.cancel(now.plus(delay))
      )

      invalidActions.foreach { action =>
        assert(action(order).isLeft)
      }
    }
  }

  test("happy path") {
    forAll(orderIdGen, instantGen, durationGen, durationGen, nelGen(itemGen), addressGen) {
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
