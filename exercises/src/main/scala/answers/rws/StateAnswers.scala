package answers.rws

import cats.data.State
import cats.implicits._
import exercises.rws.BathroomSecurity.{move, Instruction, KeyPad}
import toimpl.rws.StateToImpl

object StateAnswers extends StateToImpl {

  def findCode(
    startingPoint: KeyPad,
    instructionLines: List[List[Instruction]]
  ): List[KeyPad] =
    instructionLines
      .traverse(moveLine)
      .runA(startingPoint)
      .value

  def moveLine(instructions: List[Instruction]): State[KeyPad, KeyPad] =
    instructions.traverse_(moveS) >> State.get

  def moveS(instruction: Instruction): State[KeyPad, Unit] =
    State(k => (move(k, instruction), ()))

}
