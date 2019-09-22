package exercises.types

import eu.timepit.refined.types.numeric.PosInt
import exercises.sideeffect.IOExercises.IO
import exercises.types.Card._
import toimpl.types.TypeToImpl

// You can run and print things here
object TypeApp extends App {
  import TypeExercises._

  println(boolean.cardinality)
}

object TypeExercises extends TypeToImpl {

  ////////////////////////
  // 1. Misused types
  ////////////////////////

  case class Country(value: String)

  val UK: Country          = Country("United Kingdom")
  val France: Country      = Country("France")
  val Switzerland: Country = Country("Switzerland")

  // 1a. Implement `getCurrency` for UK, France and Switzerland
  // such as getCurrency(Country("France")) == "EUR"
  // What is wrong with this function? How could you improve it?
  def getCurrency(country: Country): String = ???

  val UKShort: Country  = Country("UK")
  val UKIban: Country   = Country("GBR") // https://www.iban.com/country-codes
  val UKFrench: Country = Country("Royaume-Uni")

  // 1b. Implement `compareChar` that indicates if `c1` is smaller, equal to or larger than `c2`
  // such as compareChar('a', 'c') == -1
  //         compareChar('c', 'c') ==  0
  //         compareChar('c', 'a') ==  1
  // What is wrong with this function? How could you improve it?
  def compareChar(c1: Char, c2: Char): Int = ???

  // 1c. Implement `mostRecentBlogs` that returns the `n` most recent blog posts
  // such as mostRecentBlogs(1)(List(
  //   BlogPost(1,First blog,2019-09-18T16:21:06.681768Z)
  //   BlogPost(23,Thoughts of the day,2019-09-21T08:14:06.702836Z)
  // )) == List(BlogPost(23,Thoughts of the day,2019-09-21T08:14:06.702836Z))
  // What is wrong with this function? How could you improve it?
  case class BlogPost(id: String, title: String, createAt: String)

  def mostRecentBlogs(n: Int)(blogs: List[BlogPost]): List[BlogPost] = ???

  // 1d. Implement `User#address` that returns the full address for a User (e.g. to send a parcel)
  // such as User("John Doe", Some(108), Some("Cannon Street"), Some("EC4N 6EU")) == "108 Canon Street EC4N 6EU"
  // What is wrong with this function? How could you improve it?
  case class User(name: String, streetNumber: Option[Int], streetName: Option[String], postCode: Option[String]) {
    def address: String = ???
  }

  // 1e. Implement `Order#total` that returns the total price of an Order then implement
  // `Order#totalWithDiscountedFirstItem`. The latter should calculate the total applying a
  // discount only on the first item, e.g. totalWithDiscountedFirstItem(0.3) would apply a 30% discount.
  // What is wrong with this function? How could you improve it?

  // quantity must be positive
  case class Item(id: String, quantity: Int, price: Double)
  // order must have at least one item
  case class Order(id: String, items: List[Item]) {
    def total: Double = ???

    def totalWithDiscountedFirstItem(discountPercent: Double): Double = ???
  }

  // 1f. Implement `getItemCount` that return how many items are part of the order.
  // In other words, it sums up items quantities without looking at prices.
  // Use `getOrder` to implement `getItemCount`.
  // What is wrong with this function? How could you improve it?
  def getOrder(id: String): IO[Order] =
    if (id == "123")
      IO.succeed(Order("123", List(Item("aa", 10, 2.5), Item("x", 2, 13.4))))
    else
      IO.fail(new Exception(s"No Order found for id $id"))

  def getItemCount(id: String): IO[Int] = ???

  ////////////////////////
  // 2. Data Encoding
  ////////////////////////

  // 2a. Create types that encode the following business requirements:
  // An order contains an order id (UUID), a created timestamp (Instant), an order status, and a basket of items.
  // An order status is either a draft, submitted, delivered or cancelled.
  // An item consists of an item id (UUID), a quantity and a price.
  // A basket can be empty in draft otherwise it must contain at least one item.
  // When an order is in draft, it may have a delivery address.
  // When an order is in submitted, it must have a delivery address and a submitted timestamp (Instant).
  // When an order is in delivered, it must have a delivery address, a submitted and delivered timestamps (Instant).
  // When an order is cancelled, it must contains a cancellation timestamp (Instant).
  // An address consists of a street number and a post code.

  ////////////////////////
  // 3. Cardinality
  ////////////////////////

  val boolean: Cardinality[Boolean] = new Cardinality[Boolean] {
    def cardinality: Card = Lit(2)
  }

  val int: Cardinality[Int] = new Cardinality[Int] {
    def cardinality: Card = Lit(2) ^ Lit(32)
  }

  val any: Cardinality[Any] = new Cardinality[Any] {
    def cardinality: Card = Inf
  }

