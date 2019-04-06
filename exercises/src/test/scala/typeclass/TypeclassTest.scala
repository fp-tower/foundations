package typeclass

import answers.typeclass.{MonoidAnswersLaws, TypeclassAnswers}
import exercises.typeclass.Monoid.syntax._
import exercises.typeclass.{MyId, TypeclassExercises}
import org.scalatest.{FunSuite, Matchers}
import org.typelevel.discipline.scalatest.Discipline
import toimpl.typeclass.{MonoidLawsToImpl, TypeclassToImpl}

class TypeclassExercisesTest extends TypeclassTest(TypeclassExercises, MonoidLaws)
class TypeclassAnswersTest extends TypeclassTest(TypeclassAnswers, MonoidAnswersLaws)

class TypeclassTest(impl: TypeclassToImpl, monoidLaws: MonoidLawsToImpl) extends FunSuite with Discipline with Matchers{
  import impl._

  test("check Double instance"){
    2.0.combine(3.5).combine(mempty[Double]) shouldEqual 5.5
  }

  test("check MyId instance"){
    MyId("foo").combine(MyId("bar")).combine(mempty[MyId]) shouldEqual MyId("foobar")
  }

  test("check List instance"){
    List(1,2,3).combine(List(4,5)).combine(mempty[List[Int]]) shouldEqual List(1,2,3,4,5)
  }

  test("check (Int, String) instance"){
    (3, "Hello").combine((5, "World")).combine(mempty[(Int, String)]) shouldEqual ((8, "HelloWorld"))
  }

  test("check (Int, Int) instance"){
    (3, 5).combine((5, 1)).combine(mempty[(Int, Int)]) shouldEqual ((8, 6))
  }

  test("sum"){
    sum(List(1,2,3,4)) shouldEqual 10
  }

  test("averageWordLength"){
    averageWordLength(List("", "ab", "abcd")) shouldEqual 2.0
  }

  test("isEmpty"){
    isEmpty(0) shouldEqual true
    isEmpty(5) shouldEqual false
    isEmpty("") shouldEqual true
    isEmpty("hello") shouldEqual false
  }

  test("ifEmpty"){
    ifEmpty("")("hello") shouldEqual "hello"
    ifEmpty("bar")("hello") shouldEqual "bar"
  }

  test("repeat"){
    repeat(3)("hello") shouldEqual "hellohellohello"
    repeat(0)("hello") shouldEqual ""
  }

  test("scsvFormat"){
    scsvFormat(List("foo", "bar", "buzz")) shouldEqual "foo;bar;buzz"
  }

  test("tupleFormat"){
    tupleFormat(List("foo", "bar")) shouldEqual "(foo,bar)"
  }

  test("foldMap"){
    foldMap(List("abc", "a", "abcde"))(_.length) == 9
  }


  test("check Option instance"){
    Option(3).combine(Option(4)).combine(mempty[Option[Int]]) shouldEqual Some(7)
  }

  test("check Map instance"){
    Map(3 -> "three", 4 -> "four").combine(Map(2 -> "deux", 4 -> "quatre")) shouldEqual Map(
      2 -> "deux",
      3 -> "three",
      4 -> "fourquatre"
    )
  }

  checkAll("Int", monoidLaws[Int])
  checkAll("Double", monoidLaws[Double])
  checkAll("Unit", monoidLaws[Unit])
  checkAll("String", monoidLaws[String])
  checkAll("List", monoidLaws[List[Boolean]])
  checkAll("Tuple2", monoidLaws[(Int, String)])
  checkAll("Option", monoidLaws[Option[Int]])
  checkAll("Map", monoidLaws[Map[Int, String]])

  checkAll("Int", monoidLaws.strong[Int])
}
