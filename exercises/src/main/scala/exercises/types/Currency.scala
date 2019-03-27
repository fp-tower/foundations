package exercises.types

sealed trait Currency

object Currency {
  case object EUR extends Currency
  case object GBP extends Currency
  case object CHF extends Currency
}

