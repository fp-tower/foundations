package ch3

object Ch3Exercises {


  // a. Implement getUser such as it returns the first user matching the id
  case class User(id: Int, name: String)
  def getUser(id: Int, xs: List[User]): Option[User] = ???




  // a. Implement validatePassword such as it returns true if it
  // * has at least 8 characters long
  // * contains at least one upper/lower case letter
  // * contains at least a digit
  def validatePassword(s: String): Boolean = ???




  // b. how would change validatePassword such as it describes the kind of error




  // c. change validatePassword such as it returns a NonEmptyList of error




}
