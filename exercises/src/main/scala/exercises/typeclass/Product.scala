package exercises.typeclass

import cats.kernel.Eq
import cats.implicits._

case class Product(getProduct: Int)

object Product {
  implicit val eq: Eq[Product] = Eq.by[Product, Int](_.getProduct)
}