package answers.errorhandling

import answers.sideeffect.IOAnswers.IO

case class OptionT[+A](value: IO[Option[A]]) {
  def map[B](f: A => B): OptionT[B] =
    OptionT(value.map(_.map(f)))

  def flatMap[B](f: A => OptionT[B]): OptionT[B] =
    OptionT(value.flatMap {
      case None    => IO.succeed(None)
      case Some(a) => f(a).value
    })
}

case class OptionT2[F[+ _], +A](value: IO[F[A]])
