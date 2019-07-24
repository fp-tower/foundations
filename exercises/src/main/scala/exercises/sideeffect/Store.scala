package exercises.sideeffect

sealed trait StoreAlg[A]

object StoreAlg {
  case class Put[A](key: String, value: A) extends StoreAlg[Unit]
  case class Get[A](key: String)           extends StoreAlg[A]
}

object Store {
  type Store[A] = FreeMap[StoreAlg, A]

  def put[A](key: String, value: A): Store[Unit] =
    FreeMap.lift[StoreAlg, Unit](StoreAlg.Put(key, value))

  def get[A](key: String): Store[A] =
    FreeMap.lift(StoreAlg.Get(key))

}
