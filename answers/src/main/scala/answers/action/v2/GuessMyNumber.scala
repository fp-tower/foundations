package answers.action.v2

import scala.io.StdIn
import scala.util.Random

object GuessMyNumberApp extends App {
  GuessMyNumber.startGame(10)
}

object GuessMyNumber {

  def startGame(attempts: Int): Unit = {
    val numberToGuess     = Random.nextInt(100)
    var remainingAttempts = attempts
    var found             = false

    while (remainingAttempts > 0 && !found) {
      println("Guess a number between 1 and 100")
      val guess = StdIn.readLine().toInt
      if (guess > numberToGuess) println("Guess is too high")
      else if (guess < numberToGuess) println("Guess is too low")
      else found = true

      remainingAttempts -= 1
    }

    if (found) println(s"You win, it was $numberToGuess")
    else println(s"You lose, it was $numberToGuess")
  }

}
