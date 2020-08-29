package answers.dataprocessing

import answers.dataprocessing.TimeUtil._
import kantan.csv._
import kantan.csv.ops._

object TemperatureNotebookOld extends App {

  val sampleSize = Int.MaxValue
//  val sampleSize    = 1000000

  val rawData: java.net.URL = getClass.getResource("/city_temperature.csv")

  val reader: CsvReader[Either[ReadError, Sample]] = rawData.asCsvReader[Sample](rfc.withHeader)

  val (failures, successes) = timeOne("load data", reader.take(sampleSize).toList.partitionMap(identity))

  println(s"${failures.size} failed and ${successes.size} succeeded")

  val partitions    = 10
  val partitionSize = successes.length / partitions + 1
  val computeEC     = ThreadPoolUtil.fixedSize(partitions, "compute")
  val ec            = computeEC
//  val ec = ExecutionContext.global

  val sequentialSamples      = ParList.partition(partitionSize, successes)
  val sequentialTemperatures = sequentialSamples.map(_.temperature)

  val parallelSamples      = sequentialSamples.setExecutionContext(Some(ec))
  val parallelTemperatures = parallelSamples.map(_.temperature)

  println(s"Min date is ${parallelSamples.minBy(_.localDate)}")
  println(s"Max date is ${parallelSamples.maxBy(_.localDate)}")

  println(s"Min temperature is ${parallelTemperatures.min}")
  println(s"Max temperature is ${parallelTemperatures.max}")

  val sumTemperature = parallelTemperatures.fold(Monoid.sumDouble)
  val size           = parallelTemperatures.size

  val avgTemperature = sumTemperature / size

  println(s"Average temperature is $avgTemperature")

  val summary = parallelTemperatures.foldMap(Summary.one)(Summary.monoid)

  println(s"Temperature summary is $summary")

  time(100, "sum sequential") { ParList.sum(sequentialTemperatures) }
  time(100, "max sequential") { sequentialTemperatures.max }
  time(100, "min sequential") { sequentialTemperatures.min }
  time(100, "summary global sequential") {
    sequentialTemperatures.foldMap(Summary.one)(Summary.monoid)
  }
  time(100, "summary perCity sequential") {
    sequentialSamples.foldMap(perCity)(Monoid.map(Summary.monoid))
  }
  time(100, "summary allLocations sequential") {
    sequentialSamples.foldMap(allLocations)(Monoid.map(Summary.monoid))
  }

  time(100, "sum parallel") { ParList.sum(parallelTemperatures) }
  time(100, "max parallel") { parallelTemperatures.max }
  time(100, "min parallel") { parallelTemperatures.min }
  time(100, "summary global parallel") {
    parallelTemperatures.foldMap(Summary.one)(Summary.monoid)
  }
  time(100, "summary perCity summary parallel") {
    parallelSamples.foldMap(perCity)(Monoid.map(Summary.monoid))
  }
  time(100, "summary allLocations summary parallel") {
    parallelSamples.foldMap(allLocations)(Monoid.map(Summary.monoid))
  }

  def perCity(sample: Sample): Map[String, Summary] =
    Map(
      sample.city -> Summary.one(sample.temperature)
    )

  def allLocations(sample: Sample): Map[String, Summary] = {
    val summary = Summary.one(sample.temperature)
    Map(
      sample.region              -> summary,
      sample.country             -> summary,
      sample.state.getOrElse("") -> summary,
      sample.city                -> summary,
    )
  }
}
