package exercises.errorhandling

sealed trait InvariantOption[A] {
  import InvariantOption._

  def orElse(other: => InvariantOption[A]): InvariantOption[A] =
    this match {
      case Some(_) => this
      case None()  => other
    }

  def getOrElse(other: => A): A =
    this match {
      case Some(x) => x
      case None()  => other
    }
}

object InvariantOption {
  case class Some[A](value: A) extends InvariantOption[A]
  case class None[A]()         extends InvariantOption[A]
}
