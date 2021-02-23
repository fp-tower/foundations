package answers.action.v2

import scala.util.Random

object Action {
  def log(message: String): Action[Unit] =
    () => println(message)

  val randomInt: Action[Int] =
    () => Random.nextInt()

  def execute[A](action: Action[A]): A =
    action()

  def combine2[A, B](action1: Action[A], action2: Action[B]): Action[(A, B)] =
    () => {
      (execute(action1), execute(action2))
    }

  def pipe[A, B](action1: Action[A])(andThen: A => Action[B]): Action[B] =
    () => execute(andThen(execute(action1)))
}

object ActionV2App extends App {
  import Action._

  val hello = log("Hello") // nothing happen

  execute(hello)

  val program = pipe(combine2(randomInt, randomInt))(tuple => log(tuple.toString))

  execute(program)

}