  val nothing: Cardinality[Nothing] = new Cardinality[Nothing] {
    def cardinality: Card = Lit(0)
  }

  val unit: Cardinality[Unit] = new Cardinality[Unit] {
    def cardinality: Card = Lit(1)
  }

  val ioUnit: Cardinality[IO[Unit]] = new Cardinality[IO[Unit]] {
    def cardinality: Card = Inf
  }

  // 3a. How many possible values exist of type Byte?
  val byte: Cardinality[Byte] = new Cardinality[Byte] {
    def cardinality: Card = ???
  }

  // 3b. How many possible values exist of type Option[Unit]?
  val optUnit: Cardinality[Option[Unit]] = new Cardinality[Option[Unit]] {
    def cardinality: Card = ???
  }

  // 3c. How many possible values exist of type Option[Boolean]?
  val optBoolean: Cardinality[Option[Boolean]] = new Cardinality[Option[Boolean]] {
    def cardinality: Card = ???
  }

  // 3d. How many possible values exist of type IntOrBoolean?
  val intOrBoolean: Cardinality[IntOrBoolean] = new Cardinality[IntOrBoolean] {
    def cardinality: Card = ???
  }

  // 3e. How many possible values exist of type (Boolean, Unit)?
  val boolUnit: Cardinality[(Boolean, Unit)] = new Cardinality[(Boolean, Unit)] {
    def cardinality: Card = ???
  }

  // 3f. How many possible values exist of type (Boolean, Byte)?
  val boolByte: Cardinality[(Boolean, Byte)] = new Cardinality[(Boolean, Byte)] {
    def cardinality: Card = ???
  }

  // 3g. How many possible values exist of type IntAndBoolean?
  val intAndBoolean: Cardinality[IntAndBoolean] = new Cardinality[IntAndBoolean] {
    def cardinality: Card = ???
  }

  // 3h. How many possible values exist of type List[Unit]?
  val listUnit: Cardinality[List[Unit]] = new Cardinality[List[Unit]] {
    def cardinality: Card = ???
  }

  // 3i. How many possible values exist of type Option[Nothing]?
  val optNothing: Cardinality[Option[Nothing]] = new Cardinality[Option[Nothing]] {
    def cardinality: Card = ???
  }

  // 3j. How many possible values exist of type (Boolean, Nothing)?
  val boolNothing: Cardinality[(Boolean, Nothing)] = new Cardinality[(Boolean, Nothing)] {
    def cardinality: Card = ???
  }

  ///////////////////////////
  // 4. Advanced Cardinality
  ///////////////////////////

  // 4a. Implement option that derives the cardinality of Option[A] from A
  def option[A](a: Cardinality[A]): Cardinality[Option[A]] =
    new Cardinality[Option[A]] {
      def cardinality: Card = ???
    }

  // 4b. Implement list
  def list[A](a: Cardinality[A]): Cardinality[List[A]] = new Cardinality[List[A]] {
    def cardinality: Card = ???
  }

  // 4c. Implement either
  def either[A, B](a: Cardinality[A], b: Cardinality[B]): Cardinality[Either[A, B]] =
    new Cardinality[Either[A, B]] {
      def cardinality: Card = ???
    }

  // 4d. Implement tuple2
  def tuple2[A, B](a: Cardinality[A], b: Cardinality[B]): Cardinality[(A, B)] =
    new Cardinality[(A, B)] {
      def cardinality: Card = ???
    }

  // 4e. How many possible values exist of type String?
  val string: Cardinality[String] = new Cardinality[String] {
    def cardinality: Card = ???
  }

  // 4f. Implement func
  def func[A, B](a: Cardinality[A], b: Cardinality[B]): Cardinality[A => B] =
    new Cardinality[A => B] {
      def cardinality: Card = ???
    }

  // 4g. Implement isAdult1 and isAdult2, which one is better?
  def isAdult1: Cardinality[Int => Boolean] = new Cardinality[Int => Boolean] {
    def cardinality: Card = ???
  }
  def isAdult2: Cardinality[PosInt => Boolean] = new Cardinality[PosInt => Boolean] {
    def cardinality: Card = ???
  }

  // 4h. Implement getCurrency1 and getCurrency2, which one is better?
  def getCurrency1: Cardinality[String => Option[String]] = new Cardinality[String => Option[String]] {
    def cardinality: Card = ???
  }

  def getCurrency2: Cardinality[Country => Currency] = new Cardinality[Country => Currency] {
    def cardinality: Card = ???
  }

  // 4i How can we make compareInt more precise? Update the signature of compareInt2
  /** see [[Integer.compare]] */
  def compareInt1(x: Int, y: Int): Int = x - y

  def compareInt2 = ???

  // 4j. Can you provide two examples of function signature with only one implementation
  // i.e. find A1, A2 such as |A1 => A2| = 1

