package answers.errorhandling.project

import answers.action.async.IO

import java.time.Instant

class OrderService(orderStore: OrderStore, clock: Clock, idGen: IdGen) {
  def createOrder: IO[Order] =
    for {
      orderId <- idGen.genOrderId
      now     <- clock.now
    } yield Order.empty(orderId, now)

  def checkout(id: OrderId): IO[Order] =
    modifyOrder(id)(_.checkout)

  def submit(id: OrderId): IO[Order] =
    for {
      now     <- clock.now
      updated <- modifyOrder(id)(_.submit(now))
    } yield updated

  def deliver(id: OrderId): IO[Order] =
    for {
      now     <- clock.now
      updated <- modifyOrder(id)(_.deliver(now))
    } yield updated

  private def modifyOrder(id: OrderId)(transition: Order => Either[OrderError, Order]): IO[Order] =
    for {
      order    <- orderStore.get(id).getOrFail(OrderMissing(id)) // add transaction in real code
      newOrder <- transition(order).getOrFail
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
