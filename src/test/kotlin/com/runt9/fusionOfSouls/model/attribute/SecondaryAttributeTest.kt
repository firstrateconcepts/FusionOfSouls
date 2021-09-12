package com.runt9.fusionOfSouls.model.attribute

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.runt9.fusionOfSouls.model.unit.attribute.PrimaryAttributes
import com.runt9.fusionOfSouls.model.unit.attribute.SecondaryAttributes
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SecondaryAttributeTest {
    private lateinit var primary: PrimaryAttributes
    private lateinit var secondary: SecondaryAttributes

    @BeforeEach
    fun setup() {
        primary = PrimaryAttributes()
        secondary = SecondaryAttributes(primary)
    }

    @Test
    fun `Test HP - Base`() {
        assertThat(secondary.maxHp.value).isEqualTo(150.0)
    }

    @Test
    fun `Test HP - Various Recalculations`() {
        primary.body.addModifier(flatModifier = 50.0, percentModifier = 25.0)
        primary.mind.addModifier(flatModifier = 25.0, percentModifier = 50.0)
        assertThat(secondary.maxHp.value).isEqualTo(281.25)

        secondary.maxHp.addModifier(flatModifier = 50.0, percentModifier = 25.0)
        assertThat(secondary.maxHp.value).isEqualTo(414.0625)
    }

    @Test
    fun `Test Damage - Base`() {
        assertThat(secondary.baseDamage.value).isEqualTo(50.0)
    }

    @Test
    fun `Test Damage - Various Recalculations`() {
        primary.body.addModifier(flatModifier = 50.0, percentModifier = 25.0)
        primary.instinct.addModifier(flatModifier = 25.0, percentModifier = 50.0)
        assertThat(secondary.baseDamage.value).isEqualTo(93.75)

        secondary.baseDamage.addModifier(flatModifier = 5.0, percentModifier = 50.0)
        assertThat(secondary.baseDamage.value).isEqualTo(148.125)
    }

    @Test
    fun `Test Skill Multiplier - Base`() {
        assertThat(secondary.skillMulti.value).isEqualTo(1.41)
    }

    @Test
    fun `Test Skill Multiplier - Various Recalculations`() {
        primary.mind.addModifier(flatModifier = 50.0, percentModifier = 25.0)
        primary.instinct.addModifier(flatModifier = 25.0, percentModifier = 50.0)
        assertThat(secondary.skillMulti.value).isEqualTo(1.94)

        secondary.skillMulti.addModifier(flatModifier = 0.25, percentModifier = 20.0)
        assertThat(secondary.skillMulti.value).isEqualTo(2.62)
    }

    @Test
    fun `Test Defense - Base`() {
        assertThat(secondary.defense.value).isEqualTo(19.84)
    }

    @Test
    fun `Test Defense - Various Recalculations`() {
        primary.body.addModifier(flatModifier = 50.0, percentModifier = 25.0)
        primary.luck.addModifier(flatModifier = 25.0, percentModifier = 50.0)
        assertThat(secondary.defense.value).isEqualTo(27.17)

        secondary.defense.addModifier(flatModifier = 5.0, percentModifier = 25.0)
        assertThat(secondary.defense.value).isEqualTo(40.21)
    }

    @Test
    fun `Test Evasion - Base`() {
        assertThat(secondary.evasion.value).isEqualTo(5.0)
    }

    @Test
    fun `Test Evasion - Various Recalculations`() {
        primary.instinct.addModifier(flatModifier = 50.0, percentModifier = 25.0)
        primary.luck.addModifier(flatModifier = 25.0, percentModifier = 50.0)
        assertThat(secondary.evasion.value).isEqualTo(9.38)

        secondary.evasion.addModifier(flatModifier = 5.0, percentModifier = 25.0)
        assertThat(secondary.evasion.value).isEqualTo(17.97)
    }

    @Test
    fun `Test Crit Threshold - Base`() {
        assertThat(secondary.critThreshold.value).isEqualTo(90.0)
    }

    @Test
    fun `Test Crit Threshold - Various Recalculations`() {
        primary.mind.addModifier(flatModifier = 50.0, percentModifier = 25.0)
        primary.luck.addModifier(flatModifier = 25.0, percentModifier = 50.0)
        assertThat(secondary.critThreshold.value).isEqualTo(81.25)

        secondary.critThreshold.addModifier(flatModifier = -5.0, percentModifier = -25.0)
        assertThat(secondary.critThreshold.value).isEqualTo(57.19)
    }

    @Test
    fun `Test Crit Bonus - Base`() {
        assertThat(secondary.critBonus.value).isEqualTo(1.41)
    }

    @Test
    fun `Test Crit Bonus - Various Recalculations`() {
        primary.body.addModifier(flatModifier = 50.0, percentModifier = 25.0)
        primary.luck.addModifier(flatModifier = 25.0, percentModifier = 50.0)
        assertThat(secondary.critBonus.value).isEqualTo(1.94)

        secondary.critBonus.addModifier(flatModifier = 0.25, percentModifier = 30.0)
        assertThat(secondary.critBonus.value).isEqualTo(2.84)
    }

    @Test
    fun `Test Attack Speed - Base`() {
        assertThat(secondary.attackSpeed.value).isEqualTo(0.5)
    }

    @Test
    fun `Test Attack Speed - Various Recalculations`() {
        primary.body.addModifier(flatModifier = 50.0, percentModifier = 25.0)
        primary.mind.addModifier(flatModifier = 25.0, percentModifier = 50.0)
        assertThat(secondary.attackSpeed.value).isEqualTo(0.94)

        secondary.attackSpeed.addModifier(flatModifier = 0.1, percentModifier = 25.0)
        assertThat(secondary.attackSpeed.value).isEqualTo(1.3)
    }

    @Test
    fun `Test Cooldown Reduction - Base`() {
        assertThat(secondary.cooldownReduction.value).isEqualTo(1.0)
    }

    @Test
    fun `Test Cooldown Reduction - Various Recalculations`() {
        primary.mind.addModifier(flatModifier = 50.0, percentModifier = 25.0)
        primary.instinct.addModifier(flatModifier = 25.0, percentModifier = 50.0)
        assertThat(secondary.cooldownReduction.value).isEqualTo(1.37)

        secondary.cooldownReduction.addModifier(flatModifier = 0.25, percentModifier = 30.0)
        assertThat(secondary.cooldownReduction.value).isEqualTo(2.11)
    }
}
