package types

import answers.types.TypeAnswers
import exercises.types.TypeExercises
import org.scalacheck.Arbitrary
import org.scalatest.{FunSuite, Matchers}
import org.typelevel.discipline.scalatest.Discipline
import toimpl.types.ACardinality.{Finite, Infinite}
import toimpl.types.{Cardinality, TypeToImpl}

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
    list[Nothing](nothing).cardinality shouldEqual Finite(0)
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
  }

  test("func") {
    func(boolean, boolean).cardinality shouldEqual Finite(4)
    func(boolean, unit).cardinality shouldEqual Finite(1)
    func[Int, Nothing](int, nothing).cardinality shouldEqual Finite(0)
  }

  checkAll("(A, Unit) <=> A", IsoLaws(aUnitToA[Int]))
  checkAll("Either[A, Nothing] <=> A", IsoLaws(aOrNothingToA[Int]))
  checkAll("Option[A] <=> Either[Unit, A]", IsoLaws(optionToEitherUnit[Int]))
  checkAll("(A, Either[B, C]) <=> Either[(A, B), (A, C)]", IsoLaws(distributeEither[Int, Int, Int]))

  test("isAdult") {
    isAdult(10) shouldBe false
    isAdult(25) shouldBe true
  }

  test("compareInt") {
    compareInt(10, 15) shouldBe -1
    compareInt(10, 10) shouldBe 0
    compareInt(15, 10) shouldBe 1
  }

  implicit def arbAOrNothing[A: Arbitrary]: Arbitrary[Either[A, Nothing]] =
    Arbitrary(Arbitrary.arbitrary[A].map(Left(_)))
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