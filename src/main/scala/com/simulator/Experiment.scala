package com.simulator

object Experiment {

  def generateResults()(implicit initialMageStats: MageStats): String = {

    implicit val cooldowns = Cooldowns()

    val finalMageStats = Parameters.getFinalStats(initialMageStats)

    val spPlus10MageStats = Parameters.getFinalStats(initialMageStats.copy(spellPower = initialMageStats.spellPower + 10))
    val intPlus10MageStats = Parameters.getFinalStats(initialMageStats.copy(intellect = initialMageStats.intellect + 50))
    val spiritPlus10MageStats = Parameters.getFinalStats(initialMageStats.copy(spirit = initialMageStats.spirit + 50))
    val critPlus10MageStats = Parameters.getFinalStats(initialMageStats.copy(critChance = initialMageStats.critChance + 10d/22.08))

    println(finalMageStats)

    val mana = Mana(finalMageStats)

    val rotations = new Rotations(finalMageStats)

    // println(rotations.frostboltDrop)

    // val allRotations = rotations.generateRotations()

    // allRotations.foreach(r => println(r))

    // println(rotations.apRotation(1))

    // println(rotations.apRotation(2))

    val fightLengths = List.tabulate(50)(x => 50 + x*5)

    val standardRotationInfo = fightLengths.map(fl => FullRotationbuilder.fullRotation(rotations, mana, fl, rotations.generateRotations()))

    val standardRotationDps = standardRotationInfo.map(r => (r.length, r.getDps()))

    val spRotations = new Rotations(spPlus10MageStats)

    val spStatWeight = fightLengths.map(fl => FullRotationbuilder.fullRotation(spRotations, mana, fl, spRotations.generateRotations()))

    val spWeight = standardRotationInfo.zip(spStatWeight).map{case(r1, r2) => (r1.length, r2.getDps() - r1.getDps(), r1.getNumABs())}.toString()

    val intMana = new Mana(intPlus10MageStats)
    val intRotations = new Rotations(intPlus10MageStats)

    val intInfo = fightLengths.map(fl => FullRotationbuilder.fullRotation(intRotations, intMana, fl, intRotations.generateRotations()))

    val intWeight = standardRotationInfo.zip(intInfo).map{case(r1, r2) => (r1.length, (r2.getDps() - r1.getDps())/5d, r2.getNumABs())}.toString()

    val spiritMana = new Mana(spiritPlus10MageStats)

    val spiritRotations = new Rotations(spiritPlus10MageStats)

    val spiritInfo = fightLengths.map(fl => FullRotationbuilder.fullRotation(spiritRotations, spiritMana, fl, spiritRotations.generateRotations()))

    val spiritWeight = standardRotationInfo.zip(spiritInfo).map{case(r1, r2) => (r1.length, (r2.getDps() - r1.getDps())/5d, r2.getNumABs)}.toString()

    val critRotations = new Rotations(critPlus10MageStats)

    val critInfo = fightLengths.map(fl => FullRotationbuilder.fullRotation(critRotations, mana, fl, critRotations.generateRotations()))

    val critWeight = standardRotationInfo.zip(critInfo).map{case(r1, r2) => (r1.length, r2.getDps() - r1.getDps(), r2.getNumABs)}.toString()

    standardRotationInfo.zip(intInfo).flatMap{ case(r1, r2) => 
        val dpsDiff = r2.getDps - r1.getDps
        if(dpsDiff < 0 || dpsDiff > 10) Option((r2, r1, dpsDiff)) else None
    }.toString()

    s"standardRotationDps: $standardRotationDps spWeights: $spWeight </p><p> intWeights: $intWeight spiritWeights $spiritWeight critWeights $critWeight"

  }
}
