package exercises.errorhandling

import exercises.errorhandling.Country._

object OptionExercises {

  ////////////////////////
  // 1. Use cases
  ////////////////////////

  // 1a. Implement `getUser` that looks up the first order in a list with a matching id
  // such as getUser(123, List(User(222, "foo"), User(123, "bar"))) == Some(User(123, "bar"))
  // but     getUser(111, List(User(222, "foo"), User(123, "bar"))) == None
  // Note: you can use any functions from List API.
  case class User(id: Int, name: String)
  def getUser(id: Int, users: List[User]): Option[User] = ???

  // 1b. Implement `charToDigit` which parses digit characters into number
  // such as charToDigit('0') == Some('0')
  //         charToDigit('1') == Some('1')
  //         charToDigit('9') == Some('9')
  // but     charToDigit('x') == None
  // How would you test `charToDigit` with property based testing (PBT)?
  def charToDigit(c: Char): Option[Int] = ???

  sealed trait Order {
    // 1c. Implement `optLimit` which return the limit if the order has one
    // such as LimitOrder(100).optLimit == Some(100)
    //         StopLimitOrder(10, 20).optLimit == Some(20)
    // but     StopOrder(100).optLimit == None
    def optLimit: Option[Double] = ???
    // 1c. Implement `optLimitOrder` which checks if the current order is a LimitOrder
    // such as LimitOrder(100).optLimitOrder == Some(LimitOrder(100))
    // but     StopLimitOrder(10, 20).optLimitOrder == None
    def optLimitOrder: Option[Order.LimitOrder] = ???
  }

  object Order {
    case object MarketOrder                                extends Order
    case class LimitOrder(limit: Double)                   extends Order
    case class StopOrder(stop: Double)                     extends Order
    case class StopLimitOrder(stop: Double, limit: Double) extends Order
  }

  ////////////////////////
  // 2. API
  ////////////////////////

  // 2a. Implement `Person#address` which returns the complete address of a Person if both
  // `streetNumber` and `streetName` is defined.
  // such as Person(John, Some(10), Some("High street")).address == Some("10 High street")
  // but     Person(John, Some(10), None).address == None
  // Note: you can use any functions from Option API such as map, flatMap.
  // Bonus: Implement `Person#oddAddress` that behaves similarly to `address` but
  // it will only return an address if the street number is odd
  case class Person(name: String, streetNumber: Option[Int], streetName: Option[String]) {
    def address: Option[String]    = ???
    def oddAddress: Option[String] = ???
  }

  // 2b. Implement `BankUser#getBalance` which returns the balance of the provided accountId
  // or 0.0 if the account does not belong to the user.
  // The balance of a CashAccount is a field of the case class, while it needs to be computed
  // for a ShareAccount.
  case class BankUser(name: String,
                      cashAccounts: Map[AccountId, CashAccount],
                      shareAccounts: Map[AccountId, CashAccount]) {
    def getBalance(accountId: AccountId): Double = ???
  }
  case class AccountId(value: String)
  case class CashAccount(id: AccountId, balance: Double)
  case class ShareAccount(id: AccountId, shares: List[Share]) {
    def balance: Double = ???
  }
  case class Share(quantity: Int, unitPrice: Double)

  // 2c. Implement `filterLimitOrders` which only keeps the LimitOrder from the list
  // such as filterLimitOrders(List(LimitOrder(10), MarketOrder, LimitOrder(20))) == List(LimitOrder(10), LimitOrder(20))
  def filterLimitOrders(orders: List[Order]): List[Order.LimitOrder] = ???

  // 2c. Implement `checkAllLimitOrders` which verifies all input orders are LimitOrder
  // such as checkAllLimitOrders(List(LimitOrder(10),              LimitOrder(20))) == Some(List(LimitOrder(10), LimitOrder(20)))
  // but     checkAllLimitOrders(List(LimitOrder(10), MarketOrder, LimitOrder(20))) == None
  // Note: you may want to use listSequence or listTraverse defined below
  def checkAllLimitOrders(orders: List[Order]): Option[List[Order.LimitOrder]] = ???

  def listSequence[A](xs: List[Option[A]]): Option[List[A]] =
    xs.foldRight(Option(List.empty[A])) {
      case (None, _)            => None
      case (_, None)            => None
      case (Some(a), Some(acc)) => Some(a :: acc)
    }

  def listTraverse[A, B](xs: List[A])(f: A => Option[B]): Option[List[B]] =
    listSequence(xs.map(f))

  ////////////////////////
  // 3. Limitation
  ////////////////////////

}
