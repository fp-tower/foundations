package types

import org.scalacheck.Arbitrary
import org.scalacheck.Prop._
import org.typelevel.discipline.Laws

object IsoLaws extends Laws {

  def apply[A: Arbitrary , B: Arbitrary](to: A => B, from: B => A): RuleSet = {
    new SimpleRuleSet("Iso",
    "round trip one way" -> forAll((a: A) => from(to(a)) == a),
    "round trip other way" -> forAll((b: B) => to(from(b)) == b)
    )
  }
}