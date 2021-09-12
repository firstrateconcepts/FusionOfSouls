package com.runt9.fusionOfSouls.model.attribute

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.runt9.fusionOfSouls.model.unit.GameUnit
import com.runt9.fusionOfSouls.model.unit.attribute.AttributeModifier
import com.runt9.fusionOfSouls.model.unit.attribute.AttributeModifierEffect
import com.runt9.fusionOfSouls.model.unit.attribute.PrimaryAttributeType
import com.runt9.fusionOfSouls.model.unit.attribute.SecondaryAttributeType
import com.runt9.fusionOfSouls.model.unit.skill.DefaultSkill
import com.runt9.fusionOfSouls.model.unit.unitClass.TankClass
import org.junit.jupiter.api.Test

// TODO: Test generated description
class AttributeModifierEffectTest {
    private fun testUnit() = GameUnit("test", Texture(Gdx.files.internal("blueArrow-tp.png")), DefaultSkill(), listOf(TankClass()))
    private fun testModifier() = AttributeModifier(flatModifier = 5.0)

    @Test
    fun `Test Attribute Modifier - Applies to Primary`() {
        val unit = testUnit()
        assertThat(unit.primaryAttrs.body.value).isEqualTo(100.0)
        AttributeModifierEffect(PrimaryAttributeType.BODY, testModifier()).applyToUnit(unit)
        assertThat(unit.primaryAttrs.body.value).isEqualTo(105.0)
    }

    @Test
    fun `Test Attribute Modifier - Applies to Secondary`() {
        val unit = testUnit()
        assertThat(unit.secondaryAttrs.maxHp.value).isEqualTo(150.0)
        AttributeModifierEffect(SecondaryAttributeType.MAX_HP, testModifier()).applyToUnit(unit)
        assertThat(unit.secondaryAttrs.maxHp.value).isEqualTo(155.0)
    }

    @Test
    fun `Test Attribute Modifier - Removes from Primary`() {
        val unit = testUnit()
        assertThat(unit.primaryAttrs.body.value).isEqualTo(100.0)
        val modifier = testModifier()
        unit.primaryAttrs.body.addModifier(modifier)
        assertThat(unit.primaryAttrs.body.value).isEqualTo(105.0)
        AttributeModifierEffect(PrimaryAttributeType.BODY, testModifier()).removeFromUnit(unit)
        assertThat(unit.primaryAttrs.body.value).isEqualTo(100.0)
    }

    @Test
    fun `Test Attribute Modifier - Removes from Secondary`() {
        val unit = testUnit()
        assertThat(unit.secondaryAttrs.maxHp.value).isEqualTo(150.0)
        val modifier = testModifier()
        unit.secondaryAttrs.maxHp.addModifier(modifier)
        assertThat(unit.secondaryAttrs.maxHp.value).isEqualTo(155.0)
        AttributeModifierEffect(SecondaryAttributeType.MAX_HP, testModifier()).removeFromUnit(unit)
        assertThat(unit.secondaryAttrs.maxHp.value).isEqualTo(150.0)
    }
}
