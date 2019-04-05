package typeclass

import exercises.typeclass.{Monoid, StrongMonoid}
import org.scalacheck.Arbitrary
import org.scalacheck.Prop._
import org.typelevel.discipline.Laws

object MonoidLaws extends Laws {

  def apply[A: Arbitrary: Monoid]: RuleSet = {
    val p = Monoid[A]

    new SimpleRuleSet("Monoid",
    "example" -> forAll((a: A) => a == a),
    "fail" -> forAll((a: A) => ???),
    )
  }

  def strong[A: Arbitrary: StrongMonoid]: RuleSet = {
    val p = StrongMonoid[A]

    new DefaultRuleSet("StrongMonoid", Some(apply[A]),
      "additional law" -> forAll((a: A) => ???)
    )
  }

}