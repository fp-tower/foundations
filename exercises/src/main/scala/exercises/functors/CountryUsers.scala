package exercises.functors

object CountryUsers {

  sealed trait Country
  case object UK      extends Country
  case object France  extends Country
  case object Germany extends Country

  case class User(name: String, age: Int, country: Country)

  def getUsers(country: Country): Either[String, List[User]] =
    country match {
      case UK      => Left("Unsupported: Brexit")
      case France  => Right(List(User("Yves", 14, France), User("Laura", 12, France), User("Lucas", 20, France)))
      case Germany => Right(List(User("Helene", 22, Germany), User("Daniel", 50, Germany)))
    }

  def checkAdult(user: User): Either[String, Unit] =
    if (user.age < 18) Left(s"${user.name} is not an adult")
    else Right(())

}
