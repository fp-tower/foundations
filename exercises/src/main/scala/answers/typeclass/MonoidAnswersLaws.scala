package answers.typeclass

import exercises.typeclass.{Monoid, StrongMonoid}
import org.scalacheck.Arbitrary
import org.scalacheck.Prop.forAll
import toimpl.typeclass.MonoidLawsToImpl
import Monoid.syntax._

object MonoidAnswersLaws extends MonoidLawsToImpl {

  def apply[A: Arbitrary: Monoid]: RuleSet = {
    new SimpleRuleSet("Monoid",
      "associative" ->
        forAll((x: A, y: A, z: A) => ((x |+| y) |+| z) == (x |+| (y |+| z))),
      "left identity" ->
        forAll((a: A) => (mempty[A] |+| a) == a),
      "right identity" ->
        forAll((a: A) => (a |+| mempty[A]) == a)
    )
  }

  def strong[A: Arbitrary: StrongMonoid]: RuleSet = {
    new DefaultRuleSet("StrongMonoid", Some(apply[A]),
      "commutative" ->
        forAll((x: A, y: A) => (x |+| y) == (y |+| x))
    )
  }

}