package types

import java.time.{Duration, Instant}
import java.util.UUID

import answers.types.TypeAnswers
import answers.types.TypeAnswers.OrderStatus._
import answers.types.TypeAnswers._
import cats.Eq
import cats.data.NonEmptyList
import cats.implicits._
import org.scalacheck.Arbitrary
import org.scalatest.Matchers
import org.scalatest.funsuite.AnyFunSuite
import org.typelevel.discipline.scalatest.Discipline

class TypeAnswersTest extends AnyFunSuite with Discipline with Matchers {
  test("boolean - optUnit") {
    boolean.cardinality.eval shouldEqual optUnit.cardinality.eval
  }

  test("intOrBoolean") {
    intOrBoolean.cardinality.eval shouldEqual Some(BigInt(2).pow(32) + 2)
  }

  test("intAndBoolean") {
    intAndBoolean.cardinality.eval shouldEqual Some(BigInt(2).pow(33))
  }

  test("option") {
    option(boolean).cardinality.eval shouldEqual Some(BigInt(3))
    option(unit).cardinality.eval shouldEqual Some(BigInt(2))
  }

  test("list") {
    list(boolean).cardinality.eval shouldEqual None
    list[Nothing](nothing).cardinality.eval shouldEqual Some(BigInt(1))
  }

  test("either") {
    either(boolean, unit).cardinality.eval shouldEqual Some(BigInt(3))
    either(byte, boolean).cardinality.eval shouldEqual Some(BigInt(258))
    either(unit, listUnit).cardinality.eval shouldEqual None
    either[Unit, Nothing](unit, nothing).cardinality.eval shouldEqual Some(BigInt(1))
  }

  test("tuple2") {
    tuple2(boolean, unit).cardinality.eval shouldEqual Some(BigInt(2))
    tuple2(byte, boolean).cardinality.eval shouldEqual Some(BigInt(512))
    tuple2(byte, boolean).cardinality.eval shouldEqual Some(BigInt(512))
    tuple2[Nothing, List[Boolean]](nothing, list(boolean)).cardinality.eval shouldEqual Some(BigInt(0))
    tuple2[List[Boolean], Nothing](list(boolean), nothing).cardinality.eval shouldEqual Some(BigInt(0))
  }

  test("func") {
    func(boolean, boolean).cardinality.eval shouldEqual Some(BigInt(4))
    func(boolean, unit).cardinality.eval shouldEqual Some(BigInt(1))
    func[Nothing, List[Boolean]](nothing, list(boolean)).cardinality.eval shouldEqual Some(BigInt(1))
    func(list(boolean), unit).cardinality.eval shouldEqual Some(BigInt(1))
    func[List[Boolean], Nothing](list(boolean), nothing).cardinality.eval shouldEqual Some(BigInt(0))
  }

  checkAll("a * 1 == a", IsoLaws(aUnitToA[Int]))
  checkAll("a + 0 == a", IsoLaws(aOrNothingToA[Int]))
  checkAll("Option[A] <=> Either[Unit, A]", IsoLaws(optionToEitherUnit[Int]))
  checkAll("a ^ 1 ==  a", IsoLaws(power1[Int]))
  checkAll("a * (b + c) == a * b + a * c", IsoLaws(distributeTuple[Int, Int, Int]))

  implicit def arbAOrNothing[A: Arbitrary]: Arbitrary[Either[A, Nothing]] =
    Arbitrary(Arbitrary.arbitrary[A].map(Left(_)))

  implicit def eqAOrNothing[A: Eq]: Eq[Either[A, Nothing]] =
    Eq.by(_.fold(identity, TypeAnswers.absurd))

  implicit def eqUnitToA[A: Eq]: Eq[Unit => A] =
    Eq.by(_.apply(()))

  def days(x: Int): Duration = Duration.ofDays(x)

  test("mostRecentBlogs") {
    val now = Instant.now()
    val b1  = BlogPost("123", "foo", now)
    val b2  = BlogPost("222", "bar", now.plus(days(3)))
    val b3  = BlogPost("444", "fuzz", now.plus(days(9)))

    mostRecentBlogs(2)(List(b3, b1, b2)) shouldEqual List(b1, b2)
  }

  test("invoice") {
    val invoice = Invoice("111",
                          NonEmptyList.of(
                            InvoiceItem("a", 2, 10),
                            InvoiceItem("a", 5, 4)
                          ))
    invoice.total shouldEqual 40
    invoice.totalWithDiscountedFirstItem(0.5) shouldEqual 30
  }

  test("submit") {
    val now     = Instant.now()
    val orderId = OrderId(UUID.randomUUID())
    val itemId  = ItemId(UUID.randomUUID())
    val address = Address(10, "EXC1 7TW")
    val status  = Checkout(NonEmptyList.of(Item(itemId, 1, 2)), Some(address))
    val order   = Order(orderId, now, status)

    submit(order, now.plus(days(3))) shouldEqual Right(
      Order(orderId, now, Submitted(NonEmptyList.of(Item(itemId, 1, 2)), address, now.plus(days(3))))
    )

    val noAddress = order.copy(status = status.copy(deliveryAddress = None))
    submit(noAddress, now.plus(days(3))) shouldEqual Left(OrderError.MissingDeliveryAddress(noAddress.status))

    val draftOrder = order.copy(status = Draft(Nil))
    submit(draftOrder, now.plus(days(3))) shouldEqual Left(OrderError.InvalidStatus(draftOrder.status))
  }

  test("deliver") {
    val now     = Instant.now()
    val orderId = OrderId(UUID.randomUUID())
    val itemId  = ItemId(UUID.randomUUID())
    val address = Address(10, "EXC1 7TW")
    val order   = Order(orderId, now, Submitted(NonEmptyList.of(Item(itemId, 1, 2)), address, now.plus(days(3))))

    deliver(order, now.plus(days(4))) shouldEqual Right(
      Order(orderId, now, Delivered(NonEmptyList.of(Item(itemId, 1, 2)), address, now.plus(days(3)), now.plus(days(4))))
    )

    val draftOrder = order.copy(status = Draft(Nil))
    deliver(draftOrder, now.plus(days(4))) shouldEqual Left(OrderError.InvalidStatus(draftOrder.status))
  }

  test("cancel") {
    val now          = Instant.now()
    def days(x: Int) = Duration.ofDays(x)
    val orderId      = OrderId(UUID.randomUUID())
    val itemId       = ItemId(UUID.randomUUID())
    val address      = Address(10, "EXC1 7TW")
    val submitted    = Submitted(NonEmptyList.of(Item(itemId, 1, 2)), address, now.plus(days(3)))
    val order        = Order(orderId, now, submitted)

    TypeAnswers.cancel(order, now.plus(days(4))) shouldEqual Right(
      Order(orderId, now, Cancelled(Right(submitted), now.plus(days(4))))
    )

    val draftOrder = order.copy(status = Draft(Nil))
    TypeAnswers.cancel(draftOrder, now.plus(days(4))) shouldEqual Left(OrderError.InvalidStatus(draftOrder.status))
  }

}
