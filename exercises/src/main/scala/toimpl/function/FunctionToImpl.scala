package toimpl.function

import exercises.function.FunctionExercises.Person

trait FunctionToImpl {

  def apply[A, B](f: A => B, value: A): B

  def identity[A](x: A): A

  def const[A, B](a: A)(b: B): A

  def tripleAge(xs: List[Person]): List[Person]

  def setAge(xs: List[Person], value: Int): List[Person]

  def noopAge(xs: List[Person]): List[Person]

  def updateAge2(f: Int => Int): List[Person] => List[Person]

  def setAge2(value: Int): List[Person] => List[Person]

  def andThen[A, B, C](f: A => B, g: B => C): A => C

  def compose[A, B, C](f: B => C, g: A => B): A => C

  val doubleInc: Int => Int

  def curry[A, B, C](f: (A, B) => C): A => B => C

  def uncurry[A, B, C](f: A => B => C): (A, B) => C

  def join[A, B, C, D](f: A => B, g: A => C)(h: (B, C) => D): A => D

  def sumList(xs: List[Int]): Int

  def memoize[A, B](f: A => B): A => B

}
