package com.simulator

object FullRotationbuilder {

  def fullRotation(rotations: Rotations, mana: Mana, fightLength: Int, stdRotations: List[Rotation])(implicit cooldowns: Cooldowns, mageStats: MageStats): Rotation = {
    val numAPs = ((fightLength + 15) / 180d).ceil.toInt

    val apRotation = List.tabulate(numAPs)(i => rotations.apRotation(i + 1)).reduce(_ + _)

    val addedLustTime = ((40 - 15) * cooldowns.bloodlust) * .3
    val addedDrumsTime = (30 - 15) * cooldowns.drums * .0507
    val addedIVTime = 5 * numAPs * .2 + 20 * .2
    val addedHasteTime = (fightLength - 15 * numAPs) * mageStats.haste / 100d
    val totalAddedHasteTime = addedLustTime + addedDrumsTime + addedIVTime + addedHasteTime

    val hasteIncreasedFightLength = fightLength - numAPs * 15 + totalAddedHasteTime - 1 // - 7.5 //total time has 1 less second for first AB

    val remainingMana = mana.totalMana(fightLength) - apRotation.mana // - mana.evocate

    println(remainingMana)

    val baseManaConsumed = hasteIncreasedFightLength * rotations.baseRotation.getMps

    val number33ABsPossible = Math.floor((remainingMana - baseManaConsumed).toDouble/(Spells.ArcaneBlast(3, 3).cost - 1.5*rotations.baseRotation.getMps))

    val number33ABs = Math.min((hasteIncreasedFightLength / 1.5).toInt, number33ABsPossible)

    val numBaseRotations = Math.floor((hasteIncreasedFightLength - number33ABs * 1.5) / rotations.baseRotation.length).toInt

    val baseRotations: Rotation = if(numBaseRotations >= 2) List.fill(numBaseRotations)(rotations.baseRotation).reduce(_ + _) else rotations.baseRotation

    val endingRotation = apRotation + rotations.allThreeStackRotation(number33ABs.toInt) + baseRotations

    val timeRemaining = hasteIncreasedFightLength - endingRotation.length

    val bonusSmoothingRotation = Rotation((timeRemaining*rotations.baseRotation.getDps).toInt, (timeRemaining*rotations.baseRotation.getMps).toInt, timeRemaining, List(Spells.ArcaneBlast(5, 5)))

    endingRotation + bonusSmoothingRotation

    // val maxMPS = remainingMana / hasteIncreasedFightLength

    // println(maxMPS)

    // val maxMPSRotation = stdRotations.filter(r => r.getMps() < maxMPS && r.length < hasteIncreasedFightLength).sortBy(_.getMps()).last

    // val numMaxRotation = ((hasteIncreasedFightLength) / maxMPSRotation.length).toInt

    // println(maxMPSRotation, numMaxRotation)

    // val maxMPSFullRotation = if (numMaxRotation >= 2) List.fill(numMaxRotation)(maxMPSRotation).reduce(_ + _) else maxMPSRotation

    // val extraTime = hasteIncreasedFightLength - numMaxRotation * maxMPSRotation.length

    // println(extraTime)

    // val extraRotation = stdRotations.filter(_.length < extraTime).sortBy(_.length).lastOption.getOrElse(rotations.timeLimitedABRotation(extraTime))

    // val resultingRotation = apRotation + maxMPSFullRotation + extraRotation

    // Rotation(resultingRotation.damage, resultingRotation.mana, fightLength, resultingRotation.spells)
  }

}
