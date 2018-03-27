package ch2

object Ch2Exercises {

  // 1a. How many possible values are of type Boolean?

  // 1b. How many possible values are of type Char?

  // 1c. How many possible values are of type Unit?

  // 1d. How many possible values are of type String?

  // 1e. How many possible values are of type Option[Unit]?

  // 1f. How many possible values are of type Option[Boolean]?

  // 1g. How many possible values are of type (Boolean, Unit)?

  // 1h. How many possible values are of type (Boolean, Int)?

  // 1i. How many possible values are of type List[Unit]?

  // 1j. How many possible values are of type List[Boolean]?

  // 1k. How many possible values are of type Nothing?

  // 1l. How many possible values are of type Option[Nothing]?

  // 1m. How many possible values are of type List[Nothing]?

  // 1n. How many possible values are of type (Boolean, Nothing)?




  // 2a. Create a type containing 2 possible values using Zero, One, Pair and Branch
  sealed trait Zero

  case object One

  case class Pair[A, B](_1: A, _2: B)

  sealed trait Branch[A, B]
  object Branch {
    case class Left [A, B](value: A) extends Branch[A, B]
    case class Right[A, B](value: B) extends Branch[A, B]
  }

  // 2b. Create a type containing 3 possible values using Zero, One, Pair and Branch


  // 2c. Create a type containing 7 possible values using all previously defined types


  // 2d. Let's define |A| has the number of elements of type A
  // Express with a formula how many elements are of type Pair[A, B]


  // 2e. Express with a formula how many elements are of type Branch[A, B]


  // 2f. in basic algebra, a * 1 = 1 * a = a and a + 0 = 0 + a = a (we say that 1 is the unit of * and 0 is the unit of +).
  // Is it also true with types?


  // 2g. Is the algebra formed of Pair/Branch distributive? In other words, is it true that
  // Pair[A, Branch[B, C]] == Branch[Pair[A, B], Pair[A, C]] ?



  // 3.


}
