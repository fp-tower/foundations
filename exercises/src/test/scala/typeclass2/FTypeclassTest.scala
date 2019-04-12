package typeclass2

import answers.typeclass2.FTypeclassAnswers
import cats.instances.all._
import exercises.typeclass.Monoid
import exercises.typeclass2.{Const, FLaws, FTypeclassExercises, Id}
import org.scalacheck.Arbitrary
import org.scalatest.{FunSuite, Matchers}
import org.typelevel.discipline.scalatest.Discipline
import toimpl.typeclass2.FTypeclassToImpl

class FTypeclassExercisesTest extends FTypeclassTest(FTypeclassExercises)
class FTypeclassAnswersTest extends FTypeclassTest(FTypeclassAnswers)

class FTypeclassTest(impl: FTypeclassToImpl) extends FunSuite with Discipline with Matchers with FTypeclassTestInstance {
  import impl._

  checkAll("List", FLaws.monad[List, Int])
  checkAll("Option", FLaws.monad[Option, Int])
  checkAll("Either", FLaws.monad[Either[Boolean, ?], Int])
  checkAll("Id", FLaws.monad[Id, Int])
  checkAll("Const", FLaws.applicative[Const[Int, ?], Boolean])

//  test("void"){
//    void(List(1,2,3)) shouldEqual List((),(),())
//  }
  
}

trait FTypeclassTestInstance {
  implicit def arbId[A: Arbitrary]: Arbitrary[Id[A]] = Arbitrary(Arbitrary.arbitrary[A].map(Id(_)))
  implicit def arbConst[A: Arbitrary, B]: Arbitrary[Const[A, B]] = Arbitrary(Arbitrary.arbitrary[A].map(Const(_).as[B]))

  implicit val monoidInt: Monoid[Int] = new Monoid[Int] {
    def combine(x: Int, y: Int): Int = x + y
    def empty: Int = 0
  }
}