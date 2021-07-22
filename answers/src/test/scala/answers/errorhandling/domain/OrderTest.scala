package answers.errorhandling.domain
import answers.errorhandling.NEL
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import answers.errorhandling.domain.OrderStatus.Delivered

import java.time.Instant
import java.util.UUID

class OrderTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("happy path") {
    val orderId         = OrderId(UUID.randomUUID())
    val createdAt       = Instant.now()
    val submittedAt     = createdAt.plusSeconds(5)
    val deliveredAt     = createdAt.plusSeconds(3600 * 30) // 30 hours
    val order           = Order.empty(orderId, createdAt)
    val item1           = Item(ItemId(UUID.randomUUID()), 2, 24.99)
    val item2           = Item(ItemId(UUID.randomUUID()), 1, 15.49)
    val deliveryAddress = Address(23, "E16 8FV")

    val result = for {
      order <- order.addItem(item1)
      order <- order.addItem(item2)
      order <- order.checkout
      order <- order.updateDeliveryAddress(deliveryAddress)
      order <- order.submit(submittedAt)
      order <- order.deliver(deliveredAt)
    } yield order

    assert(
      result == Right(
        Order(orderId, createdAt, Delivered(NEL(item1, item2), deliveryAddress, submittedAt, deliveredAt))
      )
    )
  }

}
