package toimpl.function

import exercises.function.FunctionExercises.User

trait FunctionToImpl {

  ////////////////////////////
  // 1. first class functions
  ////////////////////////////

  val tripleVal: Int => Int

  def tripleList(xs: List[Int]): List[Int]

  val tripleVal2: Int => Int

  def move(increment: Boolean): Int => Int

  val move2: (Boolean, Int) => Int

  val move3: Boolean => Int => Int

  def applyMany(xs: List[Int => Int]): Int => List[Int]

  def applyManySum(xs: List[Int => Int]): Int => Int

  ////////////////////////////
  // 2. polymorphic functions
  ////////////////////////////

  def identity[A](x: A): A

  def const[A, B](a: A)(b: B): A

  def apply[A, B](value: A, f: A => B): B

  def apply2[A, B](value: A)(f: A => B): B

  def setAge(value: Int): List[User]

  def getUsersUnchanged: List[User]

  def andThen[A, B, C](f: A => B, g: B => C): A => C

  def compose[A, B, C](f: B => C, g: A => B): A => C

  val doubleInc: Int => Int

  val incDouble: Int => Int

  def curry[A, B, C](f: (A, B) => C): A => B => C

  def uncurry[A, B, C](f: A => B => C): (A, B) => C

  def join[A, B, C, D](f: A => B, g: A => C)(h: (B, C) => D): A => D

  ///////////////////////////
  // 3. Recursion & Laziness
  ///////////////////////////

  def sumList(xs: List[Int]): Int

  def sumList2(xs: List[Int]): Int

  def foldLeft[A, B](xs: List[A], z: B)(f: (B, A) => B): B

  def sumList3(xs: List[Int]): Int

  def find[A](xs: List[A])(p: A => Boolean): Option[A]

  def forAll(xs: List[Boolean]): Boolean

  def foldRight[A, B](xs: List[A], z: B)(f: (A, => B) => B): B

  def find2[A](xs: List[A])(p: A => Boolean): Option[A]

  def forAll2(xs: List[Boolean]): Boolean

  ////////////////////////
  // 5. Memoization
  ////////////////////////

  def memoize[A, B](f: A => B): A => B

}
