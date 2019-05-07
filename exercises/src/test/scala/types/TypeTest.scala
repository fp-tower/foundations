package types

import answers.types.TypeAnswers
import exercises.typeclass.Eq
import exercises.types.{Cardinality, TypeExercises}
import org.scalacheck.Arbitrary
import org.scalatest.{FunSuite, Matchers}
import org.typelevel.discipline.scalatest.Discipline
import toimpl.types.TypeToImpl

class TypeToImplTest(impl: TypeToImpl) extends FunSuite with Discipline with Matchers {
  import impl._

  test("boolean - optUnit") {
    boolean.cardinality.eval shouldEqual optUnit.cardinality.eval
  }

  test("intOrBoolean") {
    intOrBoolean.cardinality.eval shouldEqual Some(BigInt(2).pow(32) + 2)
  }

  test("intAndBoolean") {
    intAndBoolean.cardinality.eval shouldEqual Some(BigInt(2).pow(33))
  }

  test("option") {
    option(boolean).cardinality.eval shouldEqual Some(BigInt(3))
    option(unit).cardinality.eval    shouldEqual Some(BigInt(2))
  }

  test("list") {
    list(boolean).cardinality.eval shouldEqual None
    list[Nothing](nothing).cardinality.eval shouldEqual Some(BigInt(1))
  }

  test("either") {
    either(boolean, unit).cardinality.eval shouldEqual Some(BigInt(3))
    either(byte, boolean).cardinality.eval shouldEqual Some(BigInt(258))
    either(unit, listUnit).cardinality.eval shouldEqual None
    either[Unit, Nothing](unit, nothing).cardinality.eval shouldEqual Some(BigInt(1))
  }

  test("tuple2") {
    tuple2(boolean, unit).cardinality.eval shouldEqual Some(BigInt(2))
    tuple2(byte, boolean).cardinality.eval shouldEqual Some(BigInt(512))
    tuple2(byte, boolean).cardinality.eval shouldEqual Some(BigInt(512))
    tuple2[Nothing, List[Boolean]](nothing, list(boolean)).cardinality.eval shouldEqual Some(BigInt(0))
    tuple2[List[Boolean], Nothing](list(boolean), nothing).cardinality.eval shouldEqual Some(BigInt(0))
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

class TypeExercisesTest extends TypeToImplTest(TypeExercises) {
  import TypeExercises._

  test("Two") {
    Cardinality.of[Two].eval shouldEqual Some(BigInt(2))
  }

  test("Three") {
    Cardinality.of[Three].eval shouldEqual Some(BigInt(3))
  }

  test("Four") {
    Cardinality.of[Four].eval shouldEqual Some(BigInt(4))
  }

  test("Five") {
    Cardinality.of[Five].eval shouldEqual Some(BigInt(5))
  }

  test("Eight") {
    Cardinality.of[Eight].eval shouldEqual Some(BigInt(8))
  }

}

class TypeAnswersTest extends TypeToImplTest(TypeAnswers) {
  import TypeAnswers._

  test("Two") {
    Cardinality.of[Two].eval shouldEqual Some(BigInt(2))
  }

  test("Three") {
    Cardinality.of[Three].eval shouldEqual Some(BigInt(3))
  }

  test("Four") {
    Cardinality.of[Four_1].eval shouldEqual Some(BigInt(4))
    Cardinality.of[Four_2].eval shouldEqual Some(BigInt(4))
  }

  test("Five") {
    Cardinality.of[Five_1].eval shouldEqual Some(BigInt(5))
    Cardinality.of[Five_2].eval shouldEqual Some(BigInt(5))
  }

  test("Eight") {
    Cardinality.of[Eight].eval shouldEqual Some(BigInt(8))
  }

}