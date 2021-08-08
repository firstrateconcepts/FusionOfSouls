package com.runt9.fusionOfSouls.model.unit.skill

class DefaultSkill : Skill(7) {
    override suspend fun useSkillInternal(skillUseContext: SkillUseContext) {
        skillUseContext.run {
            println("${me.name}: Using skill")
            unitManager.unitAttackAnimation(me)
            unitManager.performAttack(me, me.target!!, listOf(me.unit.secondaryAttrs.skillMulti.value, 1.25))
        }
    }

    override suspend fun canUseSkillInternal(skillUseContext: SkillUseContext): Boolean {
        skillUseContext.run {
            return me.canAttack(me.target!!)
        }
    }
}
