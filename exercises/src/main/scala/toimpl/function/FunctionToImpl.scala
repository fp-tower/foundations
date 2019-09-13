package toimpl.function

import exercises.function.FunctionExercises.{Direction, User}

trait FunctionToImpl {

  ////////////////////////////
  // 1. first class functions
  ////////////////////////////

  val tripleVal: Int => Int

  def tripleList(xs: List[Int]): List[Int]

  val tripleVal2: Int => Int

  def move(direction: Direction)(x: Int): Int

  ////////////////////////////
  // 2. polymorphic functions
  ////////////////////////////

  def identity[A](x: A): A

  def const[A, B](a: A)(b: B): A

  def setUsersAge(value: Int): List[User]

  def getUsers: List[User]

  def andThen[A, B, C](f: A => B, g: B => C): A => C

  def compose[A, B, C](f: B => C, g: A => B): A => C

  val doubleInc: Int => Int

  val incDouble: Int => Int

  ///////////////////////////
  // 3. Recursion & Laziness
  ///////////////////////////

  def sumList(xs: List[Int]): Int

  def sumList2(xs: List[Int]): Int

  def sumList3(xs: List[Int]): Int

  def mkString(xs: List[Char]): String

  def mkString2(xs: List[Char]): String

  def multiply(xs: List[Int]): Int

  def filter[A](xs: List[A])(p: A => Boolean): List[A]

  def find[A](xs: List[A])(p: A => Boolean): Option[A]

  def forAll(xs: List[Boolean]): Boolean

  def find2[A](xs: List[A])(p: A => Boolean): Option[A]

  def forAll2(xs: List[Boolean]): Boolean

  ////////////////////////
  // 5. Memoization
  ////////////////////////

  def memoize[A, B](f: A => B): A => B

}
