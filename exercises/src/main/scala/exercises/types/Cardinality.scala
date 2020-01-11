package exercises.types

import cats.implicits._
import exercises.types.Card._

// Inspired by https://typelevel.org/blog/2018/11/02/semirings.html
trait Cardinality[A] {
  def cardinality: Card
}

sealed trait Card {

  def +(other: Card): Card = Plus(this, other)
  def *(other: Card): Card = Times(this, other)
  def ^(other: Card): Card = Pow(this, other)

  def eval: Option[BigInt] =
    simplify match {
      case Constant(x) => x.some
      case Plus(x, y)  => (x.eval, y.eval).mapN(_ + _)
      case Times(x, y) => (x.eval, y.eval).mapN(_ * _)
      case Pow(x, y)   => (x.eval, y.eval.filter(_.isValidInt).map(_.toInt)).mapN(_ pow _)
      case Inf         => None
    }

  def simplify: Card =
    this match {
      case Constant(_) => this
      case Plus(x, y) =>
        val xs = x.simplify
        val ys = y.simplify
        if (xs == Constant(0)) ys
        else if (ys == Constant(0)) xs
        else if (xs == Inf || ys == Inf) Inf
        else Plus(xs, ys)
      case Times(x, y) =>
        val xs = x.simplify
        val ys = y.simplify
        if (xs == Constant(0) || ys == Constant(0)) Constant(0)
        else if (xs == Constant(1)) ys
        else if (ys == Constant(1)) xs
        else if (xs == Inf || ys == Inf) Inf
        else Times(xs, ys)
      case Pow(x, y) =>
        val xs = x.simplify
        val ys = y.simplify
        if (ys == Constant(0)) Constant(1)
        else if (xs == Constant(0) || xs == Constant(1)) xs
        else if (xs == Inf || ys == Inf) Inf
        else Pow(xs, ys)
      case Inf => Inf
    }

  override def toString: String = simplify match {
    case Constant(x) => x.toString()
    case Plus(x, y)  => s"($x + $y)"
    case Times(x, y) => s"($x * $y)"
    case Pow(x, y)   => s"($x ^ $y)"
    case Inf         => "∞"
  }
}

object Card {
  case class Constant(value: BigInt)     extends Card
  case class Plus(lhs: Card, rhs: Card)  extends Card
  case class Times(lhs: Card, rhs: Card) extends Card
  case class Pow(lhs: Card, rhs: Card)   extends Card
  case object Inf                        extends Card
}
