package toimpl.typeclass

import exercises.typeclass.{Monoid, StrongMonoid}
import org.scalacheck.Arbitrary
import org.typelevel.discipline.Laws

trait MonoidLawsToImpl extends Laws {
  def apply[A: Arbitrary: Monoid]: RuleSet
  def strong[A: Arbitrary: StrongMonoid]: RuleSet
}
