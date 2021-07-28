package answers.errorhandling.project.service

import answers.action.async.IO
import answers.errorhandling.project.{Order, OrderId}

import scala.collection.concurrent.TrieMap

trait OrderStore {
  def get(id: OrderId): IO[Option[Order]]
  def save(order: Order): IO[Unit]
}

object OrderStore {
  def inMemory(initialOrders: List[Order] = Nil): OrderStore = new OrderStore {
    val orders: TrieMap[OrderId, Order] = TrieMap.from(initialOrders.map(o => o.id -> o).toMap)

    def get(id: OrderId): IO[Option[Order]] = IO(orders.get(id))
    def save(order: Order): IO[Unit]        = IO(orders.update(order.id, order))
  }
}
