package exercises.types

import toimpl.types.{ACardinality, Cardinality, TypeToImpl}

object TypeExercises extends TypeToImpl {

  // 1a. How many possible values are of type Boolean?
  val boolean = new Cardinality[Boolean] {
    def cardinality: ACardinality = ???
  }

  // 1b. How many possible values are of type Unit?
  val unit: Cardinality[Unit] = new Cardinality[Unit] {
    def cardinality: ACardinality = ???
  }

  // 1c. How many possible values are of type Byte?
  val byte: Cardinality[Byte] = new Cardinality[Byte] {
    def cardinality: ACardinality = ???
  }

  // 1d. How many possible values are of type Int?
  val int: Cardinality[Int] = new Cardinality[Int] {
    def cardinality: ACardinality = ???
  }

  // 1e. How many possible values are of type Option[Unit]?
  val optUnit: Cardinality[Option[Unit]] = new Cardinality[Option[Unit]] {
    def cardinality: ACardinality = ???
  }

  // 1f. How many possible values are of type Option[Boolean]?
  val optBoolean: Cardinality[Boolean] = new Cardinality[Boolean] {
    def cardinality: ACardinality = ???
  }

  // 1g. How many possible values are of type (Boolean, Unit)?
  val boolUnit: Cardinality[(Boolean, Unit)] = new Cardinality[(Boolean, Unit)] {
    def cardinality: ACardinality = ???
  }

  // 1h. How many possible values are of type (Boolean, Char)?
  val boolChar: Cardinality[(Boolean, Char)] = new Cardinality[(Boolean, Char)] {
    def cardinality: ACardinality = ???
  }

  // 1i. How many possible values are of type List[Unit]?
  val listUnit: Cardinality[List[Unit]] = new Cardinality[List[Unit]] {
    def cardinality: ACardinality = ???
  }

  // 1j. How many possible values are of type String?
  val string: Cardinality[String] = new Cardinality[String] {
    def cardinality: ACardinality = ???
  }

  // 1k. How many possible values are of type Nothing?
  val nothing: Cardinality[Nothing] = new Cardinality[Nothing] {
    def cardinality: ACardinality = ???
  }

  // 1l. How many possible values are of type Option[Nothing]?
  val optNothing: Cardinality[Option[Nothing]] = new Cardinality[Option[Nothing]] {
    def cardinality: ACardinality = ???
  }

  // 1m. How many possible values are of type (Boolean, Nothing)?
  val boolNothing: Cardinality[(Boolean, Nothing)] = new Cardinality[(Boolean, Nothing)] {
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


  // 2d. Implement func
  def func[A, B](a: Cardinality[A], b: Cardinality[B]): Cardinality[A => B] =
    new Cardinality[A => B] {
      def cardinality: ACardinality = ???
    }

  // 2d. in basic algebra, a * 1 = 1 * a = a and a + 0 = 0 + a = a (we say that 1 is the unit of * and 0 is the unit of +).
  // Is it also true with types?
  // to prove that two types A and B are equivalent you need to provide a pair of functions `to` and `from`
  // such as for all a: A, from(to(a)) == a, and equivalent for B
  def aUnitToA[A](tuple: (A, Unit)): A = ???

  def aToAUnit[A](a: A): (A, Unit) = ???

  def aOrNothingToA[A](either: Either[A, Nothing]): A = ???

  def aToAOrNothing[A](a: A): Either[A, Nothing] = ???

  def optionToEitherUnit[A](option: Option[A]): Either[Unit, A] = ???

  def eitherUnitToOption[A](either: Either[Unit, A]): Option[A] = ???


  // 2j. Is the algebra formed of Pair/Branch distributive? In other words, is it true that
  // Pair[A, Branch[B, C]] == Branch[Pair[A, B], Pair[A, C]] ?


  // 2k. Can you think of any other properties that types and basic algebra have in common?
  // e.g. What can you say about a + b or a * b


  // 2l. What does it mean `|A| == |B|`? What can we say about these two types?



  // 3
  sealed trait Zero

  case object One
  type One = One.type

  case class Pair[A, B](_1: A, _2: B)

  sealed trait Branch[A, B]
  object Branch {
    case class Left [A, B](value: A) extends Branch[A, B]
    case class Right[A, B](value: B) extends Branch[A, B]
  }

  // 3a. Define Two a type containing 2 possible values using Zero, One, Pair and Branch
  type Two = Nothing

  // 3b. Define Three a type containing 3 possible values using all previously defined types
  type Three = Nothing


  // 3c. Define Four a type containing 4 possible values using all previously defined types
  type Four = Nothing


  // 3d. Define Five a type containing 8 possible values using all previously defined types
  type Five = Nothing


  // 3e. Define Eight type containing 8 possible values using Func and all previously defined types
  trait Func[A, B]{
    def apply(value: A): B
  }

  type Eight = Nothing



  // 4a. Implement isAdult
  def isAdult(i: Int): Boolean = ???





  // 4b. What if a user pass a negative number? e.g. isAdult(-5)
  // how would update the signature and implementation of isAdult





  // 4c. What is the most precise type? Why?
  // Int                  => Option[Boolean]
  // Int Refined Positive => Boolean          (see https://github.com/fthomas/refined)





  // 5a. Implement compareInt such as it return:
  // -1 if x < y
  //  0 if x == y
  //  1 if x > y
  /** see [[Integer.compare]] */
  def compareInt(x: Int, y: Int): Int = ???



  // 5b. why can we say that compareInt is imprecise? Implement a more precise compareInt_v2
  def compareInt_v2 = ???



  // 6a. implement getCountryDialCode for UK, France, Germany
  // UK is country code is 44, France is 33 and Germany is 49
  def getCountryDialCode(country: String): Int = ???


  // 6b. what happens if someone call getCountryDialCode with "Italy", "france" or "Grande Bretagne" (french version of UK)
  // change getCountryDialCode to avoid this situation


}
