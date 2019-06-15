package toimpl.errorhandling

import java.time.{Duration, Instant}

import exercises.errorhandling.UnrepresentableExercises.{Item, Order}

trait UnrepresentableToImpl {

  def totalItem(item: Item): Double

  def checkout(order: Order): Order

  def submit(order: Order, now: Instant): Order

  def deliver(order: Order, now: Instant): (Order, Duration)

}
