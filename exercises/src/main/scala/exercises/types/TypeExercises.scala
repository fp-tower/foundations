package exercises.types

import ACardinality.Finite
import eu.timepit.refined.types.numeric.PosInt
import toimpl.types.TypeToImpl

// You can run and print things here
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

  // 1. Cardinality

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

  // 1g. How many possible values are of type IntAndBoolean?
  val intAndBoolean: Cardinality[IntAndBoolean] = new Cardinality[IntAndBoolean] {
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


  // 1. Advanced Cardinality

  // 2a. Implement option that derives the cardinality of Option[A] from A
  def option[A](a: Cardinality[A]): Cardinality[Option[A]] =
    new Cardinality[Option[A]] {
      def cardinality: ACardinality = ???
    }

  // 2b. Implement list
  def list[A](a: Cardinality[A]): Cardinality[List[A]] = new Cardinality[List[A]] {
    def cardinality: ACardinality = ???
  }

  // 2c. Implement either
  def either[A, B](a: Cardinality[A], b: Cardinality[B]): Cardinality[Either[A, B]] =
    new Cardinality[Either[A, B]] {
      def cardinality: ACardinality = ???
    }

  // 2d. Implement tuple2
  def tuple2[A, B](a: Cardinality[A], b: Cardinality[B]): Cardinality[(A, B)] =
    new Cardinality[(A, B)] {
      def cardinality: ACardinality = ???
    }

  // 2e. How many possible values are of type String?
  val string: Cardinality[String] = new Cardinality[String] {
    def cardinality: ACardinality = ???
  }

  // 2f. Implement func
  def func[A, B](a: Cardinality[A], b: Cardinality[B]): Cardinality[A => B] =
    new Cardinality[A => B] {
      def cardinality: ACardinality = ???
    }

  // 2g. Implement isAdult1 and isAdult2, which one is better?
  def isAdult1: Cardinality[Int => Boolean] = new Cardinality[Int => Boolean] {
    def cardinality: ACardinality = ???
  }
  def isAdult2: Cardinality[PosInt => Boolean] = new Cardinality[PosInt => Boolean] {
    def cardinality: ACardinality = ???
  }

  // 2h. Implement getCurrency1 and getCurrency2, which one is better?
  def getCurrency1: Cardinality[String => Option[String]] = new Cardinality[String => Option[String]] {
    def cardinality: ACardinality = ???
  }

  def getCurrency2: Cardinality[Country => Currency] = new Cardinality[Country => Currency] {
    def cardinality: ACardinality = ???
  }


  // 2i How can we make compareInt more precise? Update the signature of compareInt2
  /** see [[Integer.compare]] */
  def compareInt1(x: Int, y: Int): Int = x - y

  def compareInt2 = ???



  // 2j. Can you provide two examples of function signature with only one implementation
  // i.e. find A1, A2 such as |A1 => A2| = 1



  // 2k. Can you provide an example of a function signature with no implementation
  // i.e. find A1, A2 such as |A1 => A2| = 0



  // 3. Tests

  // 3a. Given sign type signature and one unit test:
  // assert(sign(5) = true)
  // how many valid implementations exist for sign, i.e. how many pass type checker and tests
  def sign(x: Int): Boolean = ???


  // 3b. what if have 3 unit tests
  // assert(sign(-2) = false)
  // assert(sign( 0) = true)
  // assert(sign( 5) = true)
  // can you generalise for n unit tests?



  // 3c. How many implementations remain valid if I have the following property
  // forAll(x: Int => sign(x) == !sign(-x))




  // 3d. Can you think of other ways to reduce the number of valid implementations?
  // check out the following resources for more details:
  // Property-Based Testing in a Screencast Editor (by Oskar Wickström): https://wickstrom.tech/programming/2019/03/02/property-based-testing-in-a-screencast-editor-introduction.html
  // Types vs Tests (by Julien Truffaut): https://skillsmatter.com/skillscasts/12648-types-vs-tests



  // 4. Parametrictity

  // 4a. How many implementations exist for id, const (assume we are using scalazzi subset)
  def id[A](a: A): A = ???

  def const[A, B](a: A)(b: B): A = ???

  // 4b. How many implementations exist for mapOption
  def mapOption[A, B](opt: Option[A])(f: A => B): Option[B] = ???

  // 4c. How many implementations exist for mapOptionIntToBool
  def mapOptionIntToBool(opt: Option[Int])(f: Int => Boolean): Option[Boolean] = ???

  // 4d. How many implementations exist for flatMapOption
  def flatMapOption[A, B](opt: Option[A])(f: A => Option[B]): Option[B] = ???

  // 4e. How would you test mapOption and flatMapOption to achieve a VIC of 1

  // 4f. How many implementations exist for mapList
  def mapList[A, B](xs: List[A])(f: A => B): List[B] = ???


  // 4g. How would you test mapList to achieve a VIC of 1


  // Further reading on parametrictity
  // Counting type inhabitants (by Alexander Konovalov): https://alexknvl.com/posts/counting-type-inhabitants.html



  // 5. Logic

  // 5a. in basic algebra, a * 1 = 1 * a = a and a + 0 = 0 + a = a (we say that 1 is the unit of * and 0 is the unit of +).
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

  // 5b. Prove that Option is equivalent to Either[Unit,]
  def optionToEitherUnit[A]: Iso[Option[A], Either[Unit, A]] =
    Iso(_ => ???, _ => ???)


  // 5c. Prove that a * (b + c) = a * b + a * c
  // (A, Either[B, C]) =~ Either[(A, B), (A, C)] ?
  def distributeTuple[A, B, C]: Iso[(A, Either[B, C]), Either[(A, B), (A, C)]] =
    Iso(_ => ???, _ => ???)


  // 5d. Prove that a ^ 1 = a
  def power1[A]: Iso[Unit => A, A] =
    new Iso[Unit => A, A](
      _ => ???, _ => ???
    )


  // 5e. Can you think of any other properties that types and algebra have in common?



  sealed trait Zero

  case object One
  type One = One.type

  case class Pair[A, B](_1: A, _2: B)

  sealed trait Branch[A, B]
  object Branch {
    case class Left [A, B](value: A) extends Branch[A, B]
    case class Right[A, B](value: B) extends Branch[A, B]
  }

  // 6a. Define Two a type containing 2 possible values using Zero, One, Pair and Branch
  type Two = Nothing // ???

  // 6b. Define Three a type containing 3 possible values using all previously defined types
  type Three = Nothing // ???


  // 6c. Define Four a type containing 4 possible values using all previously defined types
  type Four = Nothing // ???


  // 6d. Define Five a type containing 8 possible values using all previously defined types
  type Five = Nothing // ???


  // 6e. Define Eight type containing 8 possible values using Func and all previously defined types
  trait Func[A, B]{
    def apply(value: A): B
  }

  type Eight = Nothing // ???

}
