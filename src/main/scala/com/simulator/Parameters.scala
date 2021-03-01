package com.simulator

case class MageStats(spellPower: Int, hitChance: Double, critChance: Double, haste: Double, intellect: Int, spirit: Int, arcaneSpellPower: Int = 0,
                    fireSpellPower: Int = 0, frostSpellPower: Int = 0)

case class StatBuffs(arcaneIntellect: Boolean = true, divineSpirit: Boolean = true, blessingOfKings: Boolean = true, heroicPresence: Boolean = true,
                    wrathOfAirTotem: Boolean = true, impJotC: Boolean = true, misery: Boolean = true, elements: Boolean = true, impScorch: Boolean = true,
                    giftOfTheWild: Boolean = true, spellfireBonus: Boolean = true, humanSpirit: Boolean = true, chaoticSkyfire: Boolean = true,
                    gnomishIntellect: Boolean = false, totemOfWrath: Boolean = false, moonkinAura: Boolean = false){
    val ai = if(arcaneIntellect) 40 else 0
    val ds = if(divineSpirit) 40 else 0
    val dsi = if(divineSpirit) .1 else 0
    val bok = if(blessingOfKings) 1.1 else 0
    val hp = if(heroicPresence) 1 else 0
    val woat = if(wrathOfAirTotem) 102 else 0
    val ijotc = if(impJotC) 3 else 0
    val m = if(misery) 1.05 else 0
    val is = if(impScorch) 1.15 else 0
    val gotw = if(giftOfTheWild) 18 else 0
    val sfb = if(spellfireBonus) .07 else 0
    val tow = if(totemOfWrath) 3 else 0
    val mka = if(moonkinAura) 5 else 0 
    val hs = if(humanSpirit) 1.1 else 1
    val gi = if(gnomishIntellect) 1.05 else 1
    val coe = if(elements) 1.1 else 0
    val csm = if(chaoticSkyfire) 1.03 else 0

}

case class ManaBuffs(blessingOfWisdom: Boolean = true, manaSpringTotem: Boolean = true, jow: Boolean = true, vampiricTouch: Boolean = true) {
    val bow = if(blessingOfWisdom) 49 else 0
    val mst = if(manaSpringTotem) 50 else 0
    val wis = if(jow) 37 else 0//74 mana on a 50% proc chance
    val vampTouch = 50
}

case class Consumables(spellDmgFood: Boolean = true, brilliantWizardOil: Boolean = true, spellCritFood: Boolean = false, supremeFlask: Boolean = true,
                    distilledFlask: Boolean = false, blindingLightFlask: Boolean = false, adeptAndDraenic: Boolean = false, flameCap: Boolean = false){
    val sdf = if(spellDmgFood) 23 else 0
    val bwo = if(brilliantWizardOil) (36, 14) else (0, 0)
    val scf = if(spellCritFood) 20 else 0
    val sf = if(supremeFlask) 70 else 0
    val df = if(distilledFlask) 65 else 0
    val aad = if(adeptAndDraenic) (24, 30) else (0, 0)
    val fc = if(flameCap) 70 else 0
    val blf = if(blindingLightFlask) 80 else 0
}

case class Cooldowns(innervate: Int = 1, manaTideTotem: Boolean = true, bloodlust: Int = 1, drums: Int = 0) // get drums working

class Talents(spec: String){
    val isArcaneFrost = if(spec.toUpperCase=="ARCANEFROST") true else false
    val isFire = if(spec.toUpperCase=="FIRE") true else false
    val isFrost = if(spec.toUpperCase=="FROST") true else false
    val isArcaneFire = if(spec.toUpperCase == "ARCANEFIRE") true else false
}

object ArcaneTalents {
    val isArcane = true

    val clearcast = if(isArcane) .9 else 1
    val clearcastCrit = if(isArcane) 3 else 1
    val arcaneImpact = if(isArcane) 6 else 1
    val arcaneMind = if(isArcane) 1.15 else 1
    val arcaneInst = if(isArcane) (1.03, 3) else (1d, 0)
    val spellPower = if(isArcane) .25 else 0
    val mindMastery = if(isArcane) .25 else 0
    val arcaneFocus = if(isArcane) 10 else 0
}


object Parameters {

    def critRatingToChance(rating: Int): Double = {
        rating/22.08
    }

    def hitRatingToChance(rating: Int): Double = {
        rating/12.62
    }

    def getFinalStats(inputMageStats: MageStats, statBuffs: StatBuffs = StatBuffs(), consumables: Consumables = Consumables()): MageStats = {

        val intellect: Int = ((inputMageStats.intellect + statBuffs.ai + statBuffs.gotw + consumables.df + consumables.aad._2)*statBuffs.bok*ArcaneTalents.arcaneMind*statBuffs.gi).toInt

        val intSpellPower = intellect*(ArcaneTalents.mindMastery+statBuffs.sfb)

        val spirit: Int = ((inputMageStats.spirit + statBuffs.ds + statBuffs.gotw + consumables.aad._2)*statBuffs.bok*statBuffs.hs).toInt

        val spiritSpellPower = spirit*statBuffs.dsi

        val spellPower = inputMageStats.spellPower + intSpellPower + spiritSpellPower + statBuffs.woat + consumables.sdf + consumables.bwo._1
                        + consumables.sf + consumables.aad._1

        val arcaneSpellPower = inputMageStats.arcaneSpellPower + consumables.blf

        val crit = inputMageStats.critChance + statBuffs.ijotc + statBuffs.mka + statBuffs.tow + critRatingToChance(consumables.bwo._2+consumables.aad._1) + 3

        val hit = inputMageStats.hitChance + statBuffs.is

        MageStats(spellPower.toInt, hit, crit, inputMageStats.haste, intellect, spirit, arcaneSpellPower)
    }

}