package exercises.sideeffect

import answers.sideeffect.IOAnswers.IO

trait IOApp extends App {
  def main(): IO[Unit]

  main.unsafeRun()
}
