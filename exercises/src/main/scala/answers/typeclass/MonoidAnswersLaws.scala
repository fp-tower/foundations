package answers.typeclass

import cats.kernel.Eq
import cats.syntax.eq._
import exercises.typeclass.Monoid.syntax._
import exercises.typeclass.{Monoid, StrongMonoid}
import org.scalacheck.Arbitrary
import org.scalacheck.Prop.forAll
import toimpl.typeclass.MonoidLawsToImpl

object MonoidAnswersLaws extends MonoidLawsToImpl {

  def apply[A: Arbitrary: Monoid: Eq]: RuleSet =
    new SimpleRuleSet("Monoid",
      "associative" ->
        forAll((x: A, y: A, z: A) => ((x |+| y) |+| z) === (x |+| (y |+| z))),
      "left identity" ->
        forAll((a: A) => (mempty[A] |+| a) === a),
      "right identity" ->
        forAll((a: A) => (a |+| mempty[A]) === a)
    )

  def strong[A: Arbitrary: StrongMonoid: Eq]: RuleSet =
    new DefaultRuleSet("StrongMonoid", Some(apply[A]),
      "commutative" ->
        forAll((x: A, y: A) => (x |+| y) === (y |+| x))
    )

}