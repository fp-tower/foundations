package answers.action.v1

import scala.io.Source
import scala.util.Random

object Action {
  def log(message: String): Action =
    () => println(message)

  val randomInt: Action =
    () => println(Random.nextInt())

  def fetch(url: String): Action =
    () => Source.fromURL(url)("ISO-8859-1").getLines().foreach(println)

  def fetchGithubOrganisations(username: String): Action =
    fetch(s"https://api.github.com/users/$username/orgs")

  def execute(action: Action): Unit =
    action()

  def combine2(action1: Action, action2: Action): Action =
    () => {
      execute(action1)
      execute(action2)
    }
}

object ActionV1App extends App {
  import Action._

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
