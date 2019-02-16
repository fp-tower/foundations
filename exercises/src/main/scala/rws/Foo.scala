package rws

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

object Foo extends App {

  val dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")

  def dateToStr(date: Date): String =
    dateFormat.format(date)

  val today = Calendar.getInstance.getTime

  println(dateToStr(today))

  val log: StringBuffer = new StringBuffer()

  def doSomething(x: Int): Int = {
    log.append(s"called doSomething with $x")
    x % 2
  }

}
