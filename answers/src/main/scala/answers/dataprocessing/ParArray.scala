package answers.dataprocessing

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.reflect.ClassTag

case class ParArray[A](executionContext: ExecutionContext, underlying: Array[A], partitionSize: Int) {
  def toParList: ParList[A] =
    ParList.byPartitionSize(executionContext, partitionSize, underlying.toList)

  def map[To: ClassTag](update: A => To): ParArray[To] =
    ParArray(executionContext, underlying.map(update), partitionSize)

  def parFoldMap[To](update: A => To)(monoid: Monoid[To]): To =
    parReduceMap(update)(monoid).getOrElse(monoid.default)

  def parReduceMap[To](update: A => To)(semigroup: Semigroup[To]): Option[To] =
    if (underlying.isEmpty) None
    else {
      var state: List[Future[To]] = Nil
      var index                   = 0
      while (index < underlying.length) {
        val startIndex = index
        val nextIndex  = (index + partitionSize) min underlying.length
        val task       = Future { _reduceMap(startIndex, nextIndex)(update)(semigroup) }(executionContext)
        state = task :: state
        index = nextIndex
      }
      state.reverse.map(Await.result(_, Duration.Inf)).reduceLeftOption(semigroup.combine)
    }

  private def _reduceMap[To](from: Int, to: Int)(update: A => To)(semigroup: Semigroup[To]): To = {
    var state = update(underlying(from))
    for (index <- (from + 1).until(to)) {
      val value   = underlying(index)
      val updated = update(value)
      state = semigroup.combine(state, updated)
    }
    state
  }
}
