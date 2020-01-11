package types

import answers.types.TypeAnswers
import cats.Eq
import cats.implicits._
import exercises.types.TypeExercises._
import org.scalacheck.Arbitrary
import org.scalatest.Matchers
import org.scalatest.funsuite.AnyFunSuite
import org.typelevel.discipline.scalatest.Discipline

class TypeExercisesTest extends AnyFunSuite with Discipline with Matchers {

  test("intOrBoolean") {
    intOrBoolean.cardinality.eval shouldEqual Some(BigInt(2).pow(32) + 2)
  }

  test("intAndBoolean") {
    intAndBoolean.cardinality.eval shouldEqual Some(BigInt(2).pow(33))
  }

  test("option") {
    option(boolean).cardinality.eval shouldEqual Some(BigInt(3))
    option(unit).cardinality.eval shouldEqual Some(BigInt(2))
  }

  test("list") {
    list(boolean).cardinality.eval shouldEqual None
    list[Nothing](nothing).cardinality.eval shouldEqual Some(BigInt(1))
  }

  test("func") {
    func(boolean, boolean).cardinality.eval shouldEqual Some(BigInt(4))
    func(boolean, unit).cardinality.eval shouldEqual Some(BigInt(1))
    func[Nothing, List[Boolean]](nothing, list(boolean)).cardinality.eval shouldEqual Some(BigInt(1))
    func(list(boolean), unit).cardinality.eval shouldEqual Some(BigInt(1))
    func[List[Boolean], Nothing](list(boolean), nothing).cardinality.eval shouldEqual Some(BigInt(0))
  }

  checkAll("a * 1 == a", IsoLaws(aUnitToA[Int]))
  checkAll("a + 0 == a", IsoLaws(aOrNothingToA[Int]))
  checkAll("Option[A] <=> Either[Unit, A]", IsoLaws(optionToEitherUnit[Int]))
  checkAll("a ^ 1 ==  a", IsoLaws(power1[Int]))
  checkAll("a * (b + c) == a * b + a * c", IsoLaws(distributeTuple[Int, Int, Int]))

  implicit def arbAOrNothing[A: Arbitrary]: Arbitrary[Either[A, Nothing]] =
    Arbitrary(Arbitrary.arbitrary[A].map(Left(_)))

  implicit def eqAOrNothing[A: Eq]: Eq[Either[A, Nothing]] =
    Eq.by(_.fold(identity, TypeAnswers.absurd))

  implicit def eqUnitToA[A: Eq]: Eq[Unit => A] =
    Eq.by(_.apply(()))
}
