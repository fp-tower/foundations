package exercises.sideeffect

import answers.sideeffect.IOAnswers._

sealed trait FreeMap[F[_], A] {
  def map[B](f: A => B): FreeMap[F, B]

  def mapK[G[_]](nat: NaturalTransformation[F, G]): FreeMap[G, A] =
    this match {
      case FreeMap.Map(fa, f) =>
        FreeMap.Map(nat.apply(fa), f)
    }
}

object FreeMap {
  case class Map[F[_], X, A](value: F[X], update: X => A) extends FreeMap[F, A] {
    def map[B](f: A => B): FreeMap[F, B] =
      Map(value, f compose update)
  }

  def lift[F[_], A](fa: F[A]): FreeMap[F, A] =
    Map[F, A, A](fa, identity)

  def compileIO[A](fa: FreeMap[IO, A]): IO[A] =
    fa match {
      case Map(io, f) => io.map(f)
    }
}
