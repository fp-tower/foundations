package exercises.errorhandling

import cats.data.NonEmptyList

sealed trait Validated[+E, +A]

object Validated {
  case class Invalid[+E](value: E) extends Validated[E, Nothing]
  case class Valid[+A](value: A)   extends Validated[Nothing, A]

  def invalid[E, A](value: E): Validated[E, A]                  = Invalid(value)
  def invalidNel[E, A](value: E): Validated[NonEmptyList[E], A] = invalid(NonEmptyList.of(value))

  def valid[E, A](value: A): Validated[E, A]                  = Valid(value)
  def validNel[E, A](value: A): Validated[NonEmptyList[E], A] = valid(value)
}
