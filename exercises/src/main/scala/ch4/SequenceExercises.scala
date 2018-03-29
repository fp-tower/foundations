package ch4

object SequenceExercises {

  sealed trait MyList[A]
  object MyList {
    case class MyNil[A]() extends MyList[A]
    case class MyCons[A](head: A, tail: MyList[A]) extends MyList[A]
  }

}
