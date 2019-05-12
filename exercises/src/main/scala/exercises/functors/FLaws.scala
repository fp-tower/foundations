package exercises.functors

import exercises.functors.Applicative.syntax._
import exercises.functors.Functor.syntax._
import exercises.functors.Monad.syntax._
import exercises.functors.Traverse.syntax._
import exercises.typeclass.Eq.syntax._
import exercises.typeclass.Foldable.syntax._
import exercises.typeclass.{Eq, Monoid}
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
    new SimpleRuleSet(
      "Functor",
      "identity" ->
        forAll((fa: F[A]) => fa.map(identity) === fa),
      "compose" ->
        forAll((fa: F[A], f: A => B, g: B => C) => fa.map(f).map(g) === fa.map(f andThen g))
    )

  def applicative[F[_]: Applicative, A](
    implicit arbFa: Arbitrary[F[A]],
    arbA2B: Arbitrary[A => A],
    eqFa: Eq[F[A]],
    eqFaaa: Eq[F[((A, A), A)]]
  ): RuleSet = applicative[F, A, A, A]

  def applicative[F[_]: Applicative, A, B, C](
    implicit arbFa: Arbitrary[F[A]],
    arbFb: Arbitrary[F[B]],
    arbFc: Arbitrary[F[C]],
    arbA2B: Arbitrary[A => B],
    arbB2C: Arbitrary[B => C],
    eqFa: Eq[F[A]],
    eqFB: Eq[F[B]],
    eqFc: Eq[F[C]],
    eqFabc: Eq[F[((A, B), C)]]
  ): RuleSet =
    new DefaultRuleSet(
      "Applicative",
      Some(functor[F, A, B, C]),
      "left identity"  -> forAll((fa: F[A]) => (unit[F] *> fa) === fa),
      "right identity" -> forAll((fa: F[A]) => (fa <* unit[F]) === fa),
      "associativity" ->
        forAll(
          (fa: F[A], fb: F[B], fc: F[C]) =>
            fa.tuple2(fb).tuple2(fc) === fa.tuple2(fb.tuple2(fc)).map { case (a, (b, c)) => ((a, b), c) }
        ),
      "map coherence" -> forAll((fa: F[A], f: A => B) => fa.map2(unit[F])((a, _) => f(a)) === fa.map(f))
    )

  def monad[F[_]: Monad, A: Arbitrary](
    implicit arbFa: Arbitrary[F[A]],
    arbA2A: Arbitrary[A => A],
    arbA2FA: Arbitrary[A => F[A]],
    eqFa: Eq[F[A]],
    eqFaa: Eq[F[(A, A)]],
    eqFaaa: Eq[F[((A, A), A)]]
  ): RuleSet = monad[F, A, A, A]

  def monad[F[_]: Monad, A: Arbitrary, B, C](
    implicit arbFa: Arbitrary[F[A]],
    arbFb: Arbitrary[F[B]],
    arbFc: Arbitrary[F[C]],
    arbA2B: Arbitrary[A => B],
    arbA2FB: Arbitrary[A => F[B]],
    arbB2C: Arbitrary[B => C],
    eqFa: Eq[F[A]],
    eqFB: Eq[F[B]],
    eqFc: Eq[F[C]],
    eqFab: Eq[F[(A, B)]],
    eqFabc: Eq[F[((A, B), C)]]
  ): RuleSet =
    new DefaultRuleSet(
      "Monad",
      Some(applicative[F, A, B, C]),
      "left identity"    -> forAll((a: A, f: A => F[B]) => a.pure[F].flatMap(f) === f(a)),
      "right identity"   -> forAll((fa: F[A]) => fa.flatMap(_.pure[F]) === fa),
      "map coherence"    -> forAll((fa: F[A], f: A => B) => fa.flatMap(f(_).pure[F]) === fa.map(f)),
      "tuple2 coherence" -> forAll((fa: F[A], fb: F[B]) => fa.flatMap(fb.tupleLeft) === fa.tuple2(fb))
    )

  def traverse[F[_]: Traverse, A: Monoid: Eq](
    implicit arbFa: Arbitrary[F[A]],
    arbA2B: Arbitrary[A => A],
    eqFa: Eq[F[A]]
  ): RuleSet = traverse[F, A, A, A, A]

  def traverse[F[_]: Traverse, A, B, C, M: Monoid: Eq](
    implicit arbFa: Arbitrary[F[A]],
    arbA2B: Arbitrary[A => B],
    arbB2C: Arbitrary[B => C],
    arbA2M: Arbitrary[A => M],
    eqFa: Eq[F[A]],
    eqFB: Eq[F[B]],
    eqFc: Eq[F[C]]
  ): RuleSet = {
    import answers.functors.FunctorsAnswers.{constApplicative, idApplicative}

    new DefaultRuleSet(
      "Traverse",
      Some(functor[F, A, B, C]),
      "identity"      -> forAll((fa: F[A]) => fa.traverse(Id(_)).value === fa),
      "map coherence" -> forAll((fa: F[A], f: A => B) => fa.traverse(a => Id(f(a))).value === fa.map(f)),
      "foldMap coherence" -> forAll(
        (fa: F[A], f: A => M) => fa.traverse(a => Const[M, A](f(a))).getConst === fa.foldMap(f)
      )
    )
  }

}
