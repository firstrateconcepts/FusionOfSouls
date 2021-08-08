package com.runt9.fusionOfSouls.model.attribute

import com.runt9.fusionOfSouls.model.unit.attribute.PrimaryAttributes
import com.runt9.fusionOfSouls.model.unit.attribute.SecondaryAttributes
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SecondaryAttributeTest {
    private lateinit var primary: PrimaryAttributes
    private lateinit var secondary: SecondaryAttributes

    @BeforeTest
    fun setup() {
        primary = PrimaryAttributes()
        secondary = SecondaryAttributes(primary)
    }

    @Test
    fun `Test HP - Base`() {
        assertEquals(150.0, secondary.maxHp.value)
    }

    @Test
    fun `Test HP - Various Recalculations`() {
        primary.body.addModifier(flatModifier = 50.0, percentModifier = 0.25)
        primary.mind.addModifier(flatModifier = 25.0, percentModifier = 0.5)
        assertEquals(281.25, secondary.maxHp.value)

        secondary.maxHp.addModifier(flatModifier = 50.0, percentModifier = 0.25)
        assertEquals(414.0625, secondary.maxHp.value)
    }

    @Test
    fun `Test Damage - Base`() {
        assertEquals(50.0, secondary.baseDamage.value)
    }

    @Test
    fun `Test Damage - Various Recalculations`() {
        primary.body.addModifier(flatModifier = 50.0, percentModifier = 0.25)
        primary.instinct.addModifier(flatModifier = 25.0, percentModifier = 0.5)
        assertEquals(93.75, secondary.baseDamage.value)

        secondary.baseDamage.addModifier(flatModifier = 5.0, percentModifier = 0.50)
        assertEquals(148.125, secondary.baseDamage.value)
    }

    @Test
    fun `Test Skill Multiplier - Base`() {
        assertEquals(1.41, secondary.skillMulti.value, 0.01)
    }

    @Test
    fun `Test Skill Multiplier - Various Recalculations`() {
        primary.mind.addModifier(flatModifier = 50.0, percentModifier = 0.25)
        primary.instinct.addModifier(flatModifier = 25.0, percentModifier = 0.5)
        assertEquals(1.94, secondary.skillMulti.value, 0.01)

        secondary.skillMulti.addModifier(flatModifier = 0.25, percentModifier = 0.2)
        assertEquals(2.62, secondary.skillMulti.value, 0.01)
    }

    @Test
    fun `Test Defense - Base`() {
        assertEquals(19.84, secondary.defense.value, 0.01)
    }

    @Test
    fun `Test Defense - Various Recalculations`() {
        primary.body.addModifier(flatModifier = 50.0, percentModifier = 0.25)
        primary.luck.addModifier(flatModifier = 25.0, percentModifier = 0.5)
        assertEquals(27.17, secondary.defense.value, 0.01)

        secondary.defense.addModifier(flatModifier = 5.0, percentModifier = 0.25)
        assertEquals(40.21, secondary.defense.value, 0.01)
    }

    @Test
    fun `Test Evasion - Base`() {
        assertEquals(5.0, secondary.evasion.value)
    }

    @Test
    fun `Test Evasion - Various Recalculations`() {
        primary.instinct.addModifier(flatModifier = 50.0, percentModifier = 0.25)
        primary.luck.addModifier(flatModifier = 25.0, percentModifier = 0.5)
        assertEquals(9.38, secondary.evasion.value, 0.01)

        secondary.evasion.addModifier(flatModifier = 5.0, percentModifier = 0.25)
        assertEquals(17.97, secondary.evasion.value, 0.01)
    }

    @Test
    fun `Test Crit Threshold - Base`() {
        assertEquals(90.0, secondary.critThreshold.value)
    }

    @Test
    fun `Test Crit Threshold - Various Recalculations`() {
        primary.mind.addModifier(flatModifier = 50.0, percentModifier = 0.25)
        primary.luck.addModifier(flatModifier = 25.0, percentModifier = 0.5)
        assertEquals(81.25, secondary.critThreshold.value, 0.01)

        secondary.critThreshold.addModifier(flatModifier = -5.0, percentModifier = -0.25)
        assertEquals(57.19, secondary.critThreshold.value, 0.01)
    }

    @Test
    fun `Test Crit Bonus - Base`() {
        assertEquals(1.41, secondary.critBonus.value, 0.01)
    }

    @Test
    fun `Test Crit Bonus - Various Recalculations`() {
        primary.body.addModifier(flatModifier = 50.0, percentModifier = 0.25)
        primary.luck.addModifier(flatModifier = 25.0, percentModifier = 0.5)
        assertEquals(1.94, secondary.critBonus.value, 0.01)

        secondary.critBonus.addModifier(flatModifier = 0.25, percentModifier = 0.3)
        assertEquals(2.84, secondary.critBonus.value, 0.01)
    }

    @Test
    fun `Test Attack Speed - Base`() {
        assertEquals(0.5, secondary.attackSpeed.value)
    }

    @Test
    fun `Test Attack Speed - Various Recalculations`() {
        primary.body.addModifier(flatModifier = 50.0, percentModifier = 0.25)
        primary.mind.addModifier(flatModifier = 25.0, percentModifier = 0.5)
        assertEquals(0.94, secondary.attackSpeed.value, 0.01)

        secondary.attackSpeed.addModifier(flatModifier = 0.1, percentModifier = 0.25)
        assertEquals(1.3, secondary.attackSpeed.value, 0.01)
    }

    @Test
    fun `Test Cooldown Reduction - Base`() {
        assertEquals(1.0, secondary.cooldownReduction.value)
    }

    @Test
    fun `Test Cooldown Reduction - Various Recalculations`() {
        primary.mind.addModifier(flatModifier = 50.0, percentModifier = 0.25)
        primary.instinct.addModifier(flatModifier = 25.0, percentModifier = 0.5)
        assertEquals(1.37, secondary.cooldownReduction.value, 0.01)

        secondary.cooldownReduction.addModifier(flatModifier = 0.25, percentModifier = 0.3)
        assertEquals(2.11, secondary.cooldownReduction.value, 0.01)
    }
}
