package exercises.sideeffect

import toimpl.sideeffect.SideEffectToImpl

import scala.util.Try

object SideEffectExercises extends SideEffectToImpl {

  // Implement map
  def map[A, B](fa: IO[A])(f: A => B): IO[B] = ???


  // Implement flatMap
  def flatMap[A, B](fa: IO[A])(f: A => IO[B]): IO[B] = ???




  // Implement consoleProgram using Console methods (see Console.scala)
  def unsafeReadNameProgram: String = {
    println("What's your name?")
    val name = scala.io.StdIn.readLine()
    println(s"Your name is $name")
    name
  }

  def readNameProgram: IO[String] = ???

  // Implement readInt such as it reads an int from StdIn
  // if user does not enter a valid Int, make the IO fail
  // try to reuse Console.readLine and stringToInt
  def readInt: IO[Int] = ???

  def stringToInt(s: String): Try[Int] = Try(s.toInt)


  // Implement insertUser and getAllUsers
  case class User(name: String, age: Int)
  var users: Map[String, User] = Map.empty

  def unsafeInsertUser(user: User): Unit =
    users += user.name -> user

  def unsafeGeAllUsers: List[User] =
    users.values.toList

  def insertUser(ref: IORef[Map[String, User]], user: User): IO[Unit] = ???

  def geAllUsers(ref: IORef[Map[String, User]]): IO[List[User]] = ???


  def unsafeUserDbProgram: Unit = {
    while(true){
      println("""Add a new user or press q to quit""")
      val name = scala.io.StdIn.readLine()
      if(name == "q") return ()
      val age = scala.io.StdIn.readLine().toInt
      val user = User(name, age)
      unsafeInsertUser(user)
      println("All users:")
      unsafeGeAllUsers.foreach(u => println(u))
    }
  }

  def userDbProgram: IO[Map[String, User]] = ???


  // Implement void
  def void[A](fa: IO[A]): IO[Unit] = ???



}


object UnsafeConsoleApp extends App {
  SideEffectExercises.unsafeReadNameProgram
}

object ConsoleApp extends IOApp {
  import SideEffectExercises._
  def main(): IO[Unit] = void(readNameProgram)
}

object UnsafeUserApp extends App {
  SideEffectExercises.unsafeUserDbProgram
}