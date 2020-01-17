package exercises.errorhandling

/**
  * InvOption is an Option with an invariant type parameter.
  * Standard Option in Scala is covariant.
  */
sealed trait InvOption[A] {
  import InvOption._

  def map[B](f: A => B): InvOption[B] =
    this match {
      case Some(a) => Some(f(a))
      case None()  => None()
    }

  def orElse(other: => InvOption[A]): InvOption[A] =
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

object InvOption {
  case class Some[A](value: A) extends InvOption[A]
  case class None[A]()         extends InvOption[A]
}
