package toimpl.types

import exercises.types.{IntOrBoolean, Point}

trait TypeToImpl {

  val boolean: Cardinality[Boolean]

  val unit: Cardinality[Unit]

  val byte: Cardinality[Byte]

  val int: Cardinality[Int]

  val optUnit: Cardinality[Option[Unit]]

  val optBoolean: Cardinality[Option[Boolean]]

  val intOrBoolean: Cardinality[IntOrBoolean]

  val boolUnit: Cardinality[(Boolean, Unit)]

  val boolByte: Cardinality[(Boolean, Byte)]

  val point: Cardinality[Point]

  val listUnit: Cardinality[List[Unit]]

  val string: Cardinality[String]

  val nothing: Cardinality[Nothing]

  val optNothing: Cardinality[Option[Nothing]]

  val boolNothing: Cardinality[(Boolean, Nothing)]

  def option[A](a: Cardinality[A]): Cardinality[Option[A]]

  def list[A](a: Cardinality[A]): Cardinality[List[A]]

  def either[A, B](a: Cardinality[A], b: Cardinality[B]): Cardinality[Either[A, B]]

  def tuple2[A, B](a: Cardinality[A], b: Cardinality[B]): Cardinality[(A, B)]

  def func[A, B](a: Cardinality[A], b: Cardinality[B]): Cardinality[A => B]

  def aUnitToA[A](tuple: (A, Unit)): A
  def aToAUnit[A](a: A): (A, Unit)

  def aOrNothingToA[A](either: Either[A, Nothing]): A
  def aToAOrNothing[A](a: A): Either[A, Nothing]

  def optionToEitherUnit[A](option: Option[A]): Either[Unit, A]
  def eitherUnitToOption[A](either: Either[Unit, A]): Option[A]

  def distributeBranchTo[A, B, C](value: (A, Either[B, C])): Either[(A, B), (A, C)]
  def distributeBranchFrom[A, B, C](value: Either[(A, B), (A, C)]): (A, Either[B, C])

}
