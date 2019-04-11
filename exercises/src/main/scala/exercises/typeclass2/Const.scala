package exercises.typeclass2

case class Const[A, B](getConst: A){
  def as[C]: Const[A, C] = Const(getConst)
}
