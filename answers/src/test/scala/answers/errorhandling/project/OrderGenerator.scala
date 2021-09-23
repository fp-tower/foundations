package answers.errorhandling.project

import answers.errorhandling.NEL
import answers.errorhandling.project.OrderStatus.{Checkout, Delivered, Draft, Submitted}
import org.scalacheck.Gen

import java.time.{Duration, Instant}

object OrderGenerator {

  val orderIdGen: Gen[OrderId] = Gen.uuid.map(x => OrderId(x.toString))
  val itemIdGen: Gen[ItemId]   = Gen.uuid.map(x => ItemId(x.toString))

  val instantGen: Gen[Instant] =
    for {
      seconds <- Gen.choose(Instant.MIN.getEpochSecond, Instant.MAX.getEpochSecond)
      nano    <- Gen.choose(0, 1000_000_000L)
    } yield Instant.ofEpochSecond(seconds, nano)

  val durationGen: Gen[Duration] =
    Gen
      .chooseNum(0L, Duration.ofDays(400).getSeconds)
      .map(Duration.ofSeconds)

  def nelOf[A](gen: Gen[A]): Gen[NEL[A]] =
    Gen.nonEmptyListOf(gen).map {
      case Nil          => sys.error("Impossible")
      case head :: tail => NEL(head, tail)
    }

  val itemGen: Gen[Item] =
    for {
      itemId   <- itemIdGen
      quantity <- Gen.chooseNum(1L, 999999)
      price    <- Gen.chooseNum(0.0001, 999999999)
    } yield Item(itemId, quantity, price)

  val addressGen: Gen[Address] =
    for {
      streetNumber <- Gen.chooseNum(1, 99999)
      postCode     <- Gen.alphaNumStr
    } yield Address(streetNumber, postCode)

  val draftGen: Gen[Order] =
    for {
      orderId   <- orderIdGen
      createdAt <- instantGen
      items     <- Gen.listOf(itemGen)
    } yield Order(orderId, createdAt, Draft(items))

  val nonEmptyDraftGen: Gen[Order] =
    for {
      orderId   <- orderIdGen
      createdAt <- instantGen
      items     <- nelOf(itemGen)
    } yield Order(orderId, createdAt, Draft(items.toList))

  val checkoutGen: Gen[Order] =
    for {
      orderId   <- orderIdGen
      createdAt <- instantGen
      items     <- nelOf(itemGen)
      address   <- Gen.option(addressGen)
    } yield Order(orderId, createdAt, Checkout(items, address))

  val submittedGen: Gen[Order] =
    for {
      orderId   <- orderIdGen
      createdAt <- instantGen
      items     <- nelOf(itemGen)
      address   <- addressGen
      delay     <- durationGen
    } yield Order(orderId, createdAt, Submitted(items, address, createdAt.plus(delay)))

  val deliveredGen: Gen[Order] =
    for {
      orderId   <- orderIdGen
      createdAt <- instantGen
      items     <- nelOf(itemGen)
      address   <- addressGen
      delay1    <- durationGen
      submittedAt = createdAt.plus(delay1)
      delay2 <- durationGen
      deliveredAt = submittedAt.plus(delay2)
    } yield Order(orderId, createdAt, Delivered(items, address, submittedAt, deliveredAt))

  val orderGen: Gen[Order] =
    Gen.oneOf(draftGen, checkoutGen, submittedGen, deliveredGen)

}
