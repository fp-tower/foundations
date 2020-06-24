package answers.function

import scala.annotation.tailrec

object IterationAnswers {

  ////////////////////////
  // Exercise 1: for loop
  ////////////////////////

  def size[A](items: List[A]): Int = {
    var counter = 0
    for (item <- items) counter += 1
    counter
  }

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

  def foldLeft[From, To](items: List[From], default: To)(combine: (To, From) => To): To = {
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

  def sizeRecursive[A](items: List[A]): Int =
    items match {
      case Nil       => 0
      case _ :: tail => 1 + sizeRecursive(tail)
    }

  def sumRecursive(numbers: List[Int]): Int =
    numbers match {
      case Nil          => 0
      case head :: tail => head + sumRecursive(tail)
    }

  def sumRecursiveSafe(numbers: List[Int]): Int =
    _sumRecursiveSafe(numbers, 0)

  @tailrec
  def _sumRecursiveSafe(numbers: List[Int], state: Int): Int =
    numbers match {
      case Nil          => state
      case head :: tail => _sumRecursiveSafe(tail, state + head)
    }

  def reverse[A](items: List[A]): List[A] =
    _reverse(items, Nil)

  @tailrec
  def _reverse[A](items: List[A], state: List[A]): List[A] =
    items match {
      case Nil          => state
      case head :: tail => _reverse(tail, head :: state)
    }

  def min(numbers: List[Int]): Option[Int] =
    _min(numbers, None)

  @tailrec
  def _min(numbers: List[Int], state: Option[Int]): Option[Int] =
    numbers match {
      case Nil          => state
      case head :: tail => _min(tail, state.map(_.min(head)).orElse(Some(head)))
    }

  @tailrec
  def foldLeftRecursive[From, To](items: List[From], default: To)(combine: (To, From) => To): To =
    items match {
      case Nil => default
      case head :: tail =>
        val newDefault = combine(default, head)
        foldLeftRecursive(tail, newDefault)(combine)
    }
}
