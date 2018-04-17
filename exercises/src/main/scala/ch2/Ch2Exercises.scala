package ch2

object Ch2Exercises {

  // 1a. How many possible values are of type Boolean?

  // 1b. How many possible values are of type Unit?

  // 1c. How many possible values are of type Char?

  // 1d. How many possible values are of type Int?

  // 1e. How many possible values are of type Option[Unit]?

  // 1f. How many possible values are of type Option[Boolean]?

  // 1g. How many possible values are of type (Boolean, Unit)?

  // 1h. How many possible values are of type (Boolean, Char)?

  // 1i. How many possible values are of type List[Unit]?

  // 1j. How many possible values are of type String?

  // 1k. How many possible values are of type Nothing?

  // 1l. How many possible values are of type Option[Nothing]?

  // 1m. How many possible values are of type (Boolean, Nothing)?



  // 2a. Create a type containing 2 possible values using Zero, One, Pair and Branch
  sealed trait Zero

  case object One

  case class Pair[A, B](_1: A, _2: B)

  sealed trait Branch[A, B]
  object Branch {
    case class Left [A, B](value: A) extends Branch[A, B]
    case class Right[A, B](value: B) extends Branch[A, B]
  }

  // 2b. Create a type containing 2 possible values using all previously defined types



  // 2c. Create a type containing 4 possible values using all previously defined types



  // 2d. Create a type containing 8 possible values using all previously defined types



  // 2e. Create a type containing 8 possible values using Func and all previously defined types
  trait Func[A, B]{
    def apply(value: A): B
  }



  // 3a. Let's define |A| has the number of elements of type A
  // Express with a formula how many elements are of type Pair[A, B]


  // 3b. Express with a formula how many elements are of type Branch[A, B]


  // 3c. Express with a formula how many elements are of type Func[A, B]


  // 3d. in basic algebra, a * 1 = 1 * a = a and a + 0 = 0 + a = a (we say that 1 is the unit of * and 0 is the unit of +).
  // Is it also true with types?


  // 3j. Is the algebra formed of Pair/Branch distributive? In other words, is it true that
  // Pair[A, Branch[B, C]] == Branch[Pair[A, B], Pair[A, C]] ?


  // 3k. Can you think of any other properties that types and basic algebra have in common?
  // e.g. What can you say about a + b or a * b


  // 3l. What does it mean `|A| == |B|`? What can we say about these two types?



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
