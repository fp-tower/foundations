package toimpl.function

trait FunctionToImpl {

  def apply[A, B](f: A => B, value: A): B

  def andThen[A, B, C](f: A => B, g: B => C): A => C

  def compose[A, B, C](f: B => C, g: A => B): A => C

  val doubleInc: Int => Int

  def curry[A, B, C](f: (A, B) => C): A => B => C

  def uncurry[A, B, C](f: A => B => C): (A, B) => C

  def join[A, B, C, D](f: A => B, g: A => C)(h: (B, C) => D): A => D

  def identity[A](x: A): A

  def const[A, B](a: A)(b: B): A

  def sumList(xs: List[Int]): Int

  def memoize[A, B](f: A => B): A => B

}
