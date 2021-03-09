package com.simulator

import Spells._
import scala.collection.mutable.ListBuffer

case class Rotation(damage: Int, mana: Int, length: Double, spells: List[Spell]) {

  def getDps() = damage.toDouble / length

  def getMps() = mana.toDouble / length

  def +(otherRotation: Rotation): Rotation = Rotation(damage + otherRotation.damage, mana + otherRotation.mana, length + otherRotation.length, spells ++ otherRotation.spells)

  override def toString() = f"This rotation has ${getDps()}%1.2f dps and consumes ${getMps()}%1.2f mps over $length%1.2f time:: Rotation: ${spells} "
}

class Rotations(mageStats: MageStats, statBuffs: StatBuffs = StatBuffs(), manaBuffs: ManaBuffs = ManaBuffs(), cooldowns: Cooldowns = Cooldowns()) {

  val dmgMultiplier = ArcaneTalents.arcaneInst._1 * statBuffs.m * statBuffs.coe

  val spellCost: Double => Int = cost => ((cost * ArcaneTalents.clearcast) - manaBuffs.wis).toInt

  val frostboltDmg = (Frostbolt.dmg(mageStats.spellPower + mageStats.frostSpellPower, mageStats.critChance, mageStats.hitChance) * dmgMultiplier * 1.06).toInt
  val frostboltMana = spellCost(Frostbolt.cost)

  val frostboltDrop = Rotation((frostboltDmg * dmgMultiplier).toInt * 3, frostboltMana * 3, Frostbolt.castTime * 3, List(Frostbolt, Frostbolt, Frostbolt))

  val amDmg = (ArcaneMissiles.dmg(mageStats.spellPower + mageStats.arcaneSpellPower, mageStats.critChance, mageStats.hitChance) * dmgMultiplier).toInt
  val amMana = spellCost(ArcaneMissiles.cost)

  val amFbDrop = Rotation(amDmg + frostboltDmg, amMana + frostboltMana, ArcaneMissiles.castTime + Frostbolt.castTime, List(ArcaneMissiles, Frostbolt))

  val abDmg = (ArcaneBlast(0, 0).dmg(mageStats.spellPower + mageStats.arcaneSpellPower, mageStats.critChance, mageStats.hitChance) * dmgMultiplier * 1.2).toInt
  // val baseABMana = ArcaneBlast(0, 3).cost + ArcaneBlast()

  def generateRotations(): List[Rotation] = {
    var lb = ListBuffer.empty[Rotation]
    for (i <- Range(3, 30)) {
      val arcaneBlasts = List.tabulate(i)(x => ArcaneBlast(Math.min(x, 3), if (x == 0) 3 else Math.min(x, 3)))

      lb.append(Rotation(abDmg * arcaneBlasts.size, arcaneBlasts.map(ab => spellCost(ab.cost).toInt).sum, arcaneBlasts.map(_.castTime).sum, arcaneBlasts))
    }

    val allRotations = lb.map(r => r + frostboltDrop) //++ lb.map(r => r+amFbDrop)

    allRotations.toList
  }

  def apRotation(numAP: Int): Rotation = {
    val apABDmg = (ArcaneBlast(0, 0).dmg(mageStats.spellPower + 225 + 155 + mageStats.arcaneSpellPower, mageStats.critChance, mageStats.hitChance) * 1.2 * dmgMultiplier).toInt

    val hastePercentWithCds = if (cooldowns.bloodlust.toInt >= numAP) 50 else if (cooldowns.drums >= numAP) mageStats.haste + 5.07 + 20 else mageStats.haste + 20
    val haste = hastePercentWithCds / 100d

    val numAB = (14.9 / (1.5 / (1 + haste))).toInt
    val maxAB = List.fill(numAB)(ArcaneBlast(3, 3))

    println(numAB)

    val fbRotation = Rotation((frostboltDmg * 1.3).toInt, spellCost(Frostbolt.cost * 1.3), 1.5 / (1 + haste), List(Frostbolt))
    val singleAMRotation = Rotation((amDmg * 1.3).toInt, spellCost(ArcaneMissiles.cost * 1.3).toInt, 5 / (1 + haste), List(ArcaneMissiles))

    val allABs = Rotation((apABDmg * 1.3).toInt * numAB, maxAB.map(ab => spellCost(ab.cost * 1.0923)).sum, numAB * (1.5 / (1 + haste)), maxAB)

    println(allABs + singleAMRotation)
    println(allABs + fbRotation)

    allABs + fbRotation
  }

  def timeLimitedABRotation(maxLength: Double): Rotation = {
    val abLb = ListBuffer.empty[Spell]
    while (abLb.map(_.castTime).sum < maxLength) {
      val curAB = ArcaneBlast(Math.min(abLb.length, 3), if (abLb.length == 0) 3 else Math.min(abLb.length, 3))
      abLb.append(curAB)
    }
    Rotation(abDmg * abLb.size, abLb.map(ab => spellCost(ab.cost)).sum, abLb.map(_.castTime).sum, abLb.toList)
  }

}
