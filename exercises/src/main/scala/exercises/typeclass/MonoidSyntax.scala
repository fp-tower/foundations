package exercises.typeclass

trait MonoidSyntax {

  implicit class PlusableOps[A](self: A)(implicit ev: Monoid[A]){
    def plus(other: A): A = ev.plus(self, other)
  }

  def zero[A](implicit ev: Monoid[A]): A = ev.zero

}
