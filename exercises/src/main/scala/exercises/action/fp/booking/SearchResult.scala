package exercises.action.fp.booking

// A `sealed abstract case class` is a hack to create a case class with
// * a private constructor (no `apply` or `new` outside this file)
// * no `copy` method
// As a result, we know all instances of `SearchResult` have consistent fields
// where, for example, `cheapest` is the cheapest flight from the list.
sealed abstract case class SearchResult(
  best: Option[Flight], // top secret AI algorithm
  cheapest: Option[Flight],
  fastest: Option[Flight],
  flights: List[Flight], // ordered collection of flights from cheapest to most expensive
)

object SearchResult {
  def fromList(flights: List[Flight]): SearchResult = {
    val orderedByPrice = flights.distinctBy(_.flightId).sortBy(_.unitPrice)
    new SearchResult(
      flights = orderedByPrice,
      cheapest = orderedByPrice.headOption,
      best = orderedByPrice.headOption, // we'll come up with a more clever choice later
      fastest = flights.minByOption(_.duration)
    ){}
  }
}