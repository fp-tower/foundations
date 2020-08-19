package answers.dataprocessing

object ForLoopAnswers {

  def sum(numbers: List[Int]): Int = {
    var state = 0
    for (x <- numbers) state += x
    state
  }

  def size[A](items: List[A]): Int = {
    var counter = 0
    for (_ <- items) counter += 1
    counter
  }

  def min(numbers: List[Int]): Option[Int] = {
    var state = Option.empty[Int]
    for (number <- numbers)
      state match {
        case None               => state = Some(number)
        case Some(currentState) => state = Some(currentState min number)
      }

    state
  }

  def wordCount(words: List[String]): Map[String, Int] = {
    var state = Map.empty[String, Int]
    for (word <- words) state = addKey(state, word)
    state
  }

  def addKey[K](state: Map[K, Int], key: K): Map[K, Int] =
    state.updatedWith(key) {
      case None    => Some(1)
      case Some(n) => Some(n + 1)
    }

  def foldLeft[From, To](items: List[From], default: To)(combine: (To, From) => To): To = {
    var state = default
    for (x <- items) state = combine(state, x)
    state
  }

  def sumFoldLeft(numbers: List[Int]): Int =
    foldLeft(numbers, 0)(_ + _)

  def sizeFoldLeft[A](numbers: List[A]): Int =
    foldLeft(numbers, 0)((acc, _) => acc + 1)

  def minFoldLeft(numbers: List[Int]): Option[Int] =
    foldLeft(numbers, Option.empty[Int]) {
      case (None, number)      => Some(number)
      case (Some(acc), number) => Some(acc min number)
    }

  def wordCountFoldLeft(words: List[String]): Map[String, Int] =
    foldLeft(words, Map.empty[String, Int])(addKey)

}
