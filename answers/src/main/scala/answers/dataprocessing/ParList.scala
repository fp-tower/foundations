package answers.dataprocessing

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

case class ParList[A](executionContext: ExecutionContext, partitions: List[List[A]]) {
  def toList: List[A] =
    partitions.flatten

  def map[To](update: A => To): ParList[To] =
    ParList(executionContext, partitions.map(_.map(update)))

  def foldLeft[To](default: To)(combine: (To, A) => To): To =
    sys.error("Impossible")

  def foldLeftV2[To](default: To)(combineElement: (To, A) => To)(combinePartition: (To, To) => To): To =
    partitions
      .map(_.foldLeft(default)(combineElement))
      .foldLeft(default)(combinePartition)

  // 1st `monoFoldLeft` implementation before introducing `Monoid`
  def monoFoldLeftV1(default: A)(combine: (A, A) => A): A =
    partitions
      .map(_.foldLeft(default)(combine))
      .foldLeft(default)(combine)

  def monoFoldLeft(monoid: Monoid[A]): A =
    partitions
      .map(_.foldLeft(monoid.default)(monoid.combine))
      .foldLeft(monoid.default)(monoid.combine)

  def size: Int =
    parFoldMap(_ => 1)(CommutativeMonoid.sumNumeric)

  def min(implicit ord: Ordering[A]): Option[A] =
    minBy(identity)

  def max(implicit ord: Ordering[A]): Option[A] =
    maxBy(identity)

  // 1st `minBy` implementation before `foldMap` and `reduceMap`
  def minByV1[To](zoom: A => To)(implicit ord: Ordering[To]): Option[A] =
    partitions.flatMap(_.minByOption(zoom)).minByOption(zoom)

  // 1st `maxBy` implementation before `foldMap` and `reduceMap`
  def maxByV1[To: Ordering](zoom: A => To)(implicit ord: Ordering[To]): Option[A] =
    minBy(zoom)(ord.reverse)

  def minBy[To: Ordering](zoom: A => To): Option[A] =
    parReduceMap(identity)(Semigroup.minBy(zoom))

  def maxBy[To: Ordering](zoom: A => To): Option[A] =
    parReduceMap(identity)(Semigroup.maxBy(zoom))

  def sum(implicit num: Numeric[A]): A =
    fold(CommutativeMonoid.sumNumeric)

  def fold(monoid: Monoid[A]): A =
    parFoldMap(identity)(monoid)

  def foldMap[To](update: A => To)(monoid: Monoid[To]): To =
    partitions
      .map { partition =>
        partition.foldLeft(monoid.default)((state, element) => monoid.combine(state, update(element)))
      }
      .foldLeft(monoid.default)(monoid.combine)

  def reducedMap[To](update: A => To)(semigroup: Semigroup[To]): Option[To] =
    partitions.filter(_.nonEmpty) match {
      case Nil => None
      case nonEmptyPartitions =>
        val reducedPartitions = nonEmptyPartitions.map(_.map(update).reduceLeft(semigroup.combine))
        val reduceAll         = reducedPartitions.reduceLeft(semigroup.combine)
        Some(reduceAll)
    }

  def reduce(semigroup: Semigroup[A]): Option[A] =
    parReduceMap(identity)(semigroup)

  def parFoldMap[To](update: A => To)(monoid: Monoid[To]): To =
    parReduceMap(update)(monoid).getOrElse(monoid.default)

  def parFoldMapUnordered[To](update: A => To)(monoid: CommutativeMonoid[To]): To = {
    val ref = Ref(monoid.default)

    def foldPartition(partition: List[A]): Future[Any] =
      Future {
        val res = partition.foldLeft(monoid.default)((state, value) => monoid.combine(state, update(value)))
        ref.modify(monoid.combine(res, _))
      }(executionContext)

    partitions.map(foldPartition).foreach(Await.ready(_, Duration.Inf))
    ref.get
  }

  def parReduceMap[To](update: A => To)(semigroup: Semigroup[To]): Option[To] = {
    def reducePartition(partition: List[A]): Future[To] =
      Future {
//        println(s"[${Thread.currentThread.getName}] Start on")
        var state = update(partition.head)
        for (a <- partition.tail) state = semigroup.combine(state, update(a))
//        println(s"[${Thread.currentThread.getName}] Computed $state")
        state
      }(executionContext)

    partitions
      .filter(_.nonEmpty)
      .map(reducePartition)
      .map(Await.result(_, Duration.Inf))
      .reduceLeftOption(semigroup.combine)
  }

  def withExecutionContext(ec: ExecutionContext): ParList[A] =
    copy(executionContext = ec)

}

object ParList {
  def apply[A](executionContext: ExecutionContext, partitions: List[A]*): ParList[A] =
    new ParList(executionContext, partitions.toList)

  def byPartitionSize[A](executionContext: ExecutionContext, partitionSize: Int, items: List[A]): ParList[A] =
    if (items.isEmpty) ParList(executionContext)
    else ParList(executionContext, items.grouped(partitionSize).toList)

  def byNumberOfPartition[A](executionContext: ExecutionContext, numberOfPartition: Int, items: List[A]): ParList[A] = {
    val partitionSize = math.ceil(items.length / numberOfPartition.toDouble).toInt
    byPartitionSize(executionContext, partitionSize, items)
  }

}
