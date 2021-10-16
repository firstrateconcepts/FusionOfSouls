package net.firstrateconcepts.fusionofsouls.service.unit

import com.badlogic.ashley.core.Entity
import net.firstrateconcepts.fusionofsouls.model.component.attackBonus
import net.firstrateconcepts.fusionofsouls.model.component.attrs
import net.firstrateconcepts.fusionofsouls.model.component.baseDamage
import net.firstrateconcepts.fusionofsouls.model.component.critBonus
import net.firstrateconcepts.fusionofsouls.model.component.currentHp
import net.firstrateconcepts.fusionofsouls.model.component.defense
import net.firstrateconcepts.fusionofsouls.model.component.evasion
import net.firstrateconcepts.fusionofsouls.model.component.hasTarget
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.component.isInRangeOfTarget
import net.firstrateconcepts.fusionofsouls.model.component.maxHp
import net.firstrateconcepts.fusionofsouls.model.component.name
import net.firstrateconcepts.fusionofsouls.model.component.target
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.duringRun.RandomizerService
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunService
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.ext.withUnit
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus

// TODO: Add hooks/interceptors
// TODO: Do the requisite refactoring and unit testing for this class
class AttackService(
    override val eventBus: EventBus,
    private val randomizer: RandomizerService,
    private val engine: AsyncPooledEngine,
    private val unitManager: UnitManager
) : RunService() {
    private val logger = fosLogger()

    fun canEntityAttack(entity: Entity): Boolean {
        if (!entity.hasTarget) return false

        return entity.isInRangeOfTarget
    }

    fun processEntityAttack(attacker: Entity) {
        if (!canEntityAttack(attacker)) return

        engine.withUnit(attacker.target!!) { defender ->
            runOnServiceThread {
                val rawRoll = randomizer.rng.nextInt(0, 100)
                val attackBonus = attacker.attrs.attackBonus()
                doAttack(attacker, defender, rawRoll, attackBonus.toInt())
            }
        }
    }

    fun doAttack(attacker: Entity, defender: Entity, rawRoll: Int, attackBonus: Int) {
        val tag = "[${attacker.name} -> ${defender.name}]: "
        val combatStr = StringBuilder(tag)

        val evasion = defender.attrs.evasion()
        val attackDiff = attackBonus - evasion
        val finalRoll = rawRoll + attackDiff


        combatStr.append("Rolled [$rawRoll] | $attackBonus Bonus vs $evasion Evasion | Total Roll [$finalRoll]: ")

        if (finalRoll < 0) {
            combatStr.append("Attack missed!")
            logger.info { combatStr.toString() }
            return
        }

        combatStr.append("Attack hits! ")

        val isCrit = finalRoll > 100

        val expectedRoll = 50 + attackDiff
        val damageScale = 1 + ((finalRoll - expectedRoll) / 200)

        val critMulti = attacker.attrs.critBonus()
        val totalDamageMulti = if (isCrit) {
            combatStr.append("CRITICAL HIT! Crit multi [${critMulti}] ")
            damageScale * critMulti
        } else {
            combatStr.append("No crit. ")
            damageScale
        }

        val baseDamage = attacker.attrs.baseDamage()
        val defense = defender.attrs.defense()
        val rawDamage = baseDamage * totalDamageMulti
        val finalDamage = rawDamage / defense

        unitManager.updateUnitHp(defender.id, -finalDamage)

        combatStr.append("Damage Data: [Damage Scale: $damageScale | Raw Damage: $rawDamage | Defense: $defense | Final: $finalDamage | Defender HP: ${defender.currentHp} / ${defender.attrs.maxHp()}]")
        logger.info { combatStr.toString() }
    }
}
