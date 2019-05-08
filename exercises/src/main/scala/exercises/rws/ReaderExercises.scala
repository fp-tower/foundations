package exercises.rws

import com.typesafe.config.{Config, ConfigFactory}

object ReaderExercises extends App {

  val myConf = ConfigFactory.load("test.conf")

  // 1a. Use the following getInt/String/Boolean to implement getDatabase
  def getInt(config: Config, path: String): Int =
    config.getInt(path)

  def getString(config: Config, path: String): String =
    config.getString(path)

  def getBoolean(config: Config, path: String): Boolean =
    config.getBoolean(path)

  case class DatabaseConfig(port: Int, user: String, password: String, readOnly: Boolean)

  def getDatabase(config: Config, path: String): DatabaseConfig = ???

  case class Reader[E, A](run: E => A) {
    // 2a. Implement map
    def map[B](f: A => B): Reader[E, B] = ???

    // 2b. Implement flatMap
    def flatMap[B](f: A => Reader[E, B]): Reader[E, B] = ???
  }

  // 2c. refactor getInt/String/Boolean to use Reader[Config, ?]
  def getInt2(path: String): Reader[Config, Int] = ???

  // 2d. refactor getDatabase to use Reader[Config, ?]

  // 3a. Introduce error handling for getInt/String/Boolean
  // How does it affect getDatabase?

  case class ReaderT[F[_], E, A](run: E => F[A]) {
    // 3b. Implement map
    // Why is there a problem?
    def map[B](f: A => B): ReaderT[F, E, B] = ???

    // 3c. Implement flatMap
    def flatMap[B](f: A => ReaderT[F, E, B]): ReaderT[F, E, B] = ???
  }

  // 3d. refactor getInt/String/Boolean to use ReaderT
  def getInt3(path: String): ReaderT[Option, Config, Int] = ???
  // 3e. refactor getDatabase to use ReaderT

}
