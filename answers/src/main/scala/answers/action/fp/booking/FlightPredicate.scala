package answers.action.fp.booking

import java.time.{Duration, LocalDate, ZoneId}

import Ordering.Implicits._

sealed trait FlightPredicate { self =>
  def isValid(flight: Flight): Boolean

  def &&(other: FlightPredicate): FlightPredicate =
    new FlightPredicate {
      def isValid(flight: Flight): Boolean =
        self.isValid(flight) && other.isValid(flight)
    }

  def ||(other: FlightPredicate): FlightPredicate =
    new FlightPredicate {
      def isValid(flight: Flight): Boolean =
        self.isValid(flight) || other.isValid(flight)
    }
}

object FlightPredicate {
  case object Direct extends FlightPredicate {
    def isValid(flight: Flight): Boolean = flight.numberOfStops == 0
  }

  case object OneStop extends FlightPredicate {
    def isValid(flight: Flight): Boolean = flight.numberOfStops == 1
  }

  case object TwoOrMoreStops extends FlightPredicate {
    def isValid(flight: Flight): Boolean = flight.numberOfStops >= 2
  }

  case class ShorterThan(duration: Duration) extends FlightPredicate {
    def isValid(flight: Flight): Boolean =
      flight.duration <= duration
  }

  case class CheaperThan(maxPrice: Double) extends FlightPredicate {
    def isValid(flight: Flight): Boolean =
      flight.cost <= maxPrice
  }

  case class BasicSearch(from: Airport, to: Airport, date: LocalDate) extends FlightPredicate {
    def isValid(flight: Flight): Boolean =
      flight.from == from &&
        flight.to == to &&
        flight.departureAt.atZone(ZoneId.of("UTC")).toLocalDate == date
  }

}
