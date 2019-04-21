package exercises.functors

import cats.kernel.Eq
import cats.syntax.eq._
import exercises.functors.Functor.syntax._
import exercises.functors.Applicative.syntax._
import exercises.functors.Monad.syntax._
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
     arbA: Arbitrary[A],
     arbA2A: Arbitrary[A => A],
     arbFA2A: Arbitrary[F[A => A]],
     eqFa: Eq[F[A]]
  ): RuleSet = applicative[F, A, A, A]

  def applicative[F[_]: Applicative, A, B, C](
    implicit arbFa: Arbitrary[F[A]],
    arbA: Arbitrary[A],
    arbA2B: Arbitrary[A => B],
    arbB2C: Arbitrary[B => C],
    arbFA2B: Arbitrary[F[A => B]],
    eqFa: Eq[F[A]],
    eqFB: Eq[F[B]],
    eqFc: Eq[F[C]]
  ): RuleSet =
    new DefaultRuleSet("Applicative", Some(functor[F, A, B, C]),
      "identity" ->
        forAll((fa: F[A]) => (identity[A] _).pure[F].map2(fa)((f, a) => f(a)) === fa),
        "homomorphism" ->
          forAll((a: A, f: A => B) => f.pure[F].map2(a.pure[F])(_(_)) === f(a).pure[F]),
      "interchange" ->
        forAll((a: A, ff: F[A => B]) => ff.map2(a.pure[F])(_(_)) === ((f: A => B) => f(a)).pure[F].map2(ff)(_(_))),
    )

  def monad[F[_]: Monad, A](
    implicit arbFa: Arbitrary[F[A]],
    arbA: Arbitrary[A],
    arbA2A: Arbitrary[A => A],
    arbFA2A: Arbitrary[F[A => A]],
    eqFa: Eq[F[A]]
  ): RuleSet = monad[F, A, A, A]

  def monad[F[_]: Monad, A, B, C](
    implicit arbFa: Arbitrary[F[A]],
    arbA: Arbitrary[A],
    arbA2B: Arbitrary[A => B],
    arbB2C: Arbitrary[B => C],
    arbFA2B: Arbitrary[F[A => B]],
    eqFa: Eq[F[A]],
    eqFB: Eq[F[B]],
    eqFc: Eq[F[C]]
  ): RuleSet =
    new DefaultRuleSet("Monad", Some(applicative[F, A, B, C]),
      "flatMap - pure" -> forAll((fa: F[A]) => fa.flatMap(_.pure[F]) === fa)
    )

}
