package function

import exercises.typeclass.Eq
import exercises.typeclass.Eq.syntax._
import org.scalacheck.Arbitrary
import org.scalacheck.Prop._
import org.typelevel.discipline.Laws

object PureFunctionLaws extends Laws {

  def apply[A: Arbitrary , B: Eq](f: A => B): RuleSet = {
    new SimpleRuleSet("Function",
    "deterministic & total" -> forAll((a: A) => f(a) === f(a))
    )
  }

}