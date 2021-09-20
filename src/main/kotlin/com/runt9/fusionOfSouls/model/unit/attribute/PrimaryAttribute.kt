package com.runt9.fusionOfSouls.model.unit.attribute

import com.runt9.fusionOfSouls.model.unit.GameUnit
import com.soywiz.kmem.toIntRound

const val baseValue = 100.0

enum class PrimaryAttributeType(override val attrsAttrSelection: PrimaryAttributes.() -> PrimaryAttribute) : AttributeType<PrimaryAttribute, PrimaryAttributes> {
    BODY(PrimaryAttributes::body),
    MIND(PrimaryAttributes::mind),
    INSTINCT(PrimaryAttributes::instinct),
    LUCK(PrimaryAttributes::luck);

    override val unitAttrSelection = GameUnit::primaryAttrs
    override val attrRandomizer = AttributeModifierRandomizer(5, 10)
    override val displayName: String
        get() = name.lowercase().replaceFirstChar(Character::toTitleCase)
    override val shortName: String
        get() = displayName
    override val valueDisplayer = { d: Double -> d.toIntRound().toString() }
}

class PrimaryAttribute(type: PrimaryAttributeType) : Attribute(type) {
    override fun getBase() = baseValue
}

class PrimaryAttributes : Attributes<PrimaryAttribute> {
    val body = PrimaryAttribute(PrimaryAttributeType.BODY)
    val mind = PrimaryAttribute(PrimaryAttributeType.MIND)
    val instinct = PrimaryAttribute(PrimaryAttributeType.INSTINCT)
    val luck = PrimaryAttribute(PrimaryAttributeType.LUCK)
    override val all = listOf(body, mind, instinct, luck)

    init {
        all.forEach(Attribute::recalculate)
    }
}
