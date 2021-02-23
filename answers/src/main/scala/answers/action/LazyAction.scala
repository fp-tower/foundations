package answers.action

import scala.util.Random

object LazyAction {
  def delay[A](block: => A): LazyAction[A] =
    () => block

  def execute[A](action: LazyAction[A]): A =
    action()

  def log(message: String): LazyAction[Unit] =
    delay {
      println(message)
    }

  def randomInt: LazyAction[Int] =
    delay {
      Random.nextInt()
    }

  def pipe[A, B](first: LazyAction[A])(andThen: A => LazyAction[B]): LazyAction[B] =
    delay {
      andThen(first.execute()).execute()
    }
}

object ActionV2App extends App {
  import LazyAction._

  execute(log("Hello"))

  log("Hello").execute()

  pipe(randomInt)(n => log(s"Random number is ${n}")).execute()
}
