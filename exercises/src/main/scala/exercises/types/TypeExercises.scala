package exercises.types

import ACardinality.Finite
import toimpl.types.TypeToImpl

// you can run and print things here
object TypeApp extends App {
  import TypeExercises._

  println(boolean.cardinality)
}

object TypeExercises extends TypeToImpl {

  val boolean = new Cardinality[Boolean] {
    def cardinality: ACardinality = Finite(2)
  }

  val int: Cardinality[Int] = new Cardinality[Int] {
    def cardinality: ACardinality = Finite(BigInt(2).pow(32))
  }

  // 1a. How many possible values are of type Unit?
  val unit: Cardinality[Unit] = new Cardinality[Unit] {
    def cardinality: ACardinality = ???
  }

  // 1b. How many possible values are of type Byte?
  val byte: Cardinality[Byte] = new Cardinality[Byte] {
    def cardinality: ACardinality = ???
  }

  // 1c. How many possible values are of type Option[Unit]?
  val optUnit: Cardinality[Option[Unit]] = new Cardinality[Option[Unit]] {
    def cardinality: ACardinality = ???
  }

  // 1d. How many possible values are of type Option[Boolean]?
  val optBoolean: Cardinality[Option[Boolean]] = new Cardinality[Option[Boolean]] {
    def cardinality: ACardinality = ???
  }

  val intOrBoolean: Cardinality[IntOrBoolean] = new Cardinality[IntOrBoolean] {
    def cardinality: ACardinality = ???
  }

  // 1e. How many possible values are of type (Boolean, Unit)?
  val boolUnit: Cardinality[(Boolean, Unit)] = new Cardinality[(Boolean, Unit)] {
    def cardinality: ACardinality = ???
  }

  // 1f. How many possible values are of type (Boolean, Byte)?
  val boolByte: Cardinality[(Boolean, Byte)] = new Cardinality[(Boolean, Byte)] {
    def cardinality: ACardinality = ???
  }

  // 1g. How many possible values are of type Point?
  val point: Cardinality[Point] = new Cardinality[Point] {
    def cardinality: ACardinality = ???
  }

  // 1h. How many possible values are of type List[Unit]?
  val listUnit: Cardinality[List[Unit]] = new Cardinality[List[Unit]] {
    def cardinality: ACardinality = ???
  }

  // 1j. How many possible values are of type Nothing?
  val nothing: Cardinality[Nothing] = new Cardinality[Nothing] {
    def cardinality: ACardinality = ???
  }

  // 1k. How many possible values are of type Option[Nothing]?
  val optNothing: Cardinality[Option[Nothing]] = new Cardinality[Option[Nothing]] {
    def cardinality: ACardinality = ???
  }

  // 1l. How many possible values are of type (Boolean, Nothing)?
  val boolNothing: Cardinality[(Boolean, Nothing)] = new Cardinality[(Boolean, Nothing)] {
    def cardinality: ACardinality = ???
  }

  // 1m. How many possible values are of type Any?
  val any: Cardinality[Any] = new Cardinality[Any] {
    def cardinality: ACardinality = ???
  }



  // 2a. Implement option that derives the cardinality of Option[A] from A
  def option[A](a: Cardinality[A]): Cardinality[Option[A]] =
    new Cardinality[Option[A]] {
      def cardinality: ACardinality = ???
    }

  // 2b. Implement list
  def list[A](a: Cardinality[A]): Cardinality[List[A]] = new Cardinality[List[A]] {
    def cardinality: ACardinality = ???
  }

  // 2b. Implement either
  def either[A, B](a: Cardinality[A], b: Cardinality[B]): Cardinality[Either[A, B]] =
    new Cardinality[Either[A, B]] {
      def cardinality: ACardinality = ???
    }

  // 2c. Implement tuple2
  def tuple2[A, B](a: Cardinality[A], b: Cardinality[B]): Cardinality[(A, B)] =
    new Cardinality[(A, B)] {
      def cardinality: ACardinality = ???
    }

  // 2d. How many possible values are of type String?
  val string: Cardinality[String] = new Cardinality[String] {
    def cardinality: ACardinality = ???
  }

