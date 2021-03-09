package com.simulator

case class Mana(mageStats: MageStats, statBuffs: StatBuffs = StatBuffs(), manaBuffs: ManaBuffs = ManaBuffs(), consumables: Consumables = Consumables(), cooldowns: Cooldowns = Cooldowns()) {

  val manaRegenInCombat = .6

  val spiritRegen = 0.001d + mageStats.spirit * 0.009327 * Math.sqrt(mageStats.intellect)

  val mps = (manaBuffs.bow + manaBuffs.mst.toDouble) / 5d + manaBuffs.vampTouch

  val totalMPS = mps + spiritRegen * .6

  val maxMana = 1961 + mageStats.intellect * 15

  val manaPotion = 2400

  val manaGem = 2400

  val evocate = maxMana * .6

  val innervate = cooldowns.innervate * (5 - .6) * spiritRegen * 20

  val manaTideTotem = if (cooldowns.manaTideTotem) .24 * maxMana else 0

  def totalMana(fightLength: Int): Int = {
    (maxMana + fightLength * mps + evocate + innervate + manaTideTotem + math.ceil(fightLength.toDouble / 120d) * (manaGem + manaPotion)).toInt
  }

}
