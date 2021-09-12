package com.runt9.fusionOfSouls.model.unit.skill

abstract class Skill(private val baseCooldown: Int) {
    var modifiedCooldown = baseCooldown.toDouble()
    var cooldownElapsed = 0.0

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

    suspend fun useSkill(skillUseContext: SkillUseContext) {
        useSkillInternal(skillUseContext)
        resetCooldown()
    }

    suspend fun canUseSkill(skillUseContext: SkillUseContext): Boolean {
        return cooldownElapsed >= modifiedCooldown && canUseSkillInternal(skillUseContext)
    }

    protected abstract suspend fun useSkillInternal(skillUseContext: SkillUseContext)
    protected abstract suspend fun canUseSkillInternal(skillUseContext: SkillUseContext): Boolean
}