  // 2e. Implement func
  def func[A, B](a: Cardinality[A], b: Cardinality[B]): Cardinality[A => B] =
    new Cardinality[A => B] {
      def cardinality: ACardinality = ???
    }

  // 2f. Can you provide two examples of function signature with only one implementation (without type parameter)
  // i.e. find A1, A2 such as |A1 => A2| = 1



  // 2g. Can you provide an example of a function signature with no implementation (without type parameter)
  // i.e. find A1, A2 such as |A1 => A2| = 0



  // 2h. Given sign type signature and one unit test:
  // assert(sign(5) = true)
  // how many valid implementations exist for sign, i.e. how many pass type checker and tests
  def sign(x: Int): Boolean = ???


  // 2i. what if have 3 unit tests
  // assert(sign(-2) = false)
  // assert(sign( 0) = true)
  // assert(sign( 5) = true)
  // can you generalise for n unit tests?



  // 2j. How many implementations remain valid if I have the following property
  // forAll(x: Int => sign(x) == !sign(-x))




  // 2k. Can you think of other ways to reduce the number of valid implementations?
  // check out the following presentation for more details (shameless self promotion)
  // https://skillsmatter.com/skillscasts/12648-types-vs-tests




  // 3a. in basic algebra, a * 1 = 1 * a = a and a + 0 = 0 + a = a (we say that 1 is the unit of * and 0 is the unit of +).
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

  // 3b. Prove that Option is equivalent to Either[Unit,]
  def optionToEitherUnit[A]: Iso[Option[A], Either[Unit, A]] =
    Iso(_ => ???, _ => ???)


  // 3c. Prove that a * (b + c) = a * b + a * c
  // (A, Either[B, C]) =~ Either[(A, B), (A, C)] ?
  def distributeTuple[A, B, C]: Iso[(A, Either[B, C]), Either[(A, B), (A, C)]] =
    Iso(_ => ???, _ => ???)


  // 3d. Can you think of any other properties that types and algebra have in common?



  // 4
  sealed trait Zero

  case object One
  type One = One.type

  case class Pair[A, B](_1: A, _2: B)

  sealed trait Branch[A, B]
  object Branch {
    case class Left [A, B](value: A) extends Branch[A, B]
    case class Right[A, B](value: B) extends Branch[A, B]
  }

  // 4a. Define Two a type containing 2 possible values using Zero, One, Pair and Branch
  type Two = Nothing

  // 4b. Define Three a type containing 3 possible values using all previously defined types
  type Three = Nothing


  // 4c. Define Four a type containing 4 possible values using all previously defined types
  type Four = Nothing


  // 4d. Define Five a type containing 8 possible values using all previously defined types
  type Five = Nothing


  // 4e. Define Eight type containing 8 possible values using Func and all previously defined types
  trait Func[A, B]{
    def apply(value: A): B
  }

  type Eight = Nothing



  // 5a. Implement isAdult such as isAdult return true if age is greater or equal than 18
  def isAdult(age: Int): Boolean = ???





  // 5b. What if a user pass a negative number? e.g. isAdult(-5)
  // how would update the signature to prevent that
  def isAdult_v2 = ???





  // 5c. What is the most precise type? Why?
  // Int                  => Option[Boolean]
  // Int Refined Positive => Boolean          (see https://github.com/fthomas/refined)





  // 6a. Implement compareInt such as it return:
  // -1 if x < y
  //  0 if x == y
  //  1 if x > y
  /** see [[Integer.compare]] */
  def compareInt(x: Int, y: Int): Int = ???



  // 6b. why can we say that compareInt is imprecise? Implement a more precise compareInt_v2
  def compareInt_v2 = ???



  // 7a. implement the cardinality of getCurrency1 and getCurrency2, which one is bigger?
  def getCurrency1: Cardinality[String => Option[String]] = new Cardinality[String => Option[String]] {
    def cardinality: ACardinality = ???
  }

  def getCurrency2: Cardinality[Country => String] = new Cardinality[Country => String] {
    def cardinality: ACardinality = ???
  }

}
