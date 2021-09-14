package com.runt9.fusionOfSouls.model.unit.ability

import ktx.log.info

class DefaultAbility : Ability(7) {
    private val additionalDamageMulti = 1.25
    override val name = "Heavy Strike"
    override val description = "Unit performs a powerful attack with its weapon dealing basic skill damage plus an additional ${additionalDamageMulti}x damage."

    override suspend fun useSkillInternal(abilityUseContext: AbilityUseContext) {
        abilityUseContext.run {
            info("[${me.name}]") { "Using skill" }
            unitManager.unitAttackAnimation(me)
            unitManager.performAttack(me, me.target!!, listOf(me.unit.secondaryAttrs.skillMulti.value, 1.25))
        }
    }

    override suspend fun canUseSkillInternal(abilityUseContext: AbilityUseContext): Boolean {
        abilityUseContext.run {
            return me.withinAttackRange(me.target!!)
        }
    }
}
