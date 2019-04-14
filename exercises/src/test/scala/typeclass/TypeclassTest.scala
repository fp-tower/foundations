package typeclass

import answers.typeclass.TypeclassAnswers
import cats.data.NonEmptyList
import cats.kernel.Eq
import cats.instances.all._
import exercises.typeclass.Monoid.syntax._
import exercises.typeclass._
import org.scalacheck.{Arbitrary, Cogen}
import org.scalatest.{FunSuite, Matchers}
import org.typelevel.discipline.scalatest.Discipline
import toimpl.typeclass.TypeclassToImpl

class TypeclassExercisesTest extends TypeclassTest(TypeclassExercises)
class TypeclassAnswersTest extends TypeclassTest(TypeclassAnswers)

class TypeclassTest(impl: TypeclassToImpl) extends FunSuite with Discipline with Matchers with TypeclassTestInstance{
  import impl._

  test("check Double instance"){
    2.0.combine(3.5).combine(mempty[Double]) shouldEqual 5.5
  }

  test("check List instance"){
    List(1,2,3).combine(List(4,5)).combine(mempty[List[Int]]) shouldEqual List(1,2,3,4,5)
  }

  test("check Vector instance"){
    Vector(1,2,3).combine(Vector(4,5)).combine(mempty[Vector[Int]]) shouldEqual Vector(1,2,3,4,5)
  }

  test("check Set instance"){
    Set(1,2,3).combine(Set(3,4,5)).combine(mempty[Set[Int]]) shouldEqual Set(1,2,3,4,5)
  }

  test("check (Int, String) instance"){
    (3, "Hello").combine((5, "World")).combine(mempty[(Int, String)]) shouldEqual ((8, "HelloWorld"))
  }

  test("check (Int, Int) instance"){
    (3, 5).combine((5, 1)).combine(mempty[(Int, Int)]) shouldEqual ((8, 6))
  }

  test("check Option instance"){
    Option(3).combine(Option(4)) shouldEqual Some(7)
    Option(3).combine(None) shouldEqual Some(3)
    Option.empty[Int].combine(Option(4)) shouldEqual Some(4)
  }

  test("check Map instance"){
    Map("abc" -> 3, "xxx" -> 5).combine(Map("xxx" -> 2, "aaa" -> 1)) shouldEqual Map("abc" -> 3, "xxx" -> 7, "aaa" -> 1)
  }

  test("check Unit instance"){
    ().combine(()).combine(mempty[Unit]) shouldEqual (())
  }

  test("sum"){
    sum(List(1,2,3,4)) shouldEqual 10
  }

  test("averageWordLength"){
    averageWordLength(List("a", "ab", "abcd", "abc")) shouldEqual 2.5
  }

  test("isEmpty"){
    isEmpty(0) shouldEqual true
    isEmpty(5) shouldEqual false
    isEmpty[String]("") shouldEqual true
    isEmpty[String]("hello") shouldEqual false
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
    foldMap(List("abc", "a", "abcde"))(_.length) shouldEqual 9
  }

  test("String Monoid with space"){
    stringSpaceMonoid.combine("hello", "world") shouldEqual "hello world"
  }

  checkAll("Int", monoidLaws[Int])
  checkAll("Double", monoidLaws[Double])
  checkAll("Unit", monoidLaws[Unit])
  checkAll("String", monoidLaws[String])
  checkAll("List", monoidLaws[List[Boolean]])
  checkAll("Vector", monoidLaws[Vector[Boolean]])
  checkAll("Set", monoidLaws[Set[Int]])
  checkAll("Tuple2", monoidLaws[(Int, String)])
  checkAll("Option", monoidLaws[Option[Int]])
  checkAll("Map", monoidLaws[Map[Int, String]])

  checkAll("Int", strongMonoidLaws[Int])

  test("splitFold"){
    val xs = 1.to(1000).toList
    splitFold(xs)(_.grouped(100).toList) shouldEqual fold(xs)
  }

  checkAll("Int product", monoidLaws[Int](implicitly, productIntMonoid, implicitly))
  checkAll("Boolean", monoidLaws[Boolean](implicitly, booleanMonoid, implicitly))

  checkAll("Product", monoidLaws[Product])
  checkAll("All", monoidLaws[All])
  checkAll("Endo", monoidLaws[Endo[Int]])

