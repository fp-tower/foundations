package exercises.sideeffect

import answers.sideeffect.IOAnswers.IO

sealed trait RandomAlg[A]
object RandomAlg {
  case object RandomInt extends RandomAlg[Int]
}

object Random {
  type Random[A] = FreeMap[RandomAlg, A]

  val int: Random[Int]         = FreeMap.lift(RandomAlg.RandomInt)
  val boolean: Random[Boolean] = int.map(_ % 2 == 0)

  val randomToIO: NaturalTransformation[RandomAlg, IO] = new NaturalTransformation[RandomAlg, IO] {
    def apply[A](fa: RandomAlg[A]): IO[A] =
      fa match {
        case RandomAlg.RandomInt => IO(scala.util.Random.nextInt())
      }
  }

  def runRandom[A](fa: Random[A]): IO[A] =
    FreeMap.compileIO(fa.mapK(randomToIO))
}
