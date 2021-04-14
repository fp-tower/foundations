package answers.action.fp.search

sealed abstract case class SearchResult(flights: List[Flight]) {
  val cheapest: Option[Flight] = flights.minByOption(_.unitPrice)
  val fastest: Option[Flight]  = flights.minByOption(_.duration)
  val best: Option[Flight]     = flights.minOption(SearchResult.bestOrder)
}

object SearchResult {
  val bestOrder: Ordering[Flight] =
    Ordering.by(flight => (flight.numberOfStops, flight.unitPrice))

  def validate(flights: List[Flight]): SearchResult = {
    val ordered = flights.distinctBy(_.flightId).sorted(bestOrder)
    new SearchResult(ordered) {}
  }
}
