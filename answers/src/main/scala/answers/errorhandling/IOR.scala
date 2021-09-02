package answers.errorhandling

sealed trait IOR[+E, +A] {
  import IOR._

  def flatMap[EE >: E, Next](update: A => IOR[EE, Next]): IOR[EE, Next] =
    this match {
      case Left(es)     => Left(es)
      case Right(value) => update(value)
      case Both(es, value) =>
        update(value) match {
          case Left(es2)         => Left(es ++ es2)
          case Right(value2)     => Both(es, value2)
          case Both(es2, value2) => Both(es ++ es2, value2)
        }
    }

  def map[EE >: E, Next](update: A => Next): IOR[EE, Next] =
    this match {
      case Left(es)        => Left(es)
      case Right(value)    => Right(update(value))
      case Both(es, value) => Both(es, update(value))
    }

  def zipAcc[EE >: E, Other](other: IOR[EE, Other]): IOR[EE, (A, Other)] =
    (this, other) match {
      case (Left(es1), Left(es2))       => Left(es1 ++ es2)
      case (Left(es1), Both(es2, _))    => Left(es1 ++ es2)
      case (Left(es1), Right(_))        => Left(es1)
      case (Both(es1, _), Left(es2))    => Left(es1 ++ es2)
      case (Both(es1, a), Both(es2, b)) => Both(es1 ++ es2, (a, b))
      case (Both(es1, a), Right(b))     => Both(es1, (a, b))
      case (Right(_), Left(es2))        => Left(es2)
      case (Right(a), Both(es2, b))     => Both(es2, (a, b))
      case (Right(a), Right(b))         => Right((a, b))
    }
}

object IOR {
  case class Left[+E](failures: NEL[E])                 extends IOR[E, Nothing]
  case class Right[+A](success: A)                      extends IOR[Nothing, A]
  case class Both[+E, +A](failures: NEL[E], success: A) extends IOR[E, A]

  def fromEither[E, A](either: Either[E, A]): IOR[E, A] =
    either.fold(e => Left(NEL(e)), Right(_))

  def sequence[E, A](values: List[IOR[E, A]]): IOR[E, List[A]] =
    values
      .foldLeft(Right(Nil): IOR[E, List[A]])((state, ior) =>
        state.zipAcc(ior).map { case (list, value) => value :: list }
      )
      .map(_.reverse)

  def traverse[E, A, B](values: List[A])(transform: A => IOR[E, B]): IOR[E, List[B]] =
    sequence(values.map(transform))
}
