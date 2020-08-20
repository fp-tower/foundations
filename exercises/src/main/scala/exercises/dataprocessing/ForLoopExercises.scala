package exercises.dataprocessing

object ForLoopExercises {

  def sum(numbers: List[Int]): Int = {
    var total = 0

    for (number <- numbers)
      total += number

    total
  }

  // a. Implement `size` using a mutable and a for loop
  // such as size(List(2,5,1,8)) == 4
  // and     size(Nil) == 0
  def size[A](items: List[Int]): Int =
    ???

  // b. Implement `min` using a mutable and a for loop
  // such as min(List(2,5,1,8)) == Some(1)
  // and     min(Nil) == None
  def min(numbers: List[Int]): Option[Int] =
    ???

  // c. Implement `wordCount` using a mutable and a for loop.
  // `wordCount` compute how many times each word appears in a `List`
  // such as wordCount(List("Hi", "Hello", "Hi")) == Map("Hi" -> 2, "Hello" -> 1)
  // and     wordCount(Nil) == Map()
  def wordCount(words: List[String]): Map[String, Int] =
    ???

  // d. `sum`, `size`, `min` and `wordCount` are quite similar.
  // Could you write a higher-order function that captures this pattern?
  // How should you call it?
  def pattern = ???

  // e. Refactor `sum`, `size`, `min` and `wordCount` using the higher-order
  // function you defined above.

}
