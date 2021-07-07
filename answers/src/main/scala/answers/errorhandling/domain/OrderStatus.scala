package answers.errorhandling.domain
import java.time.{Duration, Instant}

sealed trait OrderStatus
object OrderStatus {
  case class Draft(basket: List[Item])                                                    extends OrderStatus
  case class Checkout(basket: Nel[Item], deliveryAddress: Option[Address])                extends OrderStatus
  case class Submitted(basket: Nel[Item], deliveryAddress: Address, submittedAt: Instant) extends OrderStatus
  case class Cancelled(previousState: Either[Checkout, Submitted], cancelledAt: Instant)  extends OrderStatus
  case class Delivered(
    basket: Nel[Item],
    deliveryAddress: Address,
    submittedAt: Instant,
    deliveredAt: Instant
  ) extends OrderStatus {
    val deliveryDuration: Duration = Duration.between(submittedAt, deliveredAt)
  }
}
