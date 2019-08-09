package exercises.sideeffect

import exercises.sideeffect.IOExercises.IO

trait IOApp extends App {
  def main(): IO[Unit]

  main.unsafeRun()
}
