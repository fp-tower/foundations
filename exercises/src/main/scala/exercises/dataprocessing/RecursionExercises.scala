package exercises.dataprocessing

object RecursionExercises {

  def size[A](items: List[A]): Int =
    items match {
      case Nil          => 0
      case head :: tail => 1 + size(tail)
    }

  // a. Implement `sum` using recursion similar to `size`
  // such as sum(List(1,5,2)) == 8
  // and     sum(List()) == 0
  def sum(numbers: List[Int]): Int =
    ???

  // b. You may have noticed that `size` is not stack-safe. In other words, it throws
  // a StackOverflowError if you call it with a large list e.g. size(List.fill(100000)(0))
  // Can you write a test that shows `size` is not stack-safe?
  // Do you know a way to avoid this error and still use a recursion?
  // If yes try to implement `sizeSafe`
  // Note: You can check that a function is stack-safe by adding a @tailrec annotation
  // before `def`, e.g. @tailrec def safeFunction(x: Int): Boolean = ???
  def sizeSafe[A](items: List[A]): Int =
    ???

  // c. Is your implementation of `sum` stack-safe? If not, implement `sumSafe`.
  def sumSafe(numbers: List[Int]): Int =
    ???

  // c. Implement `min` recursively
  // `reverse` and `min` recursively such that:
  // reverse(List(1,2,3)) == List(3,2,1)
  // and
  // min(List(0,2,-5,4)) == Some(-5)
  // min(Nil) == None
  def min(numbers: List[Int]): Option[Int] = ???

  // d. If you want to practice writing safe recursive functions, try to implement `reverse`
  // such as reverse(List(1,2,3)) == List(3,2,1)
  // and     reverse(Nil) == Nil
  def reverse[A](items: List[A]): List[A] = ???

  // d. Implement `foldLeft` , a version of `foldLeft` using recursion.
  // Note: Try to make it stack-safe
  def foldLeft[From, To](items: List[From], default: To)(combine: (To, From) => To): To =
    ???

}
