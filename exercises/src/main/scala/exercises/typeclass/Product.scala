package exercises.typeclass

case class Product(getProduct: Int)

object Product {
  implicit val eq: Eq[Product] = Eq.by[Product, Int](_.getProduct)
}