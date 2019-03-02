package types

import exercises.types.Iso
import org.scalacheck.Arbitrary
import org.scalacheck.Prop._
import org.typelevel.discipline.Laws

object IsoLaws extends Laws {

  def apply[A: Arbitrary , B: Arbitrary](iso: Iso[A, B]): RuleSet = {
    import iso._

    new SimpleRuleSet("Iso",
    "round trip one way" -> forAll((a: A) => to(from(a)) == a),
    "round trip other way" -> forAll((b: B) => from(to(b)) == b)
    )
  }
}