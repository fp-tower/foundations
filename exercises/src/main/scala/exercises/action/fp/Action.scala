package exercises.action.fp

trait Action[A] {
  def execute(): A

  def andThen[Next](callBack: A => Action[Next]): Action[Next] =
    Action {
      val result     = this.execute()
      val nextAction = callBack(result)
      nextAction.execute()
    }

  def retry(maxAttempt: Int): Action[A] =
    ???

  def onError[Other](callback: Throwable => Action[Other]): Action[A] =
    ???
}

object Action {
  def apply[A](block: => A): Action[A] =
    new Action[A] {
      def execute(): A = block
    }
}
