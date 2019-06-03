package examples

object Introduction {

  def reverseStringImperative(x: String): String = {
    val charArray   = x.toCharArray
    val lastIndex   = x.length - 1
    val middleIndex = x.length / 2

    for (i <- 0 until middleIndex) {
      val tmp = charArray(i)
      charArray(i) = charArray(lastIndex - i)
      charArray(lastIndex - i) = tmp
    }

    String.valueOf(charArray)
  }

  def reverseStringFunctional(x: String): String =
    x.foldLeft(List.empty[Char])((acc, c) => c :: acc).mkString

  def reverseStringFunctional2(x: String): String =
    x.foldLeft(new StringBuffer(x.length))(_.insert(0, _)).toString

}
