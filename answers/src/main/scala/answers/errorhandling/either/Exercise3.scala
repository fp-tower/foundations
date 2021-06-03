package answers.errorhandling.either

import answers.errorhandling.either.Exercise3.OrderError._

import java.time.{Duration, Instant}
import scala.util.control.NoStackTrace

object Exercise3 {

  case class Item(id: String, quantity: Int, unitPrice: Double)
  case class Order(
    id: String,
    status: String, // Draft | Checkout | Submitted | Delivered
    basket: List[Item],
    deliveryAddress: Option[String],
    submittedAt: Option[Instant],
    deliveredAt: Option[Instant]
  )

  def checkout(order: Order): Either[OrderError, Order] =
    order.status match {
      case "Draft" =>
        if (order.basket.isEmpty) Left(EmptyBasket)
        else order.copy(status = "Checkout").asRight
      case other =>
        Left(InvalidStatus("checkout", other))
    }

  def submit(order: Order, now: Instant): Either[OrderError, Order] =
    order.status match {
      case "Checkout" =>
        if (order.deliveryAddress.isEmpty) Left(MissingDeliveryAddress)
        else Right(order.copy(status = "Submitted", submittedAt = Some(now)))
      case other =>
        Left(InvalidStatus("submit", other))
    }

  def deliver(order: Order, now: Instant): Either[OrderError, (Order, Duration)] =
    order.status match {
      case "Submitted" =>
        order.submittedAt match {
          case Some(submittedTimestamp) =>
            val deliveryDuration = Duration.between(submittedTimestamp, now)
            val newOrder         = order.copy(status = "Delivered", deliveredAt = Some(now))
            Right((newOrder, deliveryDuration))
          case None => Left(MissingSubmittedTimestamp)
        }
      case other =>
        Left(InvalidStatus("deliver", other))
    }

  sealed trait OrderError extends NoStackTrace
  object OrderError {
    case object EmptyBasket                                         extends OrderError
    case object MissingDeliveryAddress                              extends OrderError
    case object MissingSubmittedTimestamp                           extends OrderError
    case class InvalidStatus(action: String, currentStatus: String) extends OrderError
  }

}
