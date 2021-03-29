package exercises.action.fp

import java.time.{Instant, LocalDate}

case class User(
  name: String,
  dateOfBirth: LocalDate,
  subscribedToMailingList: Boolean,
  createdAt: Instant
)
