package exercises.rws

import exercises.rws.BathroomSecurity.{Instruction, KeyPad}
import toimpl.rws.StateToImpl

object StateExercises extends StateToImpl {

  // e.g. findCode(
  //      startingPoint = K5,
  //      instructionLines = List(
  //        List(Up, Left, Left),
  //        List(Right, Right, Down, Down, Down),
  //        List(Left, Up, Right, Down, Left),
  //        List(Up, Up, Up, Up, Up, Down)
  //      )
  // ) == List(K1, K9, K8, K5)
  //
  // * You start at "5" and move up (to "2"), left (to "1"), and left (you can't, and stay on "1"), so the first button is 1.
  // * Starting from the previous button ("1"), you move right twice (to "3") and then down three times (stopping at
  // "9" after two moves and ignoring the third), ending up with 9.
  // * Continuing from "9", you move left, up, right, down, and left, ending with 8.
  // * Finally, you move up four times (stopping at "2"), then down once, ending with 5.
  //
  //
  // source https://adventofcode.com/2016/day/2
  def findCode(
    startingPoint: KeyPad,
    instructionLines: List[List[Instruction]]
  ): List[KeyPad] = ???

}
