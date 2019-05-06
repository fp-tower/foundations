package types

import answers.types.TypeAnswers
import cats.implicits._
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
    intOrBoolean.cardinality.eval shouldEqual (BigInt(2).pow(32) + 2).some
  }

  test("intAndBoolean") {
    intAndBoolean.cardinality.eval shouldEqual BigInt(2).pow(33).some
  }

  test("option") {
    option(boolean).cardinality.eval shouldEqual BigInt(3).some
    option(unit).cardinality.eval    shouldEqual BigInt(2).some
  }

  test("list") {
    list(boolean).cardinality.eval shouldEqual None
    list[Nothing](nothing).cardinality.eval shouldEqual BigInt(1).some
  }

  test("either") {
    either(boolean, unit).cardinality.eval shouldEqual BigInt(3).some
    either(byte, boolean).cardinality.eval shouldEqual BigInt(258).some
    either(unit, listUnit).cardinality.eval shouldEqual None
    either[Unit, Nothing](unit, nothing).cardinality.eval shouldEqual BigInt(1).some
  }

  test("tuple2") {
    tuple2(boolean, unit).cardinality.eval shouldEqual BigInt(2).some
    tuple2(byte, boolean).cardinality.eval shouldEqual BigInt(512).some
    tuple2(byte, boolean).cardinality.eval shouldEqual BigInt(512).some
    tuple2[Nothing, List[Boolean]](nothing, list(boolean)).cardinality.eval shouldEqual BigInt(0).some
    tuple2[List[Boolean], Nothing](list(boolean), nothing).cardinality.eval shouldEqual BigInt(0).some
  }

  test("func") {
    func(boolean, boolean).cardinality.eval shouldEqual BigInt(4).some
    func(boolean, unit).cardinality.eval shouldEqual BigInt(1).some
    func[Nothing, List[Boolean]](nothing, list(boolean)).cardinality.eval shouldEqual BigInt(1).some
    func(list(boolean), unit).cardinality.eval shouldEqual BigInt(1).some
    func[List[Boolean], Nothing](list(boolean), nothing).cardinality.eval shouldEqual BigInt(0).some
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
    Cardinality.of[Two].eval shouldEqual BigInt(2).some
  }

  test("Three") {
    Cardinality.of[Three].eval shouldEqual BigInt(3).some
  }

  test("Four") {
    Cardinality.of[Four].eval shouldEqual BigInt(4).some
  }

  test("Five") {
    Cardinality.of[Five].eval shouldEqual BigInt(5).some
  }

  test("Eight") {
    Cardinality.of[Eight].eval shouldEqual BigInt(8).some
  }

}

class TypeAnswersTest extends TypeToImplTest(TypeAnswers) {
  import TypeAnswers._

  test("Two") {
    Cardinality.of[Two].eval shouldEqual BigInt(2).some
  }

  test("Three") {
    Cardinality.of[Three].eval shouldEqual BigInt(3).some
  }

  test("Four") {
    Cardinality.of[Four_1].eval shouldEqual BigInt(4).some
    Cardinality.of[Four_2].eval shouldEqual BigInt(4).some
  }

  test("Five") {
    Cardinality.of[Five_1].eval shouldEqual BigInt(5).some
    Cardinality.of[Five_2].eval shouldEqual BigInt(5).some
  }

  test("Eight") {
    Cardinality.of[Eight].eval shouldEqual BigInt(8).some
  }

}