  test("product") {
    product(List(1,2,3,4,5)) shouldEqual 120
  }

  test("forAll") {
    forAll(List(true, true, false, true)) shouldEqual false
    forAll(List(true, true, true, true)) shouldEqual true
  }

  checkAll("NonEmptyList", semigroupLaws[NonEmptyList[Boolean]])

  checkAll("Min", semigroupLaws[Min[Int]])
  test("minOptionList") {
    minOptionList(List(5, 7, 2, -1, 10, 34, 12)) shouldEqual Some(-1)
    minOptionList[Int](Nil) shouldEqual None
  }

  checkAll("First", semigroupLaws[First[Int]])
  test("headOptionList") {
    headOptionList(List(5, 7, 2, -1, 10, 34, 12)) shouldEqual Some(5)
    headOptionList[Int](Nil) shouldEqual None
  }

  checkAll("Dual", semigroupLaws[Dual[Int]])
  test("lastOptionList") {
    lastOptionList(List(5, 7, 2, -1, 10, 34, 12)) shouldEqual Some(12)
    lastOptionList[Int](Nil) shouldEqual None
  }

  test("foldMap Vector"){
    foldMap(Vector("abc", "a", "abcde"))(_.length) shouldEqual 9
    foldMap(Vector.empty[String])(_.length) shouldEqual 0
  }

  test("foldMap Option"){
    foldMap(Option("abc"))(_.length) shouldEqual 3
    foldMap(Option.empty[String])(_.length) shouldEqual 0
  }

  test("foldMap Either"){
    foldMap(Right("abc"))(_.length) shouldEqual 3
    foldMap(Left(2): Either[Int, String])(_.length) shouldEqual 0
  }

  test("foldMap Map"){
    foldMap(Map(1 -> "abc", 2 -> "hello"))(_.length) shouldEqual 8
    foldMap(Map.empty[Int, String])(_.length) shouldEqual 0
  }

  test("isEmptyF"){
    isEmptyF(List(1,2,3)) shouldEqual false
    isEmptyF(Nil) shouldEqual true
  }

  test("size"){
    impl.size(Option("hello")) shouldEqual 1
    impl.size(Option.empty) shouldEqual 0
  }

  test("headOption"){
    headOption(List(1,2,3)) shouldEqual Some(1)
    headOption(Nil) shouldEqual None
  }

  test("lastOption"){
    lastOption(List(1,2,3)) shouldEqual Some(3)
    lastOption(Nil) shouldEqual None
  }

  test("find"){
    find(List(10, 8, 7, 4, 3))(_ % 2 == 1) shouldEqual Some(7)
    find(List(10, 8, 6, 4, 2))(_ % 2 == 1) shouldEqual None
    find(List.empty[Int])(_ % 2 == 1) shouldEqual None
  }

  test("minimumOption"){
    minimumOption(List(5, 6, 2, 8, 0, 1)) shouldEqual Some(0)
    minimumOption(List.empty[Int]) shouldEqual None
  }

  test("foldLeftFromFoldMap"){
    foldLeftFromFoldMap(List(1,2,3,4,5), 0)(_ + _) shouldEqual 15
    foldLeftFromFoldMap(List.empty[Int], 0)(_ + _) shouldEqual 0
  }

  test("foldRightFromFoldMap"){
    foldRightFromFoldMap(List(1,2,3,4,5), 0)(_ + _) shouldEqual 15
    foldRightFromFoldMap(List.empty[Int], 0)(_ + _) shouldEqual 0
  }

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
  implicit def arbNel[A: Arbitrary]: Arbitrary[NonEmptyList[A]] = Arbitrary(
    for {
      h <- Arbitrary.arbitrary[A]
      t <- Arbitrary.arbitrary[List[A]]
    } yield NonEmptyList(h ,t)
  )
  implicit def arbMin[A: Arbitrary]: Arbitrary[Min[A]] = Arbitrary(Arbitrary.arbitrary[A].map(Min(_)))
  implicit def arbFirst[A: Arbitrary]: Arbitrary[First[A]] = Arbitrary(Arbitrary.arbitrary[A].map(First(_)))
  implicit def arbDual[A: Arbitrary]: Arbitrary[Dual[A]] = Arbitrary(Arbitrary.arbitrary[A].map(Dual(_)))
}
