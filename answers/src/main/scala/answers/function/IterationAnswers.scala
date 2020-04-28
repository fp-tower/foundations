package answers.function

object IterationAnswers {

  ////////////////////////
  // Exercise 1: for loop
  ////////////////////////

  def sum(numbers: List[Int]): Int = {
    var state = 0
    for (x <- numbers) state += x
    state
  }

  def mkString(letters: List[Char]): String = {
    var state = ""
    for (x <- letters) state += x
    state
  }

  def wordCount(words: List[String]): Map[String, Int] = {
    var state = Map.empty[String, Int]
    for (x <- words) state = addKey(state, x)
    state
  }

  def addKey[K](state: Map[K, Int], key: K): Map[K, Int] =
    state.updatedWith(key) {
      case None    => Some(1)
      case Some(n) => Some(n + 1)
    }

  def foldLeft[A, B](items: List[A], default: B)(combine: (B, A) => B): B = {
    var state = default
    for (x <- items) state = combine(state, x)
    state
  }

  def sumFoldLeft(numbers: List[Int]): Int =
    foldLeft(numbers, 0)(_ + _)

  def mkStringFoldLeft(letters: List[Char]): String =
    foldLeft(letters, "")(_ + _)

  def wordCountFoldLeft(words: List[String]): Map[String, Int] =
    foldLeft(words, Map.empty[String, Int])(addKey)

  ///////////////////////////
  // Exercise 2: recursion
  ///////////////////////////

  def sumRecursive(numbers: List[Int]): Int =
    numbers match {
      case Nil          => 0
      case head :: tail => head + sumRecursive(tail)
    }
}
