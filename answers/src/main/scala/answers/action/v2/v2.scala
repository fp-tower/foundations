package answers.action

package object v2 {
  type LazyAction[A] = () => A

  implicit class LazyActionSyntax[A](self: LazyAction[A]) {
    def execute(): A = self()
  }
}
