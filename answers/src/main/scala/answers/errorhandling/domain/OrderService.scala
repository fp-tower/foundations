package answers.errorhandling.domain

import answers.action.async.IO
import answers.errorhandling.domain.DomainModellingAnswers.{Order, OrderError, OrderId}

import java.time.Instant

class OrderService(orderStore: OrderStore, clock: Clock, idGen: IdGen) {
  def createOrder: IO[Order] =
    for {
      orderId <- idGen.genOrderId
      now     <- clock.now
    } yield DomainModellingAnswers.newOrder(orderId, now)

  def checkout(id: OrderId): IO[Order] =
    modifyOrder(id)((order, _) => DomainModellingAnswers.checkout(order))

  def submit(id: OrderId): IO[Order] =
    modifyOrder(id)(DomainModellingAnswers.submit)

  def deliver(id: OrderId): IO[Order] =
    modifyOrder(id)(DomainModellingAnswers.deliver)

  private def modifyOrder(id: OrderId)(transition: (Order, Instant) => Either[OrderError, Order]): IO[Order] =
    for {
      now      <- clock.now
      order    <- orderStore.get(id).getOrFail(OrderMissing(id)) // add transaction in real code
      newOrder <- transition(order, now).getOrFail
      _        <- orderStore.save(newOrder)
    } yield newOrder

}

case class OrderMissing(id: OrderId) extends Exception(s"Order ${id.value} is missing")

trait OrderStore {
  def get(id: OrderId): IO[Option[Order]]
  def save(order: Order): IO[Unit]
}

trait Clock {
  def now: IO[Instant]
}

trait IdGen {
  def genOrderId: IO[OrderId]
}
