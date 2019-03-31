package toimpl.rws

import exercises.rws.BathroomSecurity.{Instruction, KeyPad}

trait StateToImpl {

  def findCode(
    startingPoint: KeyPad,
    instructionLines: List[List[Instruction]]
  ): List[KeyPad]

}
