package exercises.action.fp.search

import java.time.ZoneId

case class Airport(code: String, timeZone: ZoneId)

object Airport {
  // A few examples
  val londonHeathrow       = Airport("LHR", ZoneId.of("Europe/London"))
  val londonGatwick        = Airport("LGW", ZoneId.of("Europe/London"))
  val melbourne            = Airport("MEL", ZoneId.of("Australia/Melbourne"))
  val parisOrly            = Airport("ORY", ZoneId.of("Europe/Paris"))
  val parisCharlesDeGaulle = Airport("CDG", ZoneId.of("Europe/Paris"))
  val tokyoAneda           = Airport("TYO", ZoneId.of("Asia/Tokyo"))
}
