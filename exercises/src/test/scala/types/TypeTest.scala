package types

import answers.types.TypeAnswers
import cats.implicits._
import cats.kernel.Eq
import exercises.types.ACardinality.{Finite, Infinite}
import exercises.types.{Cardinality, TypeExercises}
import org.scalacheck.Arbitrary
import org.scalatest.{FunSuite, Matchers}
import org.typelevel.discipline.scalatest.Discipline
import toimpl.types.TypeToImpl

class TypeToImplTest(impl: TypeToImpl) extends FunSuite with Discipline with Matchers {
  import impl._

  test("boolean - optUnit") {
    boolean.cardinality shouldEqual optUnit.cardinality
  }

  test("intOrBoolean") {
    intOrBoolean.cardinality shouldEqual Finite(BigInt(2).pow(32) + 2)
  }

  test("point") {
    point.cardinality shouldEqual Finite(BigInt(2).pow(64))
  }

  test("option") {
    option(boolean).cardinality shouldEqual Finite(3)
    option(unit).cardinality    shouldEqual Finite(2)
  }

  test("list") {
    list(boolean).cardinality shouldEqual Infinite
    list[Nothing](nothing).cardinality shouldEqual Finite(1)
  }

  test("either") {
    either(boolean, unit).cardinality shouldEqual Finite(3)
    either(byte, boolean).cardinality shouldEqual Finite(258)
    either(unit, listUnit).cardinality shouldEqual Infinite
    either[Unit, Nothing](unit, nothing).cardinality shouldEqual Finite(1)
  }

  test("tuple2") {
    tuple2(boolean, unit).cardinality shouldEqual Finite(2)
    tuple2(byte, boolean).cardinality shouldEqual Finite(512)
    tuple2(byte, boolean).cardinality shouldEqual Finite(512)
    tuple2[Nothing, List[Boolean]](nothing, list(boolean)).cardinality shouldEqual Finite(0)
    tuple2[List[Boolean], Nothing](list(boolean), nothing).cardinality shouldEqual Finite(0)
  }

  test("func") {
    func(boolean, boolean).cardinality shouldEqual Finite(4)
    func(boolean, unit).cardinality shouldEqual Finite(1)
    func[Nothing, List[Boolean]](nothing, list(boolean)).cardinality shouldEqual Finite(1)
    func(list(boolean), unit).cardinality shouldEqual Finite(1)
    func[List[Boolean], Nothing](list(boolean), nothing).cardinality shouldEqual Finite(0)
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
    Cardinality.of[Two] shouldEqual Finite(2)
  }

  test("Three") {
    Cardinality.of[Three] shouldEqual Finite(3)
  }

  test("Four") {
    Cardinality.of[Four] shouldEqual Finite(4)
  }

  test("Five") {
    Cardinality.of[Five] shouldEqual Finite(5)
  }

  test("Eight") {
    Cardinality.of[Eight] shouldEqual Finite(8)
  }

}

class TypeAnswersTest extends TypeToImplTest(TypeAnswers) {
  import TypeAnswers._

  test("Two") {
    Cardinality.of[Two] shouldEqual Finite(2)
  }

  test("Three") {
    Cardinality.of[Three] shouldEqual Finite(3)
  }

  test("Four") {
    Cardinality.of[Four_1] shouldEqual Finite(4)
    Cardinality.of[Four_2] shouldEqual Finite(4)
  }

  test("Five") {
    Cardinality.of[Five_1] shouldEqual Finite(5)
    Cardinality.of[Five_2] shouldEqual Finite(5)
  }

  test("Eight") {
    Cardinality.of[Eight] shouldEqual Finite(8)
  }

}