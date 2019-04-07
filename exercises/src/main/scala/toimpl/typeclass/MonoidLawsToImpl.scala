package toimpl.typeclass

import cats.kernel.Eq
import exercises.typeclass.{Monoid, StrongMonoid}
import org.scalacheck.Arbitrary
import org.typelevel.discipline.Laws

trait MonoidLawsToImpl extends Laws {
  def apply[A: Arbitrary: Monoid: Eq]: RuleSet
  def strong[A: Arbitrary: StrongMonoid : Eq]: RuleSet
}
