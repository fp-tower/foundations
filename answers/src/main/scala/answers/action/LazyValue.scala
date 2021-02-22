package answers.action

object LazyValue {
  def apply[A](block: => A): LazyValue[A] =
    () => block
}
