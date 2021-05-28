package answers.action.fp.search

sealed abstract case class SearchResult(flights: List[Flight]) {
  val cheapest: Option[Flight] = flights.minByOption(_.unitPrice)
  val fastest: Option[Flight]  = flights.minByOption(_.duration)
  val best: Option[Flight]     = flights.minOption(SearchResult.bestOrdering)
}

object SearchResult {
  val bestOrdering: Ordering[Flight] =
    Ordering.by(flight => (flight.numberOfStops, flight.unitPrice))

  def apply(flights: List[Flight]): SearchResult = {
    val deduplicate = flights.groupBy(_.flightId).map { case (_, sameIds) => sameIds.minBy(_.unitPrice) }.toList
    val ordered     = deduplicate.sorted(bestOrdering)
    new SearchResult(ordered) {}
  }
}
