package com.simulator

object FullRotationbuilder {

    def fullRotation(rotations: Rotations, totalMana: Int, fightLength: Int, stdRotations: List[Rotation])(implicit cooldowns: Cooldowns, mageStats: MageStats): Rotation = {
        val numAPs = ((fightLength + 15) / 180d).ceil.toInt

        val apRotation = List.tabulate(numAPs)(i => rotations.apRotation(i+1)).reduce(_ + _)
        
        val addedLustTime = ((40-15)*cooldowns.bloodlust)*.3
        val addedDrumsTime = (30-15)*cooldowns.drums*.0507
        val addedIVTime = 5*numAPs*.2 + 20*.2
        val addedHasteTime = (fightLength - 15*numAPs)*mageStats.haste/100d
        val totalAddedHasteTime = addedLustTime + addedDrumsTime + addedIVTime + addedHasteTime

        val hasteIncreasedFightLength = fightLength - numAPs*15 + totalAddedHasteTime - 1  - 7.5//total time has 1 less second for first AB

        val remainingMana = totalMana - apRotation.mana

        println(remainingMana)

        val maxMPS = remainingMana / hasteIncreasedFightLength

        println(maxMPS)

        val maxMPSRotation = stdRotations.filter(r => r.getMps() < maxMPS && r.length < hasteIncreasedFightLength).sortBy(_.getMps()).last

        val numMaxRotation = ((hasteIncreasedFightLength)/maxMPSRotation.length).toInt

        println(maxMPSRotation, numMaxRotation)

        val maxMPSFullRotation = if(numMaxRotation >= 2) List.fill(numMaxRotation)(maxMPSRotation).reduce(_ + _) else maxMPSRotation

        val extraTime = hasteIncreasedFightLength - numMaxRotation * maxMPSRotation.length

        println(extraTime)

        val extraRotation = stdRotations.filter(_.length<extraTime).sortBy(_.length).lastOption.getOrElse(rotations.timeLimitedABRotation(extraTime))

        val resultingRotation = apRotation + maxMPSFullRotation + extraRotation

        Rotation(resultingRotation.damage, resultingRotation.mana, fightLength, resultingRotation.spells)
    }

}
