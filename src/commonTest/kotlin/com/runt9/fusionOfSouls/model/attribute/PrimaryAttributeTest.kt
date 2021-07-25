package com.runt9.fusionOfSouls.model.attribute

import com.runt9.fusionOfSouls.model.unit.attribute.AttributeModifier
import com.runt9.fusionOfSouls.model.unit.attribute.PrimaryAttribute
import kotlin.test.Test
import kotlin.test.assertEquals

class PrimaryAttributeTest {
    private val tolerance = 0.000001

    @Test
    fun `Test No Modifiers is Base Value`() {
        assertEquals(100.0, PrimaryAttribute().value)
    }

    @Test
    fun `Test Flat Modifier - Positive`() {
        val attr = PrimaryAttribute()
        attr.addModifier(AttributeModifier(10.0))
        assertEquals(110.0, attr.value)
    }

    @Test
    fun `Test Flat Modifier - Negative`() {
        val attr = PrimaryAttribute()
        attr.addModifier(AttributeModifier(-10.0))
        assertEquals(90.0, attr.value)
    }

    @Test
    fun `Test Multiple Flat Modifiers - Equal to Zero`() {
        val attr = PrimaryAttribute()
        attr.addModifier(AttributeModifier(10.0))
        attr.addModifier(AttributeModifier(-10.0))
        assertEquals(100.0, attr.value)
    }

    @Test
    fun `Test Multiple Flat Modifiers - Both Positive`() {
        val attr = PrimaryAttribute()
        attr.addModifier(AttributeModifier(10.0))
        attr.addModifier(AttributeModifier(15.0))
        assertEquals(125.0, attr.value)
    }

    @Test
    fun `Test Multiple Flat Modifiers - Both Negative`() {
        val attr = PrimaryAttribute()
        attr.addModifier(AttributeModifier(-10.0))
        attr.addModifier(AttributeModifier(-15.0))
        assertEquals(75.0, attr.value)
    }

    @Test
    fun `Test Percent Modifier - Positive`() {
        val attr = PrimaryAttribute()
        attr.addModifier(AttributeModifier(percentModifier = 0.1))
        assertEquals(110.0, attr.value, tolerance)
    }

    @Test
    fun `Test Percent Modifier - Negative`() {
        val attr = PrimaryAttribute()
        attr.addModifier(AttributeModifier(percentModifier = -0.1))
        assertEquals(90.0, attr.value, tolerance)
    }

    @Test
    fun `Test Multiple Percent Modifiers - Equal to Zero`() {
        val attr = PrimaryAttribute()
        attr.addModifier(AttributeModifier(percentModifier = 0.1))
        attr.addModifier(AttributeModifier(percentModifier = -0.1))
        assertEquals(100.0, attr.value)
    }

    @Test
    fun `Test Multiple Percent Modifiers - Both Positive`() {
        val attr = PrimaryAttribute()
        attr.addModifier(AttributeModifier(percentModifier = 0.1))
        attr.addModifier(AttributeModifier(percentModifier = 0.15))
        assertEquals(125.0, attr.value)
    }

    @Test
    fun `Test Multiple Percent Modifiers - Both Negative`() {
        val attr = PrimaryAttribute()
        attr.addModifier(AttributeModifier(percentModifier = -0.1))
        attr.addModifier(AttributeModifier(percentModifier = -0.15))
        assertEquals(75.0, attr.value)
    }

    @Test
    fun `Test Both Modifiers - Both Positive`() {
        val attr = PrimaryAttribute()
        attr.addModifier(AttributeModifier(flatModifier = 10.0, percentModifier = 0.1))
        assertEquals(121.0, attr.value, tolerance)
    }

    @Test
    fun `Test Both Modifiers - Both Negative`() {
        val attr = PrimaryAttribute()
        attr.addModifier(AttributeModifier(flatModifier = -10.0, percentModifier = -0.1))
        assertEquals(81.0, attr.value)
    }
}
