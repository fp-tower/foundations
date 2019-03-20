package exercises.typeclass

trait PlusableSyntax {

  implicit class PlusableOps[A](self: A)(implicit ev: Plusable[A]){
    def plus(other: A): A = ev.plus(self, other)
  }

  def zero[A](implicit ev: Plusable[A]): A = ev.zero

}
