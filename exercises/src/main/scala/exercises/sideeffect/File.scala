package exercises.sideeffect

object File {
  def readLines(path: String): IO[List[String]] =
    IO.effect(
      scala.io.Source.fromResource(path).getLines().toList
    )

  def write(path: String, content: String): String =
    ???
}
