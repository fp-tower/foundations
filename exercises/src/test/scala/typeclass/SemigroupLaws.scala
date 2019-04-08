package typeclass

import cats.kernel.Eq
import cats.syntax.eq._
import exercises.typeclass.Semigroup
import org.scalacheck.Arbitrary
import org.scalacheck.Prop._
import toimpl.typeclass.SemigroupLawsToImpl

object SemigroupLaws extends SemigroupLawsToImpl {

  def apply[A: Arbitrary: Semigroup: Eq]: RuleSet = {
    val p = Semigroup[A]

    new SimpleRuleSet("Semigroup",
    "example" -> forAll((a: A) => a === a),
    "fail" -> forAll((a: A) => ???),
    )
  }

}