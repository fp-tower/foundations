package exercises.functors

import exercises.functors.Applicative.syntax._
import exercises.functors.Functor.syntax._
import exercises.functors.Monad.syntax._
import exercises.typeclass.Eq
import exercises.typeclass.Eq.syntax._
import org.scalacheck.Arbitrary
import org.scalacheck.Prop.forAll
import org.typelevel.discipline.Laws

object FLaws extends Laws {

  def functor[F[_]: Functor, A](
    implicit arbFa: Arbitrary[F[A]],
             arbA2B: Arbitrary[A => A],
             eqFa: Eq[F[A]]
  ): RuleSet = functor[F, A, A, A]

  def functor[F[_]: Functor, A, B, C](
    implicit arbFa: Arbitrary[F[A]],
             arbA2B: Arbitrary[A => B],
             arbB2C: Arbitrary[B => C],
             eqFa: Eq[F[A]],
             eqFc: Eq[F[C]]
  ): RuleSet =
    new SimpleRuleSet("Functor",
      "identity" ->
        forAll((fa: F[A]) => fa.map(identity) === fa),
        "compose" ->
          forAll((fa: F[A], f: A => B, g: B => C) => fa.map(f).map(g) === fa.map(f andThen g)),
    )

  def applicative[F[_]: Applicative, A](
     implicit arbFa: Arbitrary[F[A]],
              arbA2B: Arbitrary[A => A],
              arbA: Arbitrary[A],
              eqFa: Eq[F[A]],
              eqFaa: Eq[F[(A, A)]]
  ): RuleSet = applicative[F, A, A, A]

  def applicative[F[_]: Applicative, A, B, C](
    implicit arbFa: Arbitrary[F[A]],
             arbA2B: Arbitrary[A => B],
             arbB2C: Arbitrary[B => C],
             arbA: Arbitrary[A],
             arbB: Arbitrary[B],
             eqFa: Eq[F[A]],
             eqFc: Eq[F[C]],
             eqFab: Eq[F[(A, B)]]
  ): RuleSet =
    new DefaultRuleSet("Applicative", Some(functor[F, A, B, C]),
      "pure left" ->
        forAll((fa: F[A], b: B) => (b.pure[F] *> fa) === fa),
      "pure right" ->
        forAll((fa: F[A], b: B) => (fa <* b.pure[F]) === fa),
      "tuple2" ->
        forAll((a: A, b: B) => a.pure[F].tuple2(b.pure[F]) === (a,b).pure[F] )
    )

  def monad[F[_]: Monad, A](
    implicit arbFa: Arbitrary[F[A]],
             arbA2B: Arbitrary[A => A],
             arbA: Arbitrary[A],
             eqFa: Eq[F[A]],
             eqFaa: Eq[F[(A, A)]]
  ): RuleSet = monad[F, A, A, A]

  def monad[F[_]: Monad, A, B, C](
    implicit arbFa: Arbitrary[F[A]],
             arbA2B: Arbitrary[A => B],
             arbB2C: Arbitrary[B => C],
             arbA: Arbitrary[A],
             arbB: Arbitrary[B],
             eqFa: Eq[F[A]],
             eqFc: Eq[F[C]],
             eqFab: Eq[F[(A, B)]]
  ): RuleSet =
    new DefaultRuleSet("Monad", Some(applicative[F, A, B, C]),
      "flatMap - pure" -> forAll((fa: F[A]) => fa.flatMap(_.pure[F]) === fa)
    )

}
