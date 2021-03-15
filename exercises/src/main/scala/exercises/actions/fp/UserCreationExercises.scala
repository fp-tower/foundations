package exercises.actions.fp

object UserCreationExercises {

  def readName(console: Console): Action[String] =
    Action {
      console.writeLine("What's your name").execute()
      console.readLine().execute()
    }

}
