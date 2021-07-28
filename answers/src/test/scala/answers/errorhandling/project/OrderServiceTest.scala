package answers.errorhandling.project

import answers.errorhandling.project.OrderGenerator._
import answers.errorhandling.project.service.{Clock, IdGenerator, OrderService, OrderStore}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class OrderServiceTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  test("createOrder inserts a new order in the store") {
    forAll(orderIdGen, instantGen) { (orderId, now) =>
      val orderStore  = OrderStore.inMemory()
      val clock       = Clock.constant(now)
      val idGenerator = IdGenerator.constant(orderId)
      val service     = new OrderService(orderStore, clock, idGenerator)

      val workflow = for {
        newOrder <- service.createNewOrder
        fetched  <- orderStore.get(newOrder.id)
      } yield fetched

      assert(workflow.unsafeRun().isDefined)
    }
  }

}
