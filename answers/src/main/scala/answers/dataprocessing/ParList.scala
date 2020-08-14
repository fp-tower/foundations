package answers.dataprocessing

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

case class ParList[A](partitions: List[List[A]], maybeExecutionContext: Option[ExecutionContext]) {
  def toList: List[A] =
    partitions.flatten

  def map[To](update: A => To): ParList[To] =
    ParList(partitions.map(_.map(update)), maybeExecutionContext)

  def size: Int =
    partitions.map(_.size).sum

  def flatFoldLeft[B](default: B)(combine: (B, A) => B): B =
    toList.foldLeft(default)(combine)

  def foldLeft[B](default: B)(combine: (B, A) => A): B =
    sys.error("Impossible")

  def monoFoldLeft(default: A)(combine: (A, A) => A): A =
    partitions
      .foldLeft(default)((acc, partition) => combine(acc, partition.foldLeft(default)(combine)))

  def foldMap[To](update: A => To)(default: To, combine: (To, To) => To): To =
    ops.foldMap(update)(default, combine)

  def reduceMap[To](update: A => To)(combine: (To, To) => To): Option[To] =
    partitions.filter(_.nonEmpty) match {
      case Nil => None
      case nonEmptyPartitions =>
        val reducedPartitions = nonEmptyPartitions.map(_.map(update).reduceLeft(combine))
        val reduceAll         = reducedPartitions.reduceLeft(combine)
        Some(reduceAll)
    }

  def withExecutionContext(ec: ExecutionContext): ParList[A] =
    copy(maybeExecutionContext = Some(ec))

  def ops: Ops =
    maybeExecutionContext.fold[Ops](sequential)(parallel)

  def sequential: SequentialOps                   = new SequentialOps()
  def parallel(ec: ExecutionContext): ParallelOps = new ParallelOps()(ec)

  trait Ops {
    def foldMap[To](update: A => To)(default: To, combine: (To, To) => To): To
  }

  class SequentialOps extends Ops {
    def foldMap[To](update: A => To)(default: To, combine: (To, To) => To): To =
      partitions
        .foldLeft(default) { (acc, partition) =>
          val foldPartition = partition.foldLeft(default) { (partitionAcc, value) =>
            combine(partitionAcc, update(value))
          }
          combine(acc, foldPartition)
        }
  }

  class ParallelOps(implicit ec: ExecutionContext) extends Ops {
    def foldMap[To](update: A => To)(default: To, combine: (To, To) => To): To = {
      def foldPartition(partition: List[A]): Future[To] =
        Future {
          partition.foldLeft(default) { (partitionAcc, value) =>
            combine(partitionAcc, update(value))
          }
        }

      val tasks: List[Future[To]]     = partitions.map(foldPartition)
      val allTasks: Future[List[To]]  = Future.sequence(tasks)
      val allTasksCompleted: List[To] = Await.result(allTasks, Duration.Inf)

      allTasksCompleted.foldLeft(default)(combine)
    }
  }

}

object ParList {
  def apply[A](partitions: List[A]*): ParList[A] =
    ParList(partitions.toList, None)

  def partition[A](partitionSize: Int, items: List[A]): ParList[A] =
    ParList(items.grouped(partitionSize).toList, None)

  def max(numbers: ParList[Double]): Option[Double] =
    numbers.foldMap(Option(_))(None, maxOption)

  def min(numbers: ParList[Double]): Option[Double] =
    numbers.foldMap(Option(_))(None, minOption)

  def sum(numbers: ParList[Double]): Double =
    numbers.foldMap(identity)(0, _ + _)

  def maxOption(optFirst: Option[Double], optSecond: Option[Double]): Option[Double] =
    combineOption(optFirst, optSecond)(_ max _)

  def minOption(optFirst: Option[Double], optSecond: Option[Double]): Option[Double] =
    combineOption(optFirst, optSecond)(_ min _)

  def combineOption[A](optFirst: Option[A], optSecond: Option[A])(combine: (A, A) => A): Option[A] =
    (optFirst, optSecond) match {
      case (Some(first), Some(second)) => Some(combine(first, second))
      case (Some(first), None)         => Some(first)
      case (None, Some(second))        => Some(second)
      case (None, None)                => None
    }
}
