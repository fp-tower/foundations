package exercises.errorhandling.project

import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import java.time.{Duration, Instant}

class OrderTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  ignore("happy path") {
    val orderId         = "ORD0001"
    val createdAt       = Instant.now()
    val submittedAt     = createdAt.plusSeconds(5)
    val deliveredAt     = createdAt.plusSeconds(3600 * 30) // 30 hours
    val order           = Order.empty(orderId, createdAt)
    val item1           = Item("AAA", 2, 24.99)
    val item2           = Item("BBB", 1, 15.49)
    val deliveryAddress = Address(23, "E16 8FV")

    val result = for {
      order         <- order.addItem(item1)
      order         <- order.addItem(item2)
      order         <- order.checkout
      order         <- order.updateDeliveryAddress(deliveryAddress)
      order         <- order.submit(submittedAt)
      orderDuration <- order.deliver(deliveredAt)
    } yield orderDuration

    assert(
      result.map(_._1) == Right(
        Order(
          id = orderId,
          status = "Delivered",
          basket = List(item1, item2),
          deliveryAddress = Some(deliveryAddress),
          createdAt = createdAt,
          submittedAt = Some(submittedAt),
          deliveredAt = Some(deliveredAt)
        )
      )
    )

    assert(result.map(_._2) == Right(Duration.ofHours(30)))
  }

}
