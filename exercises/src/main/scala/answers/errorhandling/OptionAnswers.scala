package answers.errorhandling

import exercises.errorhandling.Country._
import exercises.errorhandling.OptionExercises.Order
import exercises.errorhandling.{Country, User, UserName}
import toimpl.errorhandling.OptionToImpl

object OptionAnswers extends OptionToImpl {

  ////////////////////////
  // 1. Use cases
  ////////////////////////

  def getOrder(id: Int, orders: List[Order]): Option[Order] =
    orders.find(_.id == id)

  def charToDigit(c: Char): Option[Int] =
    c match {
      case '0' => Some(0)
      case '1' => Some(1)
      case '2' => Some(2)
      case '3' => Some(3)
      case '4' => Some(4)
      case '5' => Some(5)
      case '6' => Some(6)
      case '7' => Some(7)
      case '8' => Some(8)
      case '9' => Some(9)
      case _   => None
    }

  def isValidateUsername(userName: String): Boolean =
    userName.length >= 3 &&
      userName.toList.forall(c => c.isLetter || c.isDigit || c == '_' || c == '-')

  def validateUsername(userName: String): Option[UserName] = {
    val trimmed = userName.trim
    if (isValidateUsername(trimmed)) Some(UserName(trimmed))
    else None
  }

  def validateCountry(country: String): Option[Country] = country match {
    case "FRA" => Some(France)
    case "DEU" => Some(Germany)
    case "CHE" => Some(Switzerland)
    case "BGR" => Some(UnitedKingdom)
    case _     => None
  }

  def validateCountry_v2(country: String): Option[Country] =
    Country.all.find(countryToAlpha3(_) == country)

  def countryToAlpha3(country: Country): String = country match {
    case France        => "FRA"
    case Germany       => "DEU"
    case Switzerland   => "CHE"
    case UnitedKingdom => "GBR"
  }

  def validateUser(username: String, country: String): Option[User] =
    (validateUsername(username), validateCountry(country)) match {
      case (Some(x), Some(y)) => Some(User(x, y))
      case _                  => None
    }

  ////////////////////////
  // 2. Composing Option
  ////////////////////////

  def tuple2[A, B](fa: Option[A], fb: Option[B]): Option[(A, B)] =
    (fa, fb) match {
      case (Some(a), Some(b)) => Some((a, b))
      case _                  => None
    }

  def map2[A, B, C](fa: Option[A], fb: Option[B])(f: (A, B) => C): Option[C] =
    (fa, fb) match {
      case (Some(a), Some(b)) => Some(f(a, b))
      case _                  => None
    }

  def map2FromTuple2[A, B, C](fa: Option[A], fb: Option[B])(f: (A, B) => C): Option[C] =
    tuple2(fa, fb).map { case (a, b) => f(a, b) }

  def tuple2FromMap2[A, B](fa: Option[A], fb: Option[B]): Option[(A, B)] =
    map2(fa, fb)((_, _))

  def validateUser_v2(username: String, country: String): Option[User] =
    map2(validateUsername(username), validateCountry(country))(User)

  def validateUser_v3(usernameStr: String, countryStr: String): Option[User] =
    for {
      username <- validateUsername(usernameStr)
      country  <- validateCountry(countryStr)
    } yield User(username, country)

  def validateUsernames(userNames: List[String]): Option[List[UserName]] =
    userNames.foldRight(Option(List.empty[UserName]))(
      (userName, acc) => map2(validateUsername(userName), acc)(_ :: _)
    )

  def sequence[A](fa: List[Option[A]]): Option[List[A]] =
    fa.foldRight(Option(List.empty[A]))(
      (a, acc) => map2(a, acc)(_ :: _)
    )

  def validateUsernames_v2(userNames: List[String]): Option[List[UserName]] =
    sequence(userNames.map(validateUsername))

  def traverse[A, B](fa: List[A])(f: A => Option[B]): Option[List[B]] =
    fa.foldRight(Option(List.empty[B]))(
      (a, acc) => map2(f(a), acc)(_ :: _)
    )

  def validateUsernames_v3(userNames: List[String]): Option[List[UserName]] =
    traverse(userNames)(validateUsername)

  def traverseFromSequence[A, B](fa: List[A])(f: A => Option[B]): Option[List[B]] =
    sequence(fa.map(f))

  def sequenceFromTraverse[A](fa: List[Option[A]]): Option[List[A]] =
    traverse(fa)(identity)

  ////////////////////////
  // 3. Error message
  ////////////////////////

  def validateUserMessage(username: String, country: String): String =
    validateUser(username, country) match {
      case Some(user) => user.toString
      case None =>
        "Invalid User, a username must be at least 3 characters long (letter, digit or \"-_\")," +
          s" a country must be a 3 letter code for either ${Country.all.map(_.toString).mkString(",")}"
    }
}
