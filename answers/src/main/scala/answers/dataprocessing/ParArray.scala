package answers.dataprocessing

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.reflect.ClassTag

case class ParArray[A](underlying: Array[A], partitionSize: Int) {
  def toParList(ec: ExecutionContext): ParList[A] =
    ParList.byPartitionSize(ec, partitionSize, underlying.toList)

  def map[To: ClassTag](update: A => To): ParArray[To] =
    ParArray(underlying.map(update), partitionSize)

  def foldMap[To](update: A => To)(monoid: Monoid[To])(ec: ExecutionContext): To = {
    implicit val executionContext = ec
    var state: List[Future[To]]   = Nil
    var index                     = 0
    while (index < underlying.length) {
      val startIndex = index
      val nextIndex  = (index + partitionSize) min underlying.length
      val task       = Future { foldMap(startIndex, nextIndex)(update)(monoid) }
      state = task :: state
      index = nextIndex
    }
    val allTasks = Await.result(Future.sequence(state.reverse), Duration.Inf)
    allTasks.foldLeft(monoid.default)(monoid.combine)
  }

  private def foldMap[To](from: Int, to: Int)(update: A => To)(monoid: Monoid[To]): To = {
    var state = monoid.default
    for (index <- from.until(to)) {
      val value   = underlying(index)
      val updated = update(value)
      state = monoid.combine(state, updated)
    }
    state
  }
}
