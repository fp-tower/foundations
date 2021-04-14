package exercises.action.fp.search

case class Airport(code: String, city: String, country: String)

object Airport {
  // A few examples
  val londonHeathrow       = Airport("LHR", "London", "UK")
  val londonGatwick        = Airport("LGW", "London", "UK")
  val melbourne            = Airport("MEL", "Melbourne ", "Australia")
  val parisOrly            = Airport("ORY", "Paris", "France")
  val parisCharlesDeGaulle = Airport("CDG", "Paris", "France")
  val tokyoAneda           = Airport("TYO", "Tokyo", "Japan")
}
