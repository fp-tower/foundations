package exercises.ch4

object ListExercises {

  sealed trait MyList[A]
  object MyList {
    case class MyNil[A]() extends MyList[A]
    case class MyCons[A](head: A, tail: MyList[A]) extends MyList[A]
  }

  // a. Implement head
  def head[A](xs: MyList[A]) = ???


  // b. Implement last
  def last[A](xs: MyList[A]) = ???


  // c. Implement size
  def size[A](xs: MyList[A]): Int = ???


  // d. what issue do you see with the signature of size? How would fix it?



  // e. Implement reverse
  def reverse[A](xs: MyList[A]): MyList[A] = ???


  // f. Implement map
  def map[A, B](xs: MyList[A])(f: A => B): MyList[B] = ???


  // g. Implement filter
  def filter[A](xs: MyList[A])(f: A => Boolean): MyList[A] = ???


  // h. Implement lookup
  def lookup[A](xs: MyList[A])(i: Int): Option[A] = ???




  // a. Implement foldLeft
  def foldLeft[A, B](xs: MyList[A])(z: B)(f: (B, A) => B): B = ???


  // b. Implement foldRight
  def foldRight[A, B](xs: MyList[A])(z: B)(f: (A, => B) => B): B = ???


  // c. what are the differences between foldLeft and foldRight
  // hint: ???


  // d. Implement sum in terms of a fold
  def sum[A](xs: List[A]): Int = ???


  // e. Implement reverse and map using a fold
  def reverse_fold[A](xs: MyList[A]): MyList[A] = ???


  def map_fold[A, B](xs: MyList[A])(f: A => B): MyList[B] = ???


  // f. Implement filter using a fold
  def filter_fold[A](xs: MyList[A])(f: A => Boolean): MyList[A] = ???



  // g. Implement head and last using a fold
  def head_fold[A](xs: MyList[A]): Option[A] = ???


  def last_fold[A](xs: MyList[A]): Option[A] = ???

}
