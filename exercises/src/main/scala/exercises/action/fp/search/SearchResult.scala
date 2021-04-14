package exercises.action.fp.search

case class SearchResult(flights: List[Flight]) {
  val cheapest: Option[Flight] = flights.minByOption(_.unitPrice)
  val fastest: Option[Flight]  = flights.minByOption(_.duration)

  // Cheapest flight with the minimum number of stops.
  // For example,
  // flight 1: 1 stop,  50$
  // flight 2: 0 stop,  80$ <-- best, cheapest among the 0 stop flights
  // flight 3: 1 stop, 100$
  // flight 4: 0 stop, 120$
  val best: Option[Flight] =
    flights.minByOption { flight =>
      (flight.numberOfStops, flight.unitPrice)
    }
}
