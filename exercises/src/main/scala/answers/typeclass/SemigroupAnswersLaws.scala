package answers.typeclass

import cats.kernel.Eq
import cats.syntax.eq._
import exercises.typeclass.Semigroup
import exercises.typeclass.Semigroup.syntax._
import org.scalacheck.Arbitrary
import org.scalacheck.Prop.forAll
import toimpl.typeclass.SemigroupLawsToImpl

object SemigroupAnswersLaws extends SemigroupLawsToImpl {

  def apply[A: Arbitrary : Semigroup : Eq]: RuleSet =
    new SimpleRuleSet("Semigroup",
      "associative" ->
        forAll((x: A, y: A, z: A) => ((x |+| y) |+| z) === (x |+| (y |+| z)))
    )

}