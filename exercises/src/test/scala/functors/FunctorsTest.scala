package functors

import answers.functors.FunctorsAnswers
import cats.instances.all._
import cats.kernel.Eq
import exercises.typeclass.Monoid
import exercises.functors.{Const, FLaws, FunctorsExercises, Id}
import org.scalacheck.Arbitrary
import org.scalatest.{FunSuite, Matchers}
import org.typelevel.discipline.scalatest.Discipline
import toimpl.functors.FunctorsToImpl

class FunctorsExercisesTest extends FunctorsTest(FunctorsExercises)
class FunctorsAnswersTest extends FunctorsTest(FunctorsAnswers)

class FunctorsTest(impl: FunctorsToImpl) extends FunSuite with Discipline with Matchers with FunctorsTestInstance {
  import impl._

  checkAll("List", FLaws.monad[List, Int])
  checkAll("Option", FLaws.monad[Option, Int])
  checkAll("Either", FLaws.monad[Either[Boolean, ?], Int])
  checkAll("Id", FLaws.monad[Id, Int])
  checkAll("Const", FLaws.applicative[Const[Int, ?], Boolean])
  checkAll("Function", FLaws.monad[Int => ?, Boolean])

//  test("void"){
//    void(List(1,2,3)) shouldEqual List((),(),())
//  }

}

trait FunctorsTestInstance {
  implicit def arbId[A: Arbitrary]: Arbitrary[Id[A]] = Arbitrary(Arbitrary.arbitrary[A].map(Id(_)))
  implicit def arbConst[A: Arbitrary, B]: Arbitrary[Const[A, B]] = Arbitrary(Arbitrary.arbitrary[A].map(Const(_).as[B]))

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