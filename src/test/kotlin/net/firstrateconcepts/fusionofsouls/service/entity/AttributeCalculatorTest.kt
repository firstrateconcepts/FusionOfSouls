package net.firstrateconcepts.fusionofsouls.service.entity

import assertk.Assert
import assertk.assertThat
import assertk.assertions.isCloseTo
import assertk.assertions.isEqualTo
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import ktx.ashley.entity
import ktx.ashley.with
import net.firstrateconcepts.fusionofsouls.model.attribute.Attribute
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.ATTACKS_PER_SECOND
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.BASE_DAMAGE
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.BODY
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.COOLDOWN_REDUCTION
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.CRIT_BONUS
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.CRIT_THRESHOLD
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.DEFENSE
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.EVASION
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.INSTINCT
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.LUCK
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.MAX_HP
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.MIND
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.SKILL_MULTI
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.description
import net.firstrateconcepts.fusionofsouls.model.component.AttributeModifiersComponent
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent
import net.firstrateconcepts.fusionofsouls.model.component.IdComponent
import net.firstrateconcepts.fusionofsouls.model.component.addModifier
import net.firstrateconcepts.fusionofsouls.model.component.attacksPerSecond
import net.firstrateconcepts.fusionofsouls.model.component.attrs
import net.firstrateconcepts.fusionofsouls.model.component.baseDamage
import net.firstrateconcepts.fusionofsouls.model.component.body
import net.firstrateconcepts.fusionofsouls.model.component.cooldownReduction
import net.firstrateconcepts.fusionofsouls.model.component.critBonus
import net.firstrateconcepts.fusionofsouls.model.component.critThreshold
import net.firstrateconcepts.fusionofsouls.model.component.defense
import net.firstrateconcepts.fusionofsouls.model.component.evasion
import net.firstrateconcepts.fusionofsouls.model.component.get
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.component.instinct
import net.firstrateconcepts.fusionofsouls.model.component.luck
import net.firstrateconcepts.fusionofsouls.model.component.maxHp
import net.firstrateconcepts.fusionofsouls.model.component.mind
import net.firstrateconcepts.fusionofsouls.model.component.skillMulti
import net.firstrateconcepts.fusionofsouls.model.event.AttributeRecalculateNeededEvent
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.duringRun.AttributeCalculator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AttributeCalculatorTest {
    private lateinit var engine: AsyncPooledEngine
    private lateinit var attrCalculator: AttributeCalculator
    private lateinit var entity: Entity

    @BeforeEach
    fun setup() {
        engine = AsyncPooledEngine(mockk(relaxed = true))
        attrCalculator = AttributeCalculator(engine, mockk(relaxed = true))
        Gdx.app = mockk(relaxed = true)

        entity = engine.entity {
            with<IdComponent>()
            with<AttributesComponent>()
            with<AttributeModifiersComponent>()
        }
    }

    private fun recalculate() = runBlocking { attrCalculator.handle(AttributeRecalculateNeededEvent(entity.id)) }
    private fun Assert<Attribute>.isEqualTo(value: Float) = this.transform { it() }.isCloseTo(value, 0.01f)
    private fun assertAttrs(vararg attrPairs: Pair<AttributeType, Float>) = attrPairs.forEach { assertThat(entity.attrs[it.first]).isEqualTo(it.second) }

    @Test
    fun `Test no modifiers is base value`() {
        recalculate()
        assertAttrs(
            BODY to 100f,
            MIND to 100f,
            INSTINCT to 100f,
            LUCK to 100f,
            MAX_HP to 150f,
            BASE_DAMAGE to 50f,
            SKILL_MULTI to 1.41f,
            DEFENSE to 19.84f,
            EVASION to 5f,
            CRIT_THRESHOLD to 90f,
            CRIT_BONUS to 1.41f,
            ATTACKS_PER_SECOND to 0.5f,
            COOLDOWN_REDUCTION to 1f
        )
    }

    @Test
    fun `Test Positive flat modifier`() {
        entity.addModifier(BODY, flatModifier = 50f)
        recalculate()
        assertAttrs(
            BODY to 150f,
            MIND to 100f,
            INSTINCT to 100f,
            LUCK to 100f,
            MAX_HP to 200f,
            BASE_DAMAGE to 62.5f,
            SKILL_MULTI to 1.41f,
            DEFENSE to 21.79f,
            EVASION to 5f,
            CRIT_THRESHOLD to 90f,
            CRIT_BONUS to 1.5f,
            ATTACKS_PER_SECOND to 0.63f,
            COOLDOWN_REDUCTION to 1f
        )
    }

    @Test
    fun `Test Positive percent modifier`() {
        entity.addModifier(BODY, percentModifier = 33f)
        recalculate()
        assertAttrs(
            BODY to 133f,
            MIND to 100f,
            INSTINCT to 100f,
            LUCK to 100f,
            MAX_HP to 183f,
            BASE_DAMAGE to 58.25f,
            SKILL_MULTI to 1.41f,
            DEFENSE to 21.17f,
            EVASION to 5f,
            CRIT_THRESHOLD to 90f,
            CRIT_BONUS to 1.47f,
            ATTACKS_PER_SECOND to 0.58f,
            COOLDOWN_REDUCTION to 1f
        )
    }

    @Test
    fun `Test Positive flat + percent modifiers`() {
        entity.addModifier(BODY, flatModifier = 50f, percentModifier = 33f)
        recalculate()
        assertAttrs(
            BODY to 199.5f,
            MIND to 100f,
            INSTINCT to 100f,
            LUCK to 100f,
            MAX_HP to 249.50f,
            BASE_DAMAGE to 74.88f,
            SKILL_MULTI to 1.41f,
            DEFENSE to 23.41f,
            EVASION to 5f,
            CRIT_THRESHOLD to 90f,
            CRIT_BONUS to 1.58f,
            ATTACKS_PER_SECOND to 0.75f,
            COOLDOWN_REDUCTION to 1f
        )
    }

    @Test
    fun `Test Negative flat + percent modifiers`() {
        entity.addModifier(BODY, flatModifier = -50f, percentModifier = -33f)
        recalculate()
        assertAttrs(
            BODY to 33.5f,
            MIND to 100f,
            INSTINCT to 100f,
            LUCK to 100f,
            MAX_HP to 83.5f,
            BASE_DAMAGE to 33.38f,
            SKILL_MULTI to 1.41f,
            DEFENSE to 16.19f,
            EVASION to 5f,
            CRIT_THRESHOLD to 90f,
            CRIT_BONUS to 1.29f,
            ATTACKS_PER_SECOND to 0.33f,
            COOLDOWN_REDUCTION to 1f
        )
    }

    @Test
    fun `Test Mind with modifiers`() {
        entity.addModifier(MIND, flatModifier = 50f, percentModifier = 33f)
        recalculate()
        assertAttrs(
            BODY to 100f,
            MIND to 199.5f,
            INSTINCT to 100f,
            LUCK to 100f,
            MAX_HP to 199.75f,
            BASE_DAMAGE to 50f,
            SKILL_MULTI to 1.73f,
            DEFENSE to 19.84f,
            EVASION to 5f,
            CRIT_THRESHOLD to 87.51f,
            CRIT_BONUS to 1.41f,
            ATTACKS_PER_SECOND to 0.75f,
            COOLDOWN_REDUCTION to 1.26f
        )
    }

    @Test
    fun `Test Instinct with modifiers`() {
        entity.addModifier(INSTINCT, flatModifier = 50f, percentModifier = 33f)
        recalculate()
        assertAttrs(
            BODY to 100f,
            MIND to 100f,
            INSTINCT to 199.5f,
            LUCK to 100f,
            MAX_HP to 150f,
            BASE_DAMAGE to 74.88f,
            SKILL_MULTI to 1.73f,
            DEFENSE to 19.84f,
            EVASION to 6f,
            CRIT_THRESHOLD to 90f,
            CRIT_BONUS to 1.41f,
            ATTACKS_PER_SECOND to 0.5f,
            COOLDOWN_REDUCTION to 1.18f
        )
    }

    @Test
    fun `Test Luck with modifiers`() {
        entity.addModifier(LUCK, flatModifier = 50f, percentModifier = 33f)
        recalculate()
        assertAttrs(
            BODY to 100f,
            MIND to 100f,
            INSTINCT to 100f,
            LUCK to 199.5f,
            MAX_HP to 150f,
            BASE_DAMAGE to 50f,
            SKILL_MULTI to 1.41f,
            DEFENSE to 24.45f,
            EVASION to 8.98f,
            CRIT_THRESHOLD to 82.54f,
            CRIT_BONUS to 1.87f,
            ATTACKS_PER_SECOND to 0.5f,
            COOLDOWN_REDUCTION to 1f
        )
    }

    @Test
    fun `Test with various modifiers to everything`() {
        entity.addModifier(BODY, flatModifier = 25f, percentModifier = -10f)
        entity.addModifier(MIND, flatModifier = -5f, percentModifier = 160f)
        entity.addModifier(INSTINCT, flatModifier = 75f, percentModifier = 45f)
        entity.addModifier(LUCK, flatModifier = 10f, percentModifier = 50f)
        entity.addModifier(MAX_HP, flatModifier = 50f, percentModifier = 150f)
        entity.addModifier(BASE_DAMAGE, flatModifier = -5f, percentModifier = 33f)
        entity.addModifier(SKILL_MULTI, flatModifier = 0.26f, percentModifier = 10f)
        entity.addModifier(DEFENSE, flatModifier = 1.24f, percentModifier = 25f)
        entity.addModifier(EVASION, flatModifier = 2f, percentModifier = -5f)
        entity.addModifier(CRIT_THRESHOLD, flatModifier = -5f, percentModifier = 5f)
        entity.addModifier(CRIT_BONUS, flatModifier = -0.1f, percentModifier = 40f)
        entity.addModifier(ATTACKS_PER_SECOND, flatModifier = 0.075f, percentModifier = 85f)
        entity.addModifier(COOLDOWN_REDUCTION, flatModifier = -0.015f, percentModifier = -10f)

        recalculate()
        assertAttrs(
            BODY to 112.5f,
            MIND to 247f,
            INSTINCT to 253.75f,
            LUCK to 165f,
            MAX_HP to 715f,
            BASE_DAMAGE to 115.13f,
            SKILL_MULTI to 2.75f,
            DEFENSE to 30.98f,
            EVASION to 10.58f,
            CRIT_THRESHOLD to 80.27f,
            CRIT_BONUS to 2.3f,
            ATTACKS_PER_SECOND to 1.8f,
            COOLDOWN_REDUCTION to 1.41f
        )
    }

    @Test
    fun `Test attribute descriptions`() {
        recalculate()
        with(entity.attrs) {
            assertThat(body.description).isEqualTo("Represents the physical prowess of this unit. Affects Max HP, Base Damage, Defense, Crit Bonus, Attacks / Second")
            assertThat(mind.description).isEqualTo("Represents the mental capacity of this unit. Affects Max HP, Skill Multiplier, Crit Threshold, Attacks / Second, Cooldown Reduction")
            assertThat(instinct.description).isEqualTo("Represents the innate focus and reactions of this unit. Affects Base Damage, Skill Multiplier, Evasion, Cooldown Reduction")
            assertThat(luck.description).isEqualTo("Represents how much randomness favors this unit. Affects Defense, Evasion, Crit Threshold, Crit Bonus")

            assertThat(maxHp.description).isEqualTo("How much damage the unit can take before it dies. Affected by Body and Mind")
            assertThat(baseDamage.description).isEqualTo("The base amount of damage done by attacks and skills. Reduced by enemy Defense. Affected by Body and Instinct")
            assertThat(skillMulti.description).isEqualTo("Skill damage is multiplied by this amount. Affected by Mind and Instinct")
            assertThat(defense.description).isEqualTo("Incoming damage is reduced by this percentage. Affected by Body and Luck")
            assertThat(evasion.description).isEqualTo("Reduces enemy attack rolls by a flat amount, potentially causing them to miss. Affected by Instinct and Luck")
            assertThat(critThreshold.description).isEqualTo("Attack rolls over this amount are crits. Attack rolls proportional to this add or reduce potential damage. Affected by Mind and Luck")
            assertThat(critBonus.description).isEqualTo("Critical hits multiply their damage by this amount. Affected by Body and Luck")
            assertThat(attacksPerSecond.description).isEqualTo("How many times per second this unit will attack. Affected by Body and Mind")
            assertThat(cooldownReduction.description).isEqualTo("This unit's skill cooldown is divided by this amount. Affected by Mind and Instinct")
        }
    }
}
