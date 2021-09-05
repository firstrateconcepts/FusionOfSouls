package com.runt9.fusionOfSouls.model.attribute

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.runt9.fusionOfSouls.model.unit.attribute.AttributeModifier
import com.runt9.fusionOfSouls.model.unit.attribute.PrimaryAttribute
import com.runt9.fusionOfSouls.model.unit.attribute.PrimaryAttributeType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PrimaryAttributeTest {
    private val tolerance = 0.000001

    @Test
    fun `Test No Modifiers is Base Value`() {
        val attr = PrimaryAttribute(PrimaryAttributeType.BODY)
        attr.recalculate()
        assertThat(attr.value).isEqualTo(100.0)
    }

    @Test
    fun `Test Flat Modifier - Positive`() {
        val attr = PrimaryAttribute(PrimaryAttributeType.BODY)
        attr.addModifier(AttributeModifier(10.0))
        assertThat(attr.value).isEqualTo(110.0)
    }

    @Test
    fun `Test Flat Modifier - Negative`() {
        val attr = PrimaryAttribute(PrimaryAttributeType.BODY)
        attr.addModifier(AttributeModifier(-10.0))
        assertEquals(90.0, attr.value)
        assertThat(attr.value).isEqualTo(90.0)
    }

    @Test
    fun `Test Multiple Flat Modifiers - Equal to Zero`() {
        val attr = PrimaryAttribute(PrimaryAttributeType.BODY)
        attr.addModifier(AttributeModifier(10.0))
        attr.addModifier(AttributeModifier(-10.0))
        assertThat(attr.value).isEqualTo(100.0)
    }

    @Test
    fun `Test Multiple Flat Modifiers - Both Positive`() {
        val attr = PrimaryAttribute(PrimaryAttributeType.BODY)
        attr.addModifier(AttributeModifier(10.0))
        attr.addModifier(AttributeModifier(15.0))
        assertThat(attr.value).isEqualTo(125.0)
    }

    @Test
    fun `Test Multiple Flat Modifiers - Both Negative`() {
        val attr = PrimaryAttribute(PrimaryAttributeType.BODY)
        attr.addModifier(AttributeModifier(-10.0))
        attr.addModifier(AttributeModifier(-15.0))
        assertThat(attr.value).isEqualTo(75.0)
    }

    @Test
    fun `Test Percent Modifier - Positive`() {
        val attr = PrimaryAttribute(PrimaryAttributeType.BODY)
        attr.addModifier(AttributeModifier(percentModifier = 10.0))
        assertThat(attr.value).isEqualTo(110.0)
    }

    @Test
    fun `Test Percent Modifier - Negative`() {
        val attr = PrimaryAttribute(PrimaryAttributeType.BODY)
        attr.addModifier(AttributeModifier(percentModifier = -10.0))
        assertThat(attr.value).isEqualTo(90.0)
    }

    @Test
    fun `Test Multiple Percent Modifiers - Equal to Zero`() {
        val attr = PrimaryAttribute(PrimaryAttributeType.BODY)
        attr.addModifier(AttributeModifier(percentModifier = 10.0))
        attr.addModifier(AttributeModifier(percentModifier = -10.0))
        assertThat(attr.value).isEqualTo(100.0)
    }

    @Test
    fun `Test Multiple Percent Modifiers - Both Positive`() {
        val attr = PrimaryAttribute(PrimaryAttributeType.BODY)
        attr.addModifier(AttributeModifier(percentModifier = 10.0))
        attr.addModifier(AttributeModifier(percentModifier = 15.0))
        assertThat(attr.value).isEqualTo(125.0)
    }

    @Test
    fun `Test Multiple Percent Modifiers - Both Negative`() {
        val attr = PrimaryAttribute(PrimaryAttributeType.BODY)
        attr.addModifier(AttributeModifier(percentModifier = -10.0))
        attr.addModifier(AttributeModifier(percentModifier = -15.0))
        assertThat(attr.value).isEqualTo(75.0)
    }

    @Test
    fun `Test Both Modifiers - Both Positive`() {
        val attr = PrimaryAttribute(PrimaryAttributeType.BODY)
        attr.addModifier(AttributeModifier(flatModifier = 10.0, percentModifier = 10.0))
        assertThat(attr.value).isEqualTo(121.0)
    }

    @Test
    fun `Test Both Modifiers - Both Negative`() {
        val attr = PrimaryAttribute(PrimaryAttributeType.BODY)
        attr.addModifier(AttributeModifier(flatModifier = -10.0, percentModifier = -10.0))
        assertThat(attr.value).isEqualTo(81.0)
    }
}
