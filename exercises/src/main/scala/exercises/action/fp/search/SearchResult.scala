package exercises.action.fp.search

case class SearchResult(flights: List[Flight]) {
  val cheapest: Option[Flight] = flights.minByOption(_.unitPrice)
  val fastest: Option[Flight]  = flights.minByOption(_.duration)
  val best: Option[Flight]     = flights.minOption(SearchResult.bestOrdering)
}

object SearchResult {
  // First order by number of stops (0, 1, 2, ...) and then by price.
  // For example, sorting the following flights with best order
  // flight A: 1 stop, 100$
  // flight B: 0 stop,  80$
  // flight C: 1 stop,  50$
  // flight D: 0 stop, 120$
  // will produce: flight B, flight D, flight C, flight A
  val bestOrdering: Ordering[Flight] =
    Ordering.by(flight => (flight.numberOfStops, flight.unitPrice))

  def fromFlights(flights: List[Flight]): SearchResult = {
    val ordered = flights.sorted(bestOrdering)
    SearchResult(ordered)
  }
}
