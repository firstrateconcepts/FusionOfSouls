package net.firstrateconcepts.fusionofsouls.model.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.firstrateconcepts.fusionofsouls.model.attribute.Attribute
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributePriority
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.priority

class AttributesComponent : Component, Iterable<Attribute> {
    val attrs = AttributeType.values().associateWith { Attribute(it) }
    override fun iterator() = attrs.values.iterator()
}

val attrsMapper = mapperFor<AttributesComponent>()
val Entity.attrs get() = this[attrsMapper]!!
val Entity.primaryAttrs get() = this.attrs.primary
val Entity.secondaryAttrs get() = this.attrs.secondary

operator fun AttributesComponent.get(type: AttributeType): Attribute = attrs[type]!!
val AttributesComponent.body get() = this[AttributeType.BODY]
val AttributesComponent.mind get() = this[AttributeType.MIND]
val AttributesComponent.instinct get() = this[AttributeType.INSTINCT]
val AttributesComponent.luck get() = this[AttributeType.LUCK]
val AttributesComponent.maxHp get() = this[AttributeType.MAX_HP]
val AttributesComponent.baseDamage get() = this[AttributeType.BASE_DAMAGE]
val AttributesComponent.evasion get() = this[AttributeType.EVASION]
val AttributesComponent.defense get() = this[AttributeType.DEFENSE]
val AttributesComponent.skillMulti get() = this[AttributeType.SKILL_MULTI]
val AttributesComponent.critThreshold get() = this[AttributeType.CRIT_THRESHOLD]
val AttributesComponent.critBonus get() = this[AttributeType.CRIT_BONUS]
val AttributesComponent.attacksPerSecond get() = this[AttributeType.ATTACKS_PER_SECOND]
val AttributesComponent.cooldownReduction get() = this[AttributeType.COOLDOWN_REDUCTION]
val AttributesComponent.primary get() = filter { it.priority == AttributePriority.PRIMARY }
val AttributesComponent.secondary get() = filter { it.priority == AttributePriority.SECONDARY }