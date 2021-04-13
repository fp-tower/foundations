package answers.action.async.booking

import java.time.{Duration, LocalDate}

import answers.action.async.IO
import answers.action.fp.booking.{Airport, Flight, FlightPredicate}

import scala.concurrent.ExecutionContext
import scala.util.Random

object SearchFlightServiceApp extends App {

  val fakeClient: SearchFlightClient = new SearchFlightClient {
    def search(from: Airport, to: Airport, date: LocalDate): IO[List[Flight]] =
      IO.log("Starting Search") *>
        IO { Random.nextInt(10) }.flatMap {
          case 0 =>
            IO.sleep(Duration.ofMillis(Random.nextLong(1000 * 2))) *> IO.fail[List[Flight]](new Exception("Boom!"))
          case _ =>
            IO.sleep(Duration.ofMillis(Random.nextLong(1000 * 60 * 5))) *> IO(List.empty[Flight])
        } *< IO.log("Search Completed")
  }

  val partners = List.range(1, 10).map { i =>
    Partner(
      name = s"Partner-${i}",
      commission = Random.nextDouble(),
      timeout = Duration.ofSeconds(2),
      client = fakeClient
    )
  }

  val ec = ExecutionContext.global

  SearchFlightService
    .fromPartners(partners, ec)
    .search(Airport.parisOrly, Airport.tokyoAneda, LocalDate.now(), FlightPredicate.Direct)
    .flatMap(_ => IO.log("Complete"))
    .unsafeRun()

}