  // 4k. Can you provide an example of a function signature with no implementation
  // i.e. find A1, A2 such as |A1 => A2| = 0

  ////////////////////////
  // 5. Tests
  ////////////////////////

  // 5a. Given sign type signature and one unit test:
  // assert(sign(5) = true)
  // how many valid implementations exist for sign, i.e. how many pass type checker and tests
  def sign(x: Int): Boolean = ???

  // 5b. what if have 3 unit tests
  // assert(sign(-2) = false)
  // assert(sign( 0) = true)
  // assert(sign( 5) = true)
  // can you generalise for n unit tests?

  // 5c. How many implementations remain valid if I have the following property
  // forAll(x: Int => sign(x) == !sign(-x))

  // 5d. Can you think of other ways to reduce the number of valid implementations?
  // check out the following resources for more details:
  // Property-Based Testing in a Screencast Editor (by Oskar Wickström): https://wickstrom.tech/programming/2019/03/02/property-based-testing-in-a-screencast-editor-introduction.html
  // Types vs Tests (by Julien Truffaut): https://skillsmatter.com/skillscasts/12658-types-vs-tests

  ////////////////////////
  // 6. Parametricity
  ////////////////////////

  // 6a. How many implementations exist for id, const (assume we are using scalazzi subset)
  def id[A](a: A): A = ???

  def const[A, B](a: A)(b: B): A = ???

  // 6b. How many implementations exist for mapOption
  def mapOption[A, B](opt: Option[A])(f: A => B): Option[B] = ???

  // 6c. How many implementations exist for mapOptionIntToBool
  def mapOptionIntToBool(opt: Option[Int])(f: Int => Boolean): Option[Boolean] = ???

  // 6d. How many implementations exist for flatMapOption
  def flatMapOption[A, B](opt: Option[A])(f: A => Option[B]): Option[B] = ???

  // 6e. How would you test mapOption and flatMapOption to achieve a VIC of 1

  // 6f. How many implementations exist for mapList
  def mapList[A, B](xs: List[A])(f: A => B): List[B] = ???

  // 6g. How would you test mapList to achieve a VIC of 1

  // Further reading on parametricity
  // Counting type inhabitants (by Alexander Konovalov): https://alexknvl.com/posts/counting-type-inhabitants.html

  ////////////////////////
  // 7. Algebra
  ////////////////////////

  // 7a. in basic algebra, a * 1 = 1 * a = a and a + 0 = 0 + a = a (we say that 1 is the unit of * and 0 is the unit of +).
  // Is it also true with types?
  // to prove that two types A and B are equivalent you need to provide a pair of functions `to` and `from`
  // such as for all a: A, from(to(a)) == a, and equivalent for B
  def aUnitToA[A]: Iso[(A, Unit), A] =
    Iso[(A, Unit), A](
      { case (a, b) => ??? },
      a => ???
    )

  def aOrNothingToA[A]: Iso[Either[A, Nothing], A] =
    Iso(_ => ???, _ => ???)

  // 7b. Prove that Option is equivalent to Either[Unit,]
  def optionToEitherUnit[A]: Iso[Option[A], Either[Unit, A]] =
    Iso(_ => ???, _ => ???)

  // 7c. Prove that a * (b + c) = a * b + a * c
  // (A, Either[B, C]) =~ Either[(A, B), (A, C)] ?
  def distributeTuple[A, B, C]: Iso[(A, Either[B, C]), Either[(A, B), (A, C)]] =
    Iso(_ => ???, _ => ???)

  // 7d. Prove that a ^ 1 = a
  def power1[A]: Iso[Unit => A, A] =
    new Iso[Unit => A, A](
      _ => ???,
      _ => ???
    )

  // 7e. Can you think of any other properties that types and algebra have in common?

  ////////////////////////
  // 8. Extra Cardinality
  ////////////////////////

  sealed trait Zero

  case object One
  type One = One.type

  case class Pair[A, B](_1: A, _2: B)

  sealed trait Branch[A, B]
  object Branch {
    case class Left[A, B](value: A)  extends Branch[A, B]
    case class Right[A, B](value: B) extends Branch[A, B]
  }

  // 8a. Define Two a type containing 2 possible values using Zero, One, Pair and Branch
  type Two = Nothing // ???

  // 8b. Define Three a type containing 3 possible values using all previously defined types
  type Three = Nothing // ???

  // 8c. Define Four a type containing 4 possible values using all previously defined types
  type Four = Nothing // ???

  // 8d. Define Five a type containing 8 possible values using all previously defined types
  type Five = Nothing // ???

  // 8e. Define Eight type containing 8 possible values using Func and all previously defined types
  trait Func[A, B] {
    def apply(value: A): B
  }

  type Eight = Nothing // ???

}
