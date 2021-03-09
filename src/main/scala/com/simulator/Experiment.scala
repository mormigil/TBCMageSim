// package com.simulator

// object Experiment {

//   def main(args: Array[String]) {
//     implicit val initialMageStats = MageStats(1285, 4.60, 25.22, 19.4, 517, 258, 50)

//     implicit val cooldowns = Cooldowns()

//     val finalMageStats = Parameters.getFinalStats(initialMageStats)

//     println(finalMageStats)

//     val mana = Mana(finalMageStats)

//     val fightLength = 60

//     println(mana.totalMana(fightLength))

//     val rotations = new Rotations(finalMageStats)

//     // println(rotations.frostboltDrop)

//     val allRotations = rotations.generateRotations()

//     // allRotations.foreach(r => println(r))

//     // println(rotations.apRotation(1))

//     // println(rotations.apRotation(2))

//     println("________________________")
//     println(FullRotationbuilder.fullRotation(rotations, mana, fightLength, rotations.generateRotations()))
//     println("________________________")

//   }
// }
