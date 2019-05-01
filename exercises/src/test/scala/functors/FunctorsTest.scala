package functors

import answers.functors.FunctorsAnswers
import cats.instances.all._
import cats.kernel.Eq
import exercises.functors._
import exercises.typeclass.Monoid
import org.scalacheck.Arbitrary
import org.scalatest.{FunSuite, Matchers}
import org.typelevel.discipline.scalatest.Discipline
import toimpl.functors.FunctorsToImpl

class FunctorsExercisesTest extends FunctorsTest(FunctorsExercises)
class FunctorsAnswersTest extends FunctorsTest(FunctorsAnswers)

class FunctorsTest(impl: FunctorsToImpl) extends FunSuite with Discipline with Matchers with FunctorsTestInstance {
  import impl._

  ////////////////////////
  // 1. Functor
  ////////////////////////

  checkAll("List"    , FLaws.functor[List, Int])
  checkAll("Option"  , FLaws.functor[Option, Int])
  checkAll("Either"  , FLaws.functor[Either[Boolean, ?], Int])
  checkAll("Map"     , FLaws.functor[Map[Int, ?], Int])
  checkAll("Id"      , FLaws.functor[Id, Int])
  checkAll("Const"   , FLaws.functor[Const[Int, ?], Boolean])
  checkAll("Function", FLaws.functor[Int => ?, Boolean])

  test("void"){
    void(List(1,2,3)) shouldEqual List((),(),())
  }

  test("as"){
    as(List(1,2,3))(0) shouldEqual List(0,0,0)
  }

  test("widen"){
    sealed trait Shape
    case class Circle(radius: Double) extends Shape
    case class Rectangle(width: Double, height: Double) extends Shape

    val circles: List[Circle] = List(Circle(1.2), Circle(5))
    val shapes : List[Shape]  = List(Circle(1.2), Circle(5))

    widen[List, Circle, Shape](circles) shouldEqual shapes
  }

  test("tupleLeft"){
    tupleLeft(Option(4))("hello")    shouldEqual Some(("hello", 4))
    tupleLeft(Option.empty)("hello") shouldEqual None
  }

  test("tupleRight"){
    tupleRight(Option(4))("hello")    shouldEqual Some((4, "hello"))
    tupleRight(Option.empty)("hello") shouldEqual None
  }

  // Not sure why this instance is ambiguous
  checkAll("Compose", FLaws.functor[Compose[List, Option, ?], Boolean](composeFunctor[List, Option], implicitly, implicitly, implicitly))

  ////////////////////////
  // 2. Applicative
  ////////////////////////

  checkAll("List"    , FLaws.applicative[List, Int])
  checkAll("Option"  , FLaws.applicative[Option, Int])
  checkAll("Either"  , FLaws.applicative[Either[Boolean, ?], Int])
  checkAll("Id"      , FLaws.applicative[Id, Int])
  checkAll("Const"   , FLaws.applicative[Const[Int, ?], Boolean])
  checkAll("Function", FLaws.applicative[Int => ?, Boolean])

  test("Apply for Map"){
    Apply[Map[Int, ?]].tuple2(
      Map(1 -> "One", 2 -> "Two", 10 -> "Ten"),
      Map(1 -> 1.0  , 3 -> 3.0  , 10 -> 10.0),
    ) shouldEqual Map(1 -> (("One", 1.0)), 10 -> (("Ten", 10.0)))
  }

  test("map3"){
    map3(Option(1), Option(2), Option(3))(_ + _ + _) shouldEqual Some(6)
    map3(Option(1), None     , Option(3))(_ + _ + _) shouldEqual None
  }

  test("tuple2"){
    tuple2(List(1,2,3), List('a', 'b')) shouldEqual List((1, 'a'), (1, 'b'), (2, 'a'), (2, 'b'), (3, 'a'), (3, 'b'))
  }

  test("productL"){
    productL(Option(1), Option("hello")) shouldEqual Some(1)
    productL(None     , Option("hello")) shouldEqual None
    productL(Option(1), None           ) shouldEqual None
  }

  test("productR"){
    productR(Option(1), Option("hello")) shouldEqual Some("hello")
    productR(None     , Option("hello")) shouldEqual None
    productR(Option(1), None           ) shouldEqual None
  }

  test("unit"){
    unit[Either[String, ?]] shouldEqual Right(())
    unit[List] shouldEqual List(())
  }

  test("map2 for ZipList"){
    Apply[ZipList].tuple2(
      ZipList(1,2,3),
      ZipList("hello", "world"),
    ) shouldEqual ZipList((1, "hello"), (2, "world"))
  }

  checkAll("Compose", FLaws.applicative[Compose[List, Option, ?], Boolean])

  ////////////////////////
  // 3. Monad
  ////////////////////////

  checkAll("List"    , FLaws.monad[List, Int])
  checkAll("Option"  , FLaws.monad[Option, Int])
  checkAll("Either"  , FLaws.monad[Either[Boolean, ?], Int])
  checkAll("Id"      , FLaws.monad[Id, Int])
  checkAll("Function", FLaws.monad[Int => ?, Boolean])

  test("flatten"){
    flatten(List(List(1,2), List(3,4,5))) shouldEqual List(1,2,3,4,5)
    flatten((x: Int) => (y: Int) => x + y).apply(4) shouldEqual 8
  }

  test("flatTap"){
    flatTap(Option(10))(x => if(x > 0) unit[Option] else None) shouldEqual Some(10)
    flatTap(Option(-5))(x => if(x > 0) unit[Option] else None) shouldEqual None
  }

  test("ifM"){
    val func = ifM((x: Int) => x > 0)(_ * 2, _.abs)

    func(-10) shouldEqual 10
    func(  3) shouldEqual 6
  }

  ////////////////////////
  // 4. Traverse
  ////////////////////////

  test("parseNumber"){
    parseNumber("1052")  shouldEqual Some(BigInt(1052))
    parseNumber("hello") shouldEqual None
  }

}

trait FunctorsTestInstance {
  implicit def arbId[A: Arbitrary]: Arbitrary[Id[A]] = Arbitrary(Arbitrary.arbitrary[A].map(Id(_)))
  implicit def arbConst[A: Arbitrary, B]: Arbitrary[Const[A, B]] = Arbitrary(Arbitrary.arbitrary[A].map(Const(_).as[B]))
  implicit def composeArbitrary[F[_], G[_], A](implicit ev: Arbitrary[F[G[A]]]): Arbitrary[Compose[F, G, A]] =
    Arbitrary(ev.arbitrary.map(Compose(_)))

  implicit val monoidInt: Monoid[Int] = new Monoid[Int] {
    def combine(x: Int, y: Int): Int = x + y
    def empty: Int = 0
  }

  implicit def eqFunction[A: Arbitrary, B: Eq]: Eq[A => B] =
    new Eq[A => B] {
      def eqv(x: A => B, y: A => B): Boolean = {
        val samples = List.fill(50)(Arbitrary.arbitrary[A].sample).collect {
          case Some(a) => a
          case None    => sys.error("Could not generate arbitrary values to compare two functions")
        }
        samples.forall(a => Eq[B].eqv(x(a), y(a)))
      }
    }
}