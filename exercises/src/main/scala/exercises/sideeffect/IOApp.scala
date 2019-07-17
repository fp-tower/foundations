package exercises.sideeffect

trait IOApp extends App {
  def main(): IO[Unit]

  main.unsafeRun()
}
