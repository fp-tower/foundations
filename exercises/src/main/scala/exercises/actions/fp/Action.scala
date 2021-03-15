package exercises.actions.fp

trait Action[A] {
  def execute(): A

  def andThen[Next](callBack: A => Action[Next]): Action[Next] =
    ???

  def retry(maxAttempt: Int): Action[A] =
    ???
}

object Action {
  def apply[A](block: => A): Action[A] =
    new Action[A] {
      def execute(): A = block
    }
}
