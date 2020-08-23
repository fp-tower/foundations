package answers.dataprocessing

object ForLoopAnswers {

  def sum(numbers: List[Int]): Int = {
    var total = 0
    for (x <- numbers) total += x
    total
  }

  def size[A](elements: List[A]): Int = {
    var counter = 0
    for (_ <- elements) counter += 1
    counter
  }

  def min(numbers: List[Int]): Option[Int] = {
    var state = Option.empty[Int]
    for (number <- numbers) state = minState(state, number)
    state
  }

  private def minState(state: Option[Int], number: Int): Option[Int] =
    state match {
      case None               => Some(number)
      case Some(currentState) => Some(currentState min number)
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

  def foldLeft[From, To](elements: List[From], default: To)(combine: (To, From) => To): To = {
    var state = default
    for (element <- elements) state = combine(state, element)
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
