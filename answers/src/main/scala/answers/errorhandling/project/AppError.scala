package answers.errorhandling.project
import scala.util.control.NoStackTrace

sealed abstract class AppError(message: String) extends NoStackTrace {
  override def getMessage: String = message
}

case class OrderMissing(id: OrderId) extends AppError(s"Order ${id.value} is missing")

sealed abstract class OrderError(message: String) extends AppError(message)
object OrderError {
  case object EmptyBasket            extends OrderError("Basket is empty")
  case object MissingDeliveryAddress extends OrderError("Delivery address is missing")
  case class InvalidStatus(currentStatus: OrderStatus)
      extends OrderError(s"Invalid order status: ${currentStatus.getClass.getSimpleName}")
}
