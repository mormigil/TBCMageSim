package com.simulator

object Experiment {
    

    def main(args: Array[String]) {
        implicit val initialMageStats = MageStats(815, 4.91, 19.68, 0, 458, 192, 262)

        implicit val cooldowns = Cooldowns()

        val finalMageStats = Parameters.getFinalStats(initialMageStats)

        println(finalMageStats)

        val mana = Mana(finalMageStats)

        val fightLength = 107

        println(mana.totalMana(fightLength))

        val rotations = new Rotations(finalMageStats)

        // println(rotations.frostboltDrop)

        val allRotations = rotations.generateRotations()

        // allRotations.foreach(r => println(r))

        // println(rotations.apRotation(1))

        // println(rotations.apRotation(2))


        println("________________________")
        println(FullRotationbuilder.fullRotation(rotations, mana.totalMana(fightLength), fightLength, rotations.generateRotations()))
        println("________________________")

    }
}
