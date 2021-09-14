package com.runt9.fusionOfSouls.model.unit.ability

abstract class Ability(val baseCooldown: Int) {
    var modifiedCooldown = baseCooldown.toDouble()
    var cooldownElapsed = 0.0
    abstract val name: String
    abstract val description: String

    fun updateCooldown(modifier: Double) {
        val priorRatio = cooldownElapsed / modifiedCooldown
        modifiedCooldown = baseCooldown / modifier
        cooldownElapsed = modifiedCooldown * priorRatio
    }

    fun updateRemaining(amount: Double) {
        cooldownElapsed += amount
    }

    fun resetCooldown() {
        cooldownElapsed = 0.0
    }

    suspend fun useSkill(abilityUseContext: AbilityUseContext) {
        useSkillInternal(abilityUseContext)
        resetCooldown()
    }

    suspend fun canUseSkill(abilityUseContext: AbilityUseContext): Boolean {
        return cooldownElapsed >= modifiedCooldown && canUseSkillInternal(abilityUseContext)
    }

    protected abstract suspend fun useSkillInternal(abilityUseContext: AbilityUseContext)
    protected abstract suspend fun canUseSkillInternal(abilityUseContext: AbilityUseContext): Boolean
}
