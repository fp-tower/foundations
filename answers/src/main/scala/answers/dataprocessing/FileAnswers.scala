package answers.dataprocessing

import java.io.File

object FileAnswers {

  def diskUsage(file: File): Long =
    if (!file.exists) 0
    else if (file.isDirectory) file.listFiles.map(diskUsage).sum
    else file.length()

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
