package answers.errorhandling
import answers.errorhandling.domain.Nel

package object either {

  type EitherNel[E, A] = Either[Nel[E], A]

  implicit class EitherSyntax[E, A](self: Either[E, A]) {
    def toEitherNel: EitherNel[E, A] = self.left.map(Nel.one)
  }

  implicit class EitherObjectSyntax(self: Either.type) {
    def left[A](value: A): Either[A, Nothing]  = Left(value)
    def right[A](value: A): Either[Nothing, A] = Right(value)
  }

  implicit class EitherIdSyntax[A](self: A) {
    def asLeft: Either[A, Nothing]  = Left(self)
    def asRight: Either[Nothing, A] = Right(self)
  }

  implicit class ListExtension[A](values: List[A]) {
    def traverse[E, B](transform: A => Either[E, B]): Either[E, List[B]] =
      EitherAnswers2.traverse(values)(transform)

    def parTraverse[E, B](transform: A => EitherNel[E, B]): EitherNel[E, List[B]] =
      EitherAnswers2.parTraverse(values)(transform)
  }

  implicit class TupleEitherNelExtension[E, A, B](value: (EitherNel[E, A], EitherNel[E, B])) {
    def zipAccWith[C](update: (A, B) => C): EitherNel[E, C] =
      EitherAnswers2.zipAccWith(value._1, value._2)(update)
  }

}
