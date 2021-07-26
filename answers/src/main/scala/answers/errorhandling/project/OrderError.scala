package answers.errorhandling.project
import scala.util.control.NoStackTrace

sealed abstract class OrderError(message: String) extends NoStackTrace {
  override def getMessage: String = message
}
object OrderError {
  case object EmptyBasket                                extends OrderError("Basket is empty")
  case class MissingDeliveryAddress(status: OrderStatus) extends OrderError("Delivery address is missing")
  case class InvalidStatus(currentStatus: OrderStatus)
      extends OrderError(s"Invalid order status: ${currentStatus.getClass.getSimpleName}")
}
