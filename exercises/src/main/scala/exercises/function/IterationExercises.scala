package exercises.function

object IterationExercises {
  ////////////////////////
  // Exercise 1: for loop
  ////////////////////////

  def size[A](items: List[A]): Int = {
    var counter = 0
    for (item <- items) counter += 1
    counter
  }

  // 1a. Implement `sum` using a for loop
  // such as sum(List(1,5,2)) == 8
  // and     sum(List()) == 0
  def sum(numbers: List[Int]): Int =
    ???

  // 1b. Implement `mkString` using a for loop
  // such as mkString(List('H', 'e', 'l', 'l', 'o')) == "Hello"
  // and     mkString(List()) == ""
  def mkString(letters: List[Char]): String =
    ???

  // 1c. Implement `wordCount` using a for loop.
  // `wordCount` compute how many times each word appears in a `List`
  // such as wordCount(List("Hi", "Hello", "Hi")) == Map("Hi" -> 2, "Hello" -> 1)
  // and     wordCount(List()) == Map()
  def wordCount(words: List[String]): Map[String, Int] =
    ???

  // 1d. `sum`, `mkString` and `wordCount` are quite similar.
  // Could you write a higher order function that captures this pattern?
  // Hint: this method is called `foldLeft`.
  def foldLeft[From, To](items: List[From], default: To)(combine: (To, From) => To): To =
    ???

  // 1e. Re-implement `sum`, `mkString` and `wordCount`  using `foldLeft`
  def sumFoldLeft(numbers: List[Int]): Int =
    ???

  def mkStringFoldLeft(letters: List[Char]): String =
    ???

  def wordCountFoldLeft(words: List[String]): Map[String, Int] =
    ???

  ///////////////////////////
  // Exercise 2: recursion
  ///////////////////////////

  def sizeRecursive[A](items: List[A]): Int =
    items match {
      case Nil          => 0
      case head :: tail => 1 + sizeRecursive(tail)
    }

  // 2a. Implement `sumRecursive`, a version of `sum` using recursion similar to `sizeRecursive`.
  def sumRecursive(numbers: List[Int]): Int =
    ???

  // 2b. You may have noticed that `sizeRecursive` and `sumRecursive` are not safe for large lists
  // e.g. List.fill(100000)(0)
  // Can you implement `sumRecursiveSafe`, another recursive version of `sum` which is safe for large lists.
  def sumRecursiveSafe(numbers: List[Int]): Int =
    ???

  // 2c (Optional). If you want to practice writing safe recursive functions, try to implement
  // `reverse` and `min` recursively such that:
  // reverse(List(1,2,3)) == List(3,2,1)
  // and
  // min(List(0,2,-5,4)) == Some(-5)
  // min(Nil) == None
  def reverse[A](items: List[A]): List[A] = ???

  def min(numbers: List[Int]): Option[Int] = ???

  // 2d. Implement `foldLeftRecursive` , a version of `foldLeft` using recursion.
  // Note: Try to make it stack-safe
  def foldLeftRecursive[From, To](items: List[From], default: To)(combine: (To, From) => To): To =
    ???
}
