package function

import org.scalacheck.Arbitrary
import org.scalacheck.Prop._
import org.typelevel.discipline.Laws

object PureFunctionLaws extends Laws {

  def apply[A: Arbitrary , B](f: A => B): RuleSet = {
    new SimpleRuleSet("Function",
    "deterministic & total" -> forAll((a: A) => f(a) == f(a))
    )
  }

}