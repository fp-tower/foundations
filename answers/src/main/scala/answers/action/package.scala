package answers

package object action {
  type LazyValue[A] = () => A

  type Action = () => Unit
}
