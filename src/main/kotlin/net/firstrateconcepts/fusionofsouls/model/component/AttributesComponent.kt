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
val Entity.tertiaryAttrs get() = this.attrs.tertiary

operator fun AttributesComponent.get(type: AttributeType): Attribute = attrs[type]!!
val AttributesComponent.body get() = this[AttributeType.BODY]
val AttributesComponent.mind get() = this[AttributeType.MIND]
val AttributesComponent.instinct get() = this[AttributeType.INSTINCT]
val AttributesComponent.luck get() = this[AttributeType.LUCK]
val AttributesComponent.maxHp get() = this[AttributeType.MAX_HP]
val AttributesComponent.baseDamage get() = this[AttributeType.BASE_DAMAGE]
val AttributesComponent.evasion get() = this[AttributeType.EVASION]
val AttributesComponent.defense get() = this[AttributeType.DEFENSE]
val AttributesComponent.abilityMulti get() = this[AttributeType.ABILITY_MULTI]
val AttributesComponent.attackBonus get() = this[AttributeType.ACCURACY]
val AttributesComponent.critBonus get() = this[AttributeType.CRIT_MULTI]
val AttributesComponent.attackSpeed get() = this[AttributeType.ATTACK_SPEED]
val AttributesComponent.cooldownReduction get() = this[AttributeType.COOLDOWN_REDUCTION]
val AttributesComponent.attackRange get() = this[AttributeType.ATTACK_RANGE]
val AttributesComponent.primary get() = filter { it.priority == AttributePriority.PRIMARY }
val AttributesComponent.secondary get() = filter { it.priority == AttributePriority.SECONDARY }
val AttributesComponent.tertiary get() = filter { it.priority == AttributePriority.TERTIARY }
