package answers.action.v1

import scala.io.Source
import scala.util.Random

object LazyAction {
  def delay(block: => Any): LazyAction =
    () => block

  def log(message: String): LazyAction =
    delay {
      println(message)
    }

  val randomInt: LazyAction =
    delay {
      println(Random.nextInt())
    }

  def fetch(url: String): LazyAction =
    delay {
      Source.fromURL(url)("ISO-8859-1").getLines().foreach(println)
    }

  def fetchGithubOrganisations(username: String): LazyAction =
    fetch(s"https://api.github.com/users/$username/orgs")

  def execute(action: LazyAction): Unit =
    action()

  def combine2(action1: LazyAction, action2: LazyAction): LazyAction =
    () => {
      execute(action1)
      execute(action2)
    }
}

object ActionV1App extends App {
  import LazyAction._

  val hello = log("Hello") // nothing happen

  execute(hello)

  val greeting = combine2(
    log("Hey"),
    log("How are you?")
  )

  execute(greeting)

  execute(randomInt)

  val github = fetchGithubOrganisations("julien-truffaut")

  execute(github)

}
