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

  def reduceMap[To](update: A => To)(combine: (To, To) => To): Option[To] =
    partitions.filter(_.nonEmpty) match {
      case Nil => None
      case nonEmptyPartitions =>
        val reducedPartitions = nonEmptyPartitions.map(_.map(update).reduceLeft(combine))
        val reduceAll         = reducedPartitions.reduceLeft(combine)
        Some(reduceAll)
    }

  def setExecutionContext(maybeEc: Option[ExecutionContext]): ParList[A] =
    copy(maybeExecutionContext = maybeEc)

  def ops: Ops =
    maybeExecutionContext.fold[Ops](sequential)(parallel)

  def sequential: SequentialOps                   = new SequentialOps()
  def parallel(ec: ExecutionContext): ParallelOps = new ParallelOps()(ec)

  trait Ops {
    def foldMap[To](update: A => To)(monoid: Monoid[To]): To
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
  }

  class ParallelOps(implicit ec: ExecutionContext) extends Ops {
    def foldMap[To](update: A => To)(monoid: Monoid[To]): To =
      if (monoid.isCommutative)
        foldMapCommutative(update)(monoid.default, monoid.combine)
      else
        foldMapNonCommutative(update)(monoid.default, monoid.combine)

    def foldMapNonCommutative[To](update: A => To)(default: To, combine: (To, To) => To): To = {
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

    def foldMapCommutative[To](update: A => To)(default: To, combine: (To, To) => To): To = {
      val latch = new CountDownLatch(partitions.size)
      val ref   = Ref(default)

      def foldPartition(partition: List[A]): Future[Unit] =
        Future {
          println(s"Start computation in ${Thread.currentThread().getName}")
          val folded = partition.foldLeft(default) { (partitionAcc, value) =>
            combine(partitionAcc, update(value))
          }
          println(s"End computation in ${Thread.currentThread().getName}")
          ref.modify(combine(folded, _))
          latch.countDown()
        }

      partitions.foreach(foldPartition)
      latch.await()
      ref.get
    }
  }

}

object ParList {
  def apply[A](partitions: List[A]*): ParList[A] =
    ParList(partitions.toList, None)

  def partition[A](partitionSize: Int, items: List[A]): ParList[A] =
    ParList(items.grouped(partitionSize).toList, None)

  def max(numbers: ParList[Double]): Option[Double] =
    numbers.foldMap(Option(_))(Monoid.maxOption)

  def min(numbers: ParList[Double]): Option[Double] =
    numbers.foldMap(Option(_))(Monoid.minOption)

  def sum(numbers: ParList[Double]): Double =
    numbers.foldMap(identity)(Monoid.sum)

}
