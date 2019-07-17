package exercises.sideeffect

import scala.util.Try

case class IO[+A](unsafeRun: () => Either[Throwable, A])

object IO {
  // strict, successful value
  def succeed[A](value: A): IO[A] =
    fromEither(Right(value))

  // strict failure
  def failed(value: Throwable): IO[Nothing] =
    fromEither(Left(value))

  def fromTry[A](value: Try[A]): IO[A] =
    fromEither(value.toEither)

  def fromEither[A](value: Either[Throwable, A]): IO[A] =
    IO(() => value)

  // lazily import effectful code
  def effect[A](value: => A): IO[A] =
    IO(() => Try(value).toEither)
}
