package answers.dataprocessing

import java.io.File

import scala.collection.mutable

object FileAnswers {

  def diskUsageImperative(input: File): Long = {
    var total = 0L
    val queue = mutable.Queue(input)

    while (queue.nonEmpty) {
      val file = queue.dequeue()
      total += file.length()

      if (file.isDirectory)
        queue.addAll(file.listFiles())
    }

    total
  }

  def diskUsage(file: File): Long =
    if (file.isDirectory)
      file.length() + file.listFiles.map(diskUsage).sum
    else file.length()

  def largestFileSize(file: File): Long =
    if (file.isDirectory)
      file.listFiles
        .map(largestFileSize)
        .maxOption
        .getOrElse(file.length())
    else file.length()

  def filterFiles(file: File, predicate: File => Boolean): List[File] =
    if (file.isDirectory)
      file
        .listFiles()
        .toList
        .flatMap(filterFiles(_, predicate))
    else
      List(file).filter(predicate)

  /**
    * @see https://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc
    * @see https://en.wikipedia.org/wiki/Zettabyte
    * @param fileSize Up to Exabytes
    * @return
    */
  def humanReadableByteSize(fileSize: Long): String = {
    if (fileSize <= 0) return "0 B"
    // kilo, Mega, Giga, Tera, Peta, Exa, Zetta, Yotta
    val units: Array[String] = Array("B", "kB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB")
    val digitGroup: Int      = (Math.log10(fileSize) / Math.log10(1024)).toInt
    f"${fileSize / Math.pow(1024, digitGroup)}%3.3f ${units(digitGroup)}"
  }
}
