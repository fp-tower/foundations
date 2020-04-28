package exercises.function

object IterationExercises {
  ////////////////////////
  // Exercise 1: for loop
  ////////////////////////

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
  // `wordCount` tells us how many times each words appear in a `List`
  // such as wordCount(List("Hi", "Hello", "Hi")) == Map("Hi" -> 2, "Hello" -> 1)
  // and     wordCount(List()) == Map()
  def wordCount(words: List[String]): Map[String, Int] =
    ???

  // 1d. `sum`, `mkString` and `wordCount` are quite similar.
  // Could you write a higher order function that captures this pattern?
  // Hint: this method is called `foldLeft`.
  def foldLeft[A, B](items: List[A], default: B)(combine: (B, A) => B): B =
    ???

  // 1e. Re-implement `sum`, `mkString` and `wordCount`  using `foldLeft`
  def sumFoldLeft(numbers: List[Int]): Int =
    ???

  def mkStringFoldLeft(letters: List[Char]): String =
    ???

  def wordCountFoldLeft(words: List[String]): Map[String, Int] =
    ???
}
