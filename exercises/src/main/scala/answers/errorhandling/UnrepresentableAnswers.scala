package answers.errorhandling

import java.time.{Duration, Instant}

import cats.data.NonEmptyList
import cats.implicits._
import eu.timepit.refined.auto._
import eu.timepit.refined.types.numeric.{PosDouble, PosInt}
import exercises.errorhandling.UnrepresentableExercises.{Item, Order}
import org.scalacheck.Prop.forAll
import org.scalacheck.{Arbitrary, Properties}
import toimpl.errorhandling.UnrepresentableToImpl

object UnrepresentableAnswers extends UnrepresentableToImpl {

  ////////////////////////
  // 1. Order
  ////////////////////////

  def checkout(order: Order): Order =
    order.status match {
      case "Draft" =>
        if (order.items.nonEmpty) order.copy(status = "Checkout")
        else throw new Exception("Cannot checkout order with an empty basket")
      case other => throw new Exception(s"Invalid status to checkout $other")
    }

  def submit(order: Order, now: Instant): Order =
    order.status match {
      case "Checkout" =>
        if (order.deliveryAddress.isDefined && order.items.nonEmpty)
          order.copy(status = "Submitted", submittedAt = Some(now))
        else if (order.items.isEmpty)
          throw new Exception("Cannot submit order with an empty basket")
        else
          throw new Exception("Cannot submit order with no delivery address")
      case other => throw new Exception(s"Invalid status to submit $other")
    }

  def deliver(order: Order, now: Instant): (Order, Duration) =
    order.status match {
      case "Submitted" =>
        order.submittedAt match {
          case Some(x) =>
            val duration = Duration.between(x, now)
            (order.copy(status = "Delivered", deliveredAt = Some(now)), duration)
          case None =>
            throw new Exception("Invalid state, delivered without submittedAt")
        }
      case other => throw new Exception(s"Invalid status to submit $other")
    }

  sealed trait Order_V2

  object Order_V2 {
    case class Draft(id: String, items: List[Item])                                             extends Order_V2
    case class Checkout(id: String, items: NonEmptyList[Item], deliveryAddress: Option[String]) extends Order_V2
    case class Submitted(id: String, items: NonEmptyList[Item], deliveryAddress: String, submittedAt: Instant)
        extends Order_V2
    case class Delivered(
      id: String,
      items: NonEmptyList[Item],
      deliveryAddress: String,
      submittedAt: Instant,
      deliveredAt: Instant
    ) extends Order_V2
    case class Cancel(previousState: Either[Submitted, Delivered], cancelledAt: Instant) extends Order_V2
  }

  import Order_V2._

  def checkout_V2(order: Order_V2): Order_V2 =
    order match {
      case x: Draft =>
        x.items.toNel match {
          case Some(items) => Checkout(x.id, items, deliveryAddress = None)
          case None        => throw new Exception("Cannot checkout order with an empty basket")
        }
      case _: Checkout | _: Submitted | _: Delivered | _: Cancel =>
        throw new Exception(s"Invalid status to checkout $order")
    }

  def submit_V2(order: Order_V2, now: Instant): Order_V2 =
    order match {
      case x: Checkout =>
        x.deliveryAddress match {
          case Some(address) => Submitted(x.id, x.items, address, now)
          case None          => throw new Exception("Cannot submit order with no delivery address")
        }
      case _: Draft | _: Submitted | _: Delivered | _: Cancel =>
        throw new Exception(s"Invalid status to checkout $order")
    }

  def deliver_V2(order: Order_V2, now: Instant): (Order_V2, Duration) =
    order match {
      case x: Submitted =>
        val duration = Duration.between(x.submittedAt, now)
        val newState = Delivered(x.id, x.items, x.deliveryAddress, x.submittedAt, now)
        (newState, duration)
      case _: Draft | _: Checkout | _: Delivered | _: Cancel =>
        throw new Exception(s"Invalid status to checkout $order")
    }

  def checkout_V3(draft: Draft): Checkout =
    draft.items.toNel match {
      case Some(items) => Checkout(draft.id, items, deliveryAddress = None)
      case None        => throw new Exception("Cannot checkout order with an empty basket")
    }

  ////////////////////////
  // 2. Item
  ////////////////////////

  def totalItem(item: Item): Double =
    (item.quantity max 0) * (item.unitPrice max 0.0)

  def totalItemProperties(implicit arb: Arbitrary[Item]): Properties =
    new Properties("totalItem") {
      property("always positive") = forAll((item: Item) => totalItem(item) >= 0)
      property("add qty increase total") = forAll { item: Item =>
        val newQty = item.quantity + 1
        totalItem(item.copy(quantity = newQty)) >= totalItem(item)
      }
    }

  case class Item_V2(id: String, quantity: PosInt, unitPrice: PosDouble)
  val redBook_v2 = Item_V2("12345", 2, 17.99)

  def totalItem_v2(item: Item_V2): PosDouble = ???
}
