package answers.dataprocessing

import scala.annotation.tailrec

object StackSafeRecursiveAnswers {

  @tailrec
  def contains[A](list: List[A], value: A): Boolean =
    list match {
      case Nil          => false
      case head :: tail => if (head == value) true else contains(tail, value)
    }

  def unsafeSum(numbers: List[Int]): Int =
    numbers match {
      case Nil          => 0
      case head :: tail => head + unsafeSum(tail)
    }

  def sum(numbers: List[Int]): Int = {
    @tailrec
    def go(numbers: List[Int], accumulator: Int): Int =
      numbers match {
        case Nil          => accumulator
        case head :: tail => go(tail, accumulator + head)
      }
    go(numbers, 0)
  }

  def min(numbers: List[Int]): Option[Int] = {
    @tailrec
    def go(numbers: List[Int], accumulator: Option[Int]): Option[Int] =
      numbers match {
        case Nil => accumulator
        case head :: tail =>
          val newState = accumulator match {
            case None          => Some(head)
            case Some(current) => Some(current min head)
          }
          go(tail, newState)
      }
    go(numbers, None)
  }

  def size[A](items: List[A]): Int = {
    @tailrec
    def go(items: List[A], accumulator: Int): Int =
      items match {
        case Nil       => accumulator
        case _ :: tail => go(tail, accumulator + 1)
      }
    go(items, 0)
  }

  def reverse[A](items: List[A]): List[A] = {
    @tailrec
    def go(items: List[A], accumulator: List[A]): List[A] =
      items match {
        case Nil          => accumulator
        case head :: tail => go(tail, head :: accumulator)
      }
    go(items, Nil)
  }

  @tailrec
  def foldLeft[From, To](items: List[From], default: To)(combine: (To, From) => To): To =
    items match {
      case Nil => default
      case head :: tail =>
        val newDefault = combine(default, head)
        foldLeft(tail, newDefault)(combine)
    }
}
