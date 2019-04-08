package toimpl.typeclass

import cats.kernel.Eq
import exercises.typeclass.Semigroup
import org.scalacheck.Arbitrary
import org.typelevel.discipline.Laws

trait SemigroupLawsToImpl extends Laws {
  def apply[A: Arbitrary: Semigroup: Eq]: RuleSet
}
