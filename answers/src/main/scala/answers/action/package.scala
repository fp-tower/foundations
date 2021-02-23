package answers

package object action {
  type LazyAction[A] = () => A

  implicit class LazyActionSyntax[A](self: LazyAction[A]) {
    def execute(): A = self()
  }
}
