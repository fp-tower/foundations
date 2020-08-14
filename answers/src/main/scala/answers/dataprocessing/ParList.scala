package answers.dataprocessing

import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicReference

import scala.annotation.tailrec
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

  def foldMap[To](update: A => To)(monoid: Monoid[To]): To =
    ops.foldMap(update)(monoid)

  def reduceMap[To](update: A => To)(semigroup: Semigroup[To]): Option[To] =
    ops.reducedMap(update)(semigroup)

  def setExecutionContext(maybeEc: Option[ExecutionContext]): ParList[A] =
    copy(maybeExecutionContext = maybeEc)

  def ops: Ops =
    maybeExecutionContext.fold[Ops](sequential)(parallel)

  def sequential: SequentialOps                   = new SequentialOps()
  def parallel(ec: ExecutionContext): ParallelOps = new ParallelOps()(ec)

  trait Ops {
    def foldMap[To](update: A => To)(monoid: Monoid[To]): To
    def reducedMap[To](update: A => To)(semigroup: Semigroup[To]): Option[To]
  }

  class SequentialOps extends Ops {
    def foldMap[To](update: A => To)(monoid: Monoid[To]): To =
      partitions
        .foldLeft(monoid.default) { (acc, partition) =>
          val foldPartition = partition.foldLeft(monoid.default) { (partitionAcc, value) =>
            monoid.combine(partitionAcc, update(value))
          }
          monoid.combine(acc, foldPartition)
        }

    def reducedMap[To](update: A => To)(semigroup: Semigroup[To]): Option[To] =
      partitions.filter(_.nonEmpty) match {
        case Nil => None
        case nonEmptyPartitions =>
          val reducedPartitions = nonEmptyPartitions.map(_.map(update).reduceLeft(semigroup.combine))
          val reduceAll         = reducedPartitions.reduceLeft(semigroup.combine)
          Some(reduceAll)
      }
  }

  class ParallelOps(implicit ec: ExecutionContext) extends Ops {

    def foldMap[To](update: A => To)(monoid: Monoid[To]): To =
      reducedMap(update)(monoid).getOrElse(monoid.default)

    def reducedMap[To](update: A => To)(semigroup: Semigroup[To]): Option[To] =
      partitions.filter(_.nonEmpty) match {
        case Nil => None
        case nonEmptyPartitions =>
          def foldPartition(partition: List[A]): Future[To] =
            Future { _reduceMap(partition)(update, semigroup) }

          val tasks: List[Future[To]]     = nonEmptyPartitions.map(foldPartition)
          val allTasks: Future[List[To]]  = Future.sequence(tasks)
          val allTasksCompleted: List[To] = Await.result(allTasks, Duration.Inf)

          Some(allTasksCompleted.reduceLeft(semigroup.combine))
      }

    private def _reduceMap[To](partition: List[A])(update: A => To, semigroup: Semigroup[To]): To = {
      var state = update(partition.head)
      for (a <- partition.tail) state = semigroup.combine(state, update(a))
      state
    }

  }

}

object ParList {
  def apply[A](partitions: List[A]*): ParList[A] =
    ParList(partitions.toList, None)

  def partition[A](partitionSize: Int, items: List[A]): ParList[A] =
    ParList(items.grouped(partitionSize).toList, None)

  def max(numbers: ParList[Double]): Option[Double] =
    numbers.reduceMap(identity)(Semigroup.max)

  def min(numbers: ParList[Double]): Option[Double] =
    numbers.reduceMap(identity)(Semigroup.min)

  def sum(numbers: ParList[Double]): Double =
    numbers.foldMap(identity)(Monoid.sumDouble)

}
