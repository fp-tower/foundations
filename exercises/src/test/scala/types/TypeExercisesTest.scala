package types

import answers.types.TypeAnswers
import cats.Eq
import cats.implicits._
import exercises.types.TypeExercises._
import org.scalacheck.Arbitrary
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.Checkers
import org.typelevel.discipline.scalatest.{Discipline, FunSuiteDiscipline}

class TypeExercisesTest extends AnyFunSuite with FunSuiteDiscipline with Checkers with Matchers {

  test("intOrBoolean") {
    intOrBoolean.cardinality.eval shouldEqual Some(BigInt(2).pow(32) + 2)
  }

  test("intAndBoolean") {
    intAndBoolean.cardinality.eval shouldEqual Some(BigInt(2).pow(33))
  }

  test("func") {
    func(boolean, boolean).cardinality.eval shouldEqual Some(BigInt(4))
    func(boolean, unit).cardinality.eval shouldEqual Some(BigInt(1))
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
