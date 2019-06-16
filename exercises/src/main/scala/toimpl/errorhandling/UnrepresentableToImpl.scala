package toimpl.errorhandling

import java.time.{Duration, Instant}

import exercises.errorhandling.UnrepresentableExercises.{Item, Order}
import org.scalacheck.{Arbitrary, Properties}

trait UnrepresentableToImpl {

  def checkout(order: Order): Order

  def submit(order: Order, now: Instant): Order

  def deliver(order: Order, now: Instant): (Order, Duration)

  def totalItem(item: Item): Double

  def totalItemProperties(implicit arb: Arbitrary[Item]): Properties

}
