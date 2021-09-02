package answers.errorhandling.project
import answers.errorhandling.project.OrderGenerator._
import answers.errorhandling.project.OrderStatus.Delivered
import org.scalacheck.Gen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class OrderTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("addItem invalid in submitted and delivered") {
    forAll(Gen.oneOf(submittedGen, deliveredGen), itemGen) { (order, item) =>
      assert(order.addItem(item).isLeft)
    }
  }

  test("checkout invalid in checkout, submitted and delivered") {
    forAll(Gen.oneOf(checkoutGen, submittedGen, deliveredGen)) { order =>
      assert(order.checkout.isLeft)
    }
  }

  test("updateDeliveryAddress invalid in draft, submitted and delivered") {
    forAll(Gen.oneOf(draftGen, submittedGen, deliveredGen), addressGen) { (order, deliveryAddress) =>
      assert(order.updateDeliveryAddress(deliveryAddress).isLeft)
    }
  }

  test("submit invalid in draft, submitted and delivered") {
    forAll(Gen.oneOf(draftGen, submittedGen, deliveredGen), instantGen) { (order, now) =>
      assert(order.submit(now).isLeft)
    }
  }

  test("deliver invalid in draft, checkout and delivered") {
    forAll(Gen.oneOf(draftGen, checkoutGen, deliveredGen), instantGen) { (order, now) =>
      assert(order.deliver(now).isLeft)
    }
  }

  test("happy path") {
    forAll(orderIdGen, instantGen, durationGen, durationGen, nelOf(itemGen), addressGen) {
      (orderId, now, delay1, delay2, items, deliveryAddress) =>
        val submittedAt = now.plus(delay1)
        val deliveredAt = submittedAt.plus(delay2)
        val order       = Order.empty(orderId, now)

        val result = for {
          order <- order.addItems(items)
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
