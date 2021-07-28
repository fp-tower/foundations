package answers.errorhandling.project.service

import answers.action.async.IO
import answers.errorhandling.NEL
import answers.errorhandling.project.{Item, Order, OrderError, OrderId, OrderMissing}

class OrderService(
  orderStore: OrderStore,
  clock: Clock,
  idGenerator: IdGenerator
) {

  def createNewOrder: IO[Order] =
    for {
      orderId <- idGenerator.genOrderId
      now     <- clock.now
      order = Order.empty(orderId, now)
      _ <- orderStore.save(order)
    } yield order

  def addItems(id: OrderId, items: NEL[Item]): IO[Order] =
    modifyOrder(id)(_.addItems(items))

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
