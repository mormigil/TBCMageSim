package com.simulator

object Spells {

  sealed trait Spell {
    val cost: Int
    val castTime: Double
    val spCoeff: Double
    val minDmg: Int
    val maxDmg: Int
    val critMult: Double
    val bonusCrit: Double
    val bonusHit: Double

    def dmg(spellPower: Int, critChance: Double, hitChance: Double): Int = {
      (((maxDmg - minDmg) / 2d + spCoeff * spellPower) * (math.max(hitChance + bonusHit, 99) / 100d) * (1 + ((critChance + bonusCrit) / 100d) * critMult)).toInt
    }
  }

  case class ArcaneBlast(costMult: Int, hasteMult: Int) extends Spell {
    val cost = (195 * (1 + .75 * costMult)).toInt
    val castTime = 2.5 - (hasteMult / 3d)
    val spCoeff = 2.5 / 3.5
    val minDmg = 668
    val maxDmg = 772
    val critMult = 1.5 + ArcaneTalents.spellPower + .03
    val bonusCrit = ArcaneTalents.arcaneImpact + ArcaneTalents.clearcastCrit + ArcaneTalents.arcaneInst._2
    val bonusHit = ArcaneTalents.arcaneFocus

    def nextBlast() = {
      if (costMult == 3) ArcaneBlast(3, 3)
      else ArcaneBlast(costMult + 1, hasteMult + 1)
    }

    override def toString() = s"AB$costMult,$hasteMult"
  }

  object Frostbolt extends Spell {
    val cost = (330 * .85).toInt
    val castTime = 2.5
    val spCoeff = .95 * 3d / 3.5
    val minDmg = 600
    val maxDmg = 647
    val critMult = 1.5 + .5 + ArcaneTalents.spellPower + .03
    val bonusCrit = ArcaneTalents.arcaneInst._2 + ArcaneTalents.clearcastCrit
    val bonusHit = 3

    override def toString() = "FB"
  }

  object Scorch extends Spell {
    val cost = 180
    val castTime = 1.5
    val spCoeff = 1.5 / 3.5
    val minDmg = 305
    val maxDmg = 361
    val critMult = (1.5 + ArcaneTalents.spellPower + .03) * 1.4
    val bonusCrit = 4 + ArcaneTalents.arcaneInst._2 + ArcaneTalents.clearcastCrit
    val bonusHit = 0
  }

  object Fireball extends Spell {
    val cost = 425
    val castTime = 3
    val spCoeff = 3.5 / 3.5
    val minDmg = 649
    val maxDmg = 821
    val critMult = (1.5 + ArcaneTalents.spellPower + .03) * 1.4
    val bonusCrit = ArcaneTalents.arcaneInst._2 + ArcaneTalents.clearcastCrit
    val bonusHit = 0
  }

  object ArcaneMissiles extends Spell {
    val cost = 740
    val castTime = 5
    val spCoeff = 5 / 3.5
    val minDmg = (267.6f * 5).toInt
    val maxDmg = minDmg
    val critMult = 1.5 + ArcaneTalents.spellPower + .03
    val bonusCrit = ArcaneTalents.arcaneInst._2 + ArcaneTalents.clearcastCrit
    val bonusHit = 10

    override def toString() = "AM"
  }

}
