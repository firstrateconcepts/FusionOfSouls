package com.runt9.fusionOfSouls.model.attribute

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.badlogic.gdx.graphics.Texture
import com.runt9.fusionOfSouls.model.loot.DefaultPassive
import com.runt9.fusionOfSouls.model.unit.BasicUnit
import com.runt9.fusionOfSouls.model.unit.ability.DefaultAbility
import com.runt9.fusionOfSouls.model.unit.attribute.AttributeModifier
import com.runt9.fusionOfSouls.model.unit.attribute.AttributeModifierEffect
import com.runt9.fusionOfSouls.model.unit.attribute.PrimaryAttributeType
import com.runt9.fusionOfSouls.model.unit.attribute.SecondaryAttributeType
import com.runt9.fusionOfSouls.model.unit.unitClass.TankClass
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

class AttributeModifierEffectTest {
    private fun testUnit(): BasicUnit {
        val texture = mockk<Texture>() {
            every { width }.returns(10)
            every { height }.returns(10)
        }

        return BasicUnit(
            "test",
            texture,
            DefaultAbility(),
            DefaultPassive(),
            AttributeModifierEffect(PrimaryAttributeType.BODY, AttributeModifier(flatModifier = 0.0)),
            listOf(TankClass())
        )
    }

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
