package exercises.functors

case class ZipList[A](getZipList: List[A])

object ZipList {
  def apply[A](values: A*): ZipList[A] =
    ZipList(values.toList)
}
