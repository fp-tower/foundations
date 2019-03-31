package exercises.rws

// source https://adventofcode.com/2016/day/2
object BathroomSecurity {

  sealed trait KeyPad
  object KeyPad {
    case object K1 extends KeyPad
    case object K2 extends KeyPad
    case object K3 extends KeyPad
    case object K4 extends KeyPad
    case object K5 extends KeyPad
    case object K6 extends KeyPad
    case object K7 extends KeyPad
    case object K8 extends KeyPad
    case object K9 extends KeyPad
  }

  sealed trait Instruction
  object Instruction {
    case object Up    extends Instruction
    case object Down  extends Instruction
    case object Left  extends Instruction
    case object Right extends Instruction
  }

  import KeyPad._
  import Instruction._

  def move(keyPad: KeyPad, instruction: Instruction): KeyPad =
    moveOpt(keyPad, instruction).getOrElse(keyPad)

  def moveOpt(keyPad: KeyPad, instruction: Instruction): Option[KeyPad] =
    (keyPad, instruction) match {
      case (K1, Right) => Some(K2)
      case (K1, Left)  => None
      case (K1, Up)    => None
      case (K1, Down)  => Some(K4)

      case (K2, Right) => Some(K3)
      case (K2, Left)  => Some(K1)
      case (K2, Up)    => None
      case (K2, Down)  => Some(K5)

      case (K3, Right) => None
      case (K3, Left)  => Some(K2)
      case (K3, Up)    => None
      case (K3, Down)  => Some(K6)

      case (K4, Right) => Some(K5)
      case (K4, Left)  => None
      case (K4, Up)    => Some(K1)
      case (K4, Down)  => Some(K7)

      case (K5, Right) => Some(K6)
      case (K5, Left)  => Some(K4)
      case (K5, Down)  => Some(K8)
      case (K5, Up)    => Some(K2)

      case (K6, Right) => None
      case (K6, Left)  => Some(K5)
      case (K6, Down)  => Some(K9)
      case (K6, Up)    => Some(K3)

      case (K7, Right) => Some(K8)
      case (K7, Left)  => None
      case (K7, Down)  => None
      case (K7, Up)    => Some(K4)

      case (K8, Right) => Some(K9)
      case (K8, Left)  => Some(K7)
      case (K8, Down)  => None
      case (K8, Up)    => Some(K5)

      case (K9, Right) => None
      case (K9, Left)  => Some(K8)
      case (K9, Down)  => None
      case (K9, Up)    => Some(K6)
    }

  def parseInstruction(c: Char): Option[Instruction] =
    c match {
      case 'R' => Some(Right)
      case 'L' => Some(Left)
      case 'U' => Some(Up)
      case 'D' => Some(Down)
    }

}
