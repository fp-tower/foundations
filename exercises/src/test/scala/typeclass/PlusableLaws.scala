package typeclass

import exercises.typeclass.{Plusable, StrongPlusable}
import org.scalacheck.Arbitrary
import org.scalacheck.Prop._
import org.typelevel.discipline.Laws

object PlusableLaws extends Laws {

  def apply[A: Arbitrary: Plusable]: RuleSet = {
    val p = Plusable[A]

    new SimpleRuleSet("Plusable",
    "example" -> forAll((a: A) => a == a),
    "fail" -> forAll((a: A) => ???),
    )
  }

  def strong[A: Arbitrary: StrongPlusable]: RuleSet = {
    val p = StrongPlusable[A]

    new DefaultRuleSet("StrongPlusable", Some(apply[A]),
      "additional law" -> forAll((a: A) => ???)
    )
  }

}