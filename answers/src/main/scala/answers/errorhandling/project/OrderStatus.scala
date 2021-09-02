package answers.errorhandling.project
import answers.errorhandling.NEL

import java.time.{Duration, Instant}

sealed trait OrderStatus
object OrderStatus {
  case class Draft(basket: List[Item])                                                    extends OrderStatus
  case class Checkout(basket: NEL[Item], deliveryAddress: Option[Address])                extends OrderStatus
  case class Submitted(basket: NEL[Item], deliveryAddress: Address, submittedAt: Instant) extends OrderStatus
  case class Cancelled(previousState: Either[Checkout, Submitted], cancelledAt: Instant)  extends OrderStatus
  case class Delivered(
    basket: NEL[Item],
    deliveryAddress: Address,
    submittedAt: Instant,
    deliveredAt: Instant
  ) extends OrderStatus {
    val deliveryDuration: Duration = Duration.between(submittedAt, deliveredAt)
  }
}
