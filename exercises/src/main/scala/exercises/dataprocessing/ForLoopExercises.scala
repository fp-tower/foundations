package exercises.dataprocessing

object ForLoopExercises {

  def size[A](items: List[A]): Int = {
    var counter = 0
    for (item <- items) counter += 1
    counter
  }

  // a. Implement `sum` using a for loop
  // such as sum(List(1,5,2)) == 8
  // and     sum(List()) == 0
  def sum(numbers: List[Int]): Int =
    ???

  // b. Implement `mkString` using a for loop
  // such as mkString(List('H', 'e', 'l', 'l', 'o')) == "Hello"
  // and     mkString(List()) == ""
  def mkString(letters: List[Char]): String =
    ???

  // c. Implement `wordCount` using a for loop.
  // `wordCount` compute how many times each word appears in a `List`
  // such as wordCount(List("Hi", "Hello", "Hi")) == Map("Hi" -> 2, "Hello" -> 1)
  // and     wordCount(List()) == Map()
  def wordCount(words: List[String]): Map[String, Int] =
    ???

  // d. `sum`, `mkString` and `wordCount` are quite similar.
  // Could you write a higher order function that captures this pattern?
  // Hint: this method is called `foldLeft`.
  def foldLeft[From, To](items: List[From], default: To)(combine: (To, From) => To): To =
    ???

  // e. Re-implement `sum`, `mkString` and `wordCount`  using `foldLeft`
  def sumFoldLeft(numbers: List[Int]): Int =
    ???

  def mkStringFoldLeft(letters: List[Char]): String =
    ???

  def wordCountFoldLeft(words: List[String]): Map[String, Int] =
    ???

}
