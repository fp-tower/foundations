package toimpl.function

import exercises.function.FunctionExercises.Person

trait FunctionToImpl {

  def identity[A](x: A): A

  def const[A, B](a: A)(b: B): A

  val tripleVal: Int => Int

  def tripleAge(xs: List[Person]): List[Person]

  def setAge(xs: List[Person], value: Int): List[Person]

  def noopAge(xs: List[Person]): List[Person]

  def apply[A, B](f: A => B, value: A): B

  def andThen[A, B, C](f: A => B, g: B => C): A => C

  def compose[A, B, C](f: B => C, g: A => B): A => C

  val doubleInc: Int => Int

  val incDouble: Int => Int

  def curry[A, B, C](f: (A, B) => C): A => B => C

  def uncurry[A, B, C](f: A => B => C): (A, B) => C

  def join[A, B, C, D](f: A => B, g: A => C)(h: (B, C) => D): A => D

  def sumList(xs: List[Int]): Int

  def sumList2(xs: List[Int]): Int

  def foldLeft[A, B](xs: List[A], z: B)(f: (B, A) => B): B

  def foldRight[A, B](xs: List[A], z: B)(f: (A, => B) => B): B

  def sumList3(xs: List[Int]): Int

  def find[A](xs: List[A])(p: A => Boolean): Option[A]

  def memoize[A, B](f: A => B): A => B

}
