package errorhandling

import java.time.{Duration, Instant}

import answers.errorhandling.UnrepresentableAnswers
import exercises.errorhandling.UnrepresentableExercises
import exercises.errorhandling.UnrepresentableExercises.{Item, Order}
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.Matchers
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.Checkers
import toimpl.errorhandling.UnrepresentableToImpl

class UnrepresentableExercisesTest extends UnrepresentableTest(UnrepresentableExercises)
class UnrepresentableAnswersTest   extends UnrepresentableTest(UnrepresentableAnswers)

class UnrepresentableTest(impl: UnrepresentableToImpl) extends AnyFunSuite with Matchers with Checkers {
  import impl._

  test("checkout") {
    val order = Order(
      id = "xxx",
      status = "Draft",
      items = List(Item("a", 1, 2), Item("b", 2, 3)),
      deliveredAt = None,
      deliveryAddress = None,
      submittedAt = None
    )

    checkout(order) shouldEqual order.copy(status = "Checkout")

    assertThrows[Exception](checkout(order.copy(items = Nil)))
    assertThrows[Exception](checkout(order.copy(status = "Delivered")))
  }

  test("submit") {
    val now = Instant.now()
    val order = Order(
      id = "xxx",
      status = "Checkout",
      items = List(Item("a", 1, 2)),
      deliveredAt = None,
      deliveryAddress = Some("123 Iffley road"),
      submittedAt = None
    )

    submit(order, now) shouldEqual order.copy(status = "Submitted", submittedAt = Some(now))

    assertThrows[Exception](submit(order.copy(items = Nil), now))
    assertThrows[Exception](submit(order.copy(deliveryAddress = None), now))
    assertThrows[Exception](submit(order.copy(status = "Delivered"), now))
  }

  test("deliver") {
    val now              = Instant.now()
    val deliveryDuration = Duration.ofDays(2)
    val order = Order(
      id = "xxx",
      status = "Submitted",
      items = List(Item("a", 1, 2)),
      deliveredAt = None,
      deliveryAddress = Some("123 Iffley road"),
      submittedAt = Some(now.minus(deliveryDuration))
    )

    deliver(order, now) shouldEqual ((order.copy(status = "Delivered", deliveredAt = Some(now)), deliveryDuration))

    assertThrows[Exception](submit(order.copy(items = Nil), now))
    assertThrows[Exception](submit(order.copy(deliveryAddress = None), now))
    assertThrows[Exception](submit(order.copy(submittedAt = None), now))
    assertThrows[Exception](submit(order.copy(status = "Draft"), now))
  }

  test("totalItem") {
    totalItem(Item("12345", 2, 17.99)) shouldEqual 35.98
    totalItem(Item("12345", 0, 21.01)) shouldEqual 0
  }

  implicit val arbItem: Arbitrary[Item] = Arbitrary(
    for {
      id    <- Gen.resize(5, Gen.alphaNumStr)
      qty   <- Gen.chooseNum(-10, 1000000)
      price <- Gen.chooseNum(-10.0, 999999999999999.9) // it is cheating, I know
    } yield Item(id, qty, price)
  )

  for ((id, prop) <- totalItemProperties.properties)
    test(s"totalItem.$id") {
      check(prop)
    }
}
