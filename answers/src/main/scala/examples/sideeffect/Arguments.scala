package examples.sideeffect

import java.time.LocalDate

object Arguments {

  def createDate(year: Int, month: Int, dayOfMonth: Int): LocalDate =
    LocalDate.of(year, month, dayOfMonth)

  val createDateVal: (Int, Int, Int) => LocalDate =
    (year: Int, month: Int, dayOfMonth: Int) => LocalDate.of(year, month, dayOfMonth)

}
