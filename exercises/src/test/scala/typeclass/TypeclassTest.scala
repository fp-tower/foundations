package typeclass

import answers.typeclass.{MonoidAnswersLaws, TypeclassAnswers}
import cats.kernel.Eq
import cats.instances.all._
import exercises.typeclass.Monoid.syntax._
import exercises.typeclass._
import org.scalacheck.{Arbitrary, Cogen}
import org.scalatest.{FunSuite, Matchers}
import org.typelevel.discipline.scalatest.Discipline
import toimpl.typeclass.{MonoidLawsToImpl, TypeclassToImpl}

class TypeclassExercisesTest extends TypeclassTest(TypeclassExercises, MonoidLaws)
class TypeclassAnswersTest extends TypeclassTest(TypeclassAnswers, MonoidAnswersLaws)

class TypeclassTest(impl: TypeclassToImpl, monoidLaws: MonoidLawsToImpl) extends FunSuite with Discipline with Matchers with TypeclassTestInstance{
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
    Map("abc" -> 3, "xxx" -> 5).combine(Map("xxx" -> 2, "aaa" -> 1)) shouldEqual Map(
      "abc" -> 3,
      "xxx" -> 7,
      "aaa" -> 1
    )
  }

  checkAll("Int", monoidLaws[Int])
  checkAll("Double", monoidLaws[Double])
  checkAll("Unit", monoidLaws[Unit])
  checkAll("String", monoidLaws[String])
  checkAll("List", monoidLaws[List[Boolean]])
  checkAll("Vector", monoidLaws[Vector[Boolean]])
  checkAll("Set", monoidLaws[Set[Int]])
  checkAll("Tuple2", monoidLaws[(Int, String)])
  checkAll("Product", monoidLaws[Product])
  checkAll("All", monoidLaws[All])
  checkAll("Endo", monoidLaws[Endo[Int]])
  checkAll("Option", monoidLaws[Option[Int]])
  checkAll("Map", monoidLaws[Map[Int, String]])

  checkAll("Int", monoidLaws.strong[Int])
}

trait TypeclassTestInstance {
  implicit val arbProduct: Arbitrary[Product] = Arbitrary(Arbitrary.arbitrary[Int].map(Product(_)))
  implicit val arbAll: Arbitrary[All] = Arbitrary(Arbitrary.arbitrary[Boolean].map(All(_)))
  implicit def arbEndo[A: Arbitrary: Cogen]: Arbitrary[Endo[A]] = Arbitrary(Arbitrary.arbitrary[A => A].map(Endo(_)))
  implicit def eqEndo[A: Eq : Arbitrary]: Eq[Endo[A]] =
    new Eq[Endo[A]] {
      def eqv(x: Endo[A], y: Endo[A]): Boolean = {
        val samples = List.fill(50)(Arbitrary.arbitrary[A].sample).collect {
          case Some(a) => a
          case None    => sys.error("Could not generate arbitrary values to compare two functions")
        }
        samples.forall(s => Eq[A].eqv(x.getEndo(s), y.getEndo(s)))
      }
    }

}
