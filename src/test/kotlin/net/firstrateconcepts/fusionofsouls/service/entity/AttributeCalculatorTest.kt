package net.firstrateconcepts.fusionofsouls.service.entity

import assertk.assertThat
import assertk.assertions.isCloseTo
import assertk.assertions.isEqualTo
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import ktx.ashley.entity
import ktx.ashley.with
import net.firstrateconcepts.fusionofsouls.config.Injector
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
import net.firstrateconcepts.fusionofsouls.model.component.attrs
import net.firstrateconcepts.fusionofsouls.model.component.get
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.event.AttributeRecalculateNeededEvent
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.duringRun.AttributeCalculator
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunServiceRegistry
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class AttributeCalculatorTest {
    private lateinit var engine: AsyncPooledEngine
    private lateinit var attrCalculator: AttributeCalculator
    private lateinit var entity: Entity

    @BeforeEach
    fun setup() {
        Injector.bindSingleton(mockk<RunServiceRegistry>(relaxed = true))
        engine = AsyncPooledEngine(mockk(relaxed = true))
        attrCalculator = AttributeCalculator(engine, mockk(relaxed = true))
        Gdx.app = mockk(relaxed = true)

        entity = engine.entity {
            with<IdComponent>()
            with<AttributesComponent>()
            with<AttributeModifiersComponent>()
        }
    }

    @AfterEach
    fun tearDown() {
        Injector.clear()
    }

    private fun recalculate() = runBlocking { attrCalculator.handle(AttributeRecalculateNeededEvent(entity.id)).join() }
    private fun assertAttr(attr: AttributeType, value: Float) = assertThat(entity.attrs[attr]).transform { it() }.isCloseTo(value, 0.01f)

    companion object {
        @JvmStatic
        fun baseValueArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(BODY, 100f),
            Arguments.of(MIND, 100f),
            Arguments.of(INSTINCT, 100f),
            Arguments.of(LUCK, 100f),
            Arguments.of(MAX_HP, 150f),
            Arguments.of(BASE_DAMAGE, 50f),
            Arguments.of(SKILL_MULTI, 1.41f),
            Arguments.of(DEFENSE, 19.84f),
            Arguments.of(EVASION, 5f),
            Arguments.of(CRIT_THRESHOLD, 90f),
            Arguments.of(CRIT_BONUS, 1.41f),
            Arguments.of(ATTACKS_PER_SECOND, 0.5f),
            Arguments.of(COOLDOWN_REDUCTION, 1f)
        )

        @JvmStatic
        fun positiveFlatArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(BODY, 150f),
            Arguments.of(MIND, 100f),
            Arguments.of(INSTINCT, 100f),
            Arguments.of(LUCK, 100f),
            Arguments.of(MAX_HP, 200f),
            Arguments.of(BASE_DAMAGE, 62.5f),
            Arguments.of(SKILL_MULTI, 1.41f),
            Arguments.of(DEFENSE, 21.79f),
            Arguments.of(EVASION, 5f),
            Arguments.of(CRIT_THRESHOLD, 90f),
            Arguments.of(CRIT_BONUS, 1.5f),
            Arguments.of(ATTACKS_PER_SECOND, 0.63f),
            Arguments.of(COOLDOWN_REDUCTION, 1f)
        )

        @JvmStatic
        fun positivePercentArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(BODY, 133f),
            Arguments.of(MIND, 100f),
            Arguments.of(INSTINCT, 100f),
            Arguments.of(LUCK, 100f),
            Arguments.of(MAX_HP, 183f),
            Arguments.of(BASE_DAMAGE, 58.25f),
            Arguments.of(SKILL_MULTI, 1.41f),
            Arguments.of(DEFENSE, 21.17f),
            Arguments.of(EVASION, 5f),
            Arguments.of(CRIT_THRESHOLD, 90f),
            Arguments.of(CRIT_BONUS, 1.47f),
            Arguments.of(ATTACKS_PER_SECOND, 0.58f),
            Arguments.of(COOLDOWN_REDUCTION, 1f)
        )

        @JvmStatic
        fun positiveFlatAndPercentArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(BODY, 199.5f),
            Arguments.of(MIND, 100f),
            Arguments.of(INSTINCT, 100f),
            Arguments.of(LUCK, 100f),
            Arguments.of(MAX_HP, 249.50f),
            Arguments.of(BASE_DAMAGE, 74.88f),
            Arguments.of(SKILL_MULTI, 1.41f),
            Arguments.of(DEFENSE, 23.41f),
            Arguments.of(EVASION, 5f),
            Arguments.of(CRIT_THRESHOLD, 90f),
            Arguments.of(CRIT_BONUS, 1.58f),
            Arguments.of(ATTACKS_PER_SECOND, 0.75f),
            Arguments.of(COOLDOWN_REDUCTION, 1f)
        )

        @JvmStatic
        fun negativeFlatAndPercentArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(BODY, 33.5f),
            Arguments.of(MIND, 100f),
            Arguments.of(INSTINCT, 100f),
            Arguments.of(LUCK, 100f),
            Arguments.of(MAX_HP, 83.5f),
            Arguments.of(BASE_DAMAGE, 33.38f),
            Arguments.of(SKILL_MULTI, 1.41f),
            Arguments.of(DEFENSE, 16.19f),
            Arguments.of(EVASION, 5f),
            Arguments.of(CRIT_THRESHOLD, 90f),
            Arguments.of(CRIT_BONUS, 1.29f),
            Arguments.of(ATTACKS_PER_SECOND, 0.33f),
            Arguments.of(COOLDOWN_REDUCTION, 1f)
        )

        @JvmStatic
        fun mindArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(BODY, 100f),
            Arguments.of(MIND, 199.5f),
            Arguments.of(INSTINCT, 100f),
            Arguments.of(LUCK, 100f),
            Arguments.of(MAX_HP, 199.75f),
            Arguments.of(BASE_DAMAGE, 50f),
            Arguments.of(SKILL_MULTI, 1.73f),
            Arguments.of(DEFENSE, 19.84f),
            Arguments.of(EVASION, 5f),
            Arguments.of(CRIT_THRESHOLD, 87.51f),
            Arguments.of(CRIT_BONUS, 1.41f),
            Arguments.of(ATTACKS_PER_SECOND, 0.75f),
            Arguments.of(COOLDOWN_REDUCTION, 1.26f)
        )

        @JvmStatic
        fun instinctArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(BODY, 100f),
            Arguments.of(MIND, 100f),
            Arguments.of(INSTINCT, 199.5f),
            Arguments.of(LUCK, 100f),
            Arguments.of(MAX_HP, 150f),
            Arguments.of(BASE_DAMAGE, 74.88f),
            Arguments.of(SKILL_MULTI, 1.73f),
            Arguments.of(DEFENSE, 19.84f),
            Arguments.of(EVASION, 6f),
            Arguments.of(CRIT_THRESHOLD, 90f),
            Arguments.of(CRIT_BONUS, 1.41f),
            Arguments.of(ATTACKS_PER_SECOND, 0.5f),
            Arguments.of(COOLDOWN_REDUCTION, 1.18f)
        )

        @JvmStatic
        fun luckArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(BODY, 100f),
            Arguments.of(MIND, 100f),
            Arguments.of(INSTINCT, 100f),
            Arguments.of(LUCK, 199.5f),
            Arguments.of(MAX_HP, 150f),
            Arguments.of(BASE_DAMAGE, 50f),
            Arguments.of(SKILL_MULTI, 1.41f),
            Arguments.of(DEFENSE, 24.45f),
            Arguments.of(EVASION, 8.98f),
            Arguments.of(CRIT_THRESHOLD, 82.54f),
            Arguments.of(CRIT_BONUS, 1.87f),
            Arguments.of(ATTACKS_PER_SECOND, 0.5f),
            Arguments.of(COOLDOWN_REDUCTION, 1f)
        )

        @JvmStatic
        fun variousModsArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(BODY, 112.5f),
            Arguments.of(MIND, 247f),
            Arguments.of(INSTINCT, 253.75f),
            Arguments.of(LUCK, 165f),
            Arguments.of(MAX_HP, 715f),
            Arguments.of(BASE_DAMAGE, 115.13f),
            Arguments.of(SKILL_MULTI, 2.75f),
            Arguments.of(DEFENSE, 30.98f),
            Arguments.of(EVASION, 10.58f),
            Arguments.of(CRIT_THRESHOLD, 80.27f),
            Arguments.of(CRIT_BONUS, 2.3f),
            Arguments.of(ATTACKS_PER_SECOND, 1.8f),
            Arguments.of(COOLDOWN_REDUCTION, 1.41f)
        )

        @JvmStatic
        fun descriptionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(BODY, "Represents the physical prowess of this unit. Affects Max HP, Base Damage, Defense, Crit Bonus, Attacks / Second"),
            Arguments.of(MIND, "Represents the mental capacity of this unit. Affects Max HP, Skill Multiplier, Crit Threshold, Attacks / Second, Cooldown Reduction"),
            Arguments.of(INSTINCT, "Represents the innate focus and reactions of this unit. Affects Base Damage, Skill Multiplier, Evasion, Cooldown Reduction"),
            Arguments.of(LUCK, "Represents how much randomness favors this unit. Affects Defense, Evasion, Crit Threshold, Crit Bonus"),

            Arguments.of(MAX_HP, "How much damage the unit can take before it dies. Affected by Body and Mind"),
            Arguments.of(BASE_DAMAGE, "The base amount of damage done by attacks and skills. Reduced by enemy Defense. Affected by Body and Instinct"),
            Arguments.of(SKILL_MULTI, "Skill damage is multiplied by this amount. Affected by Mind and Instinct"),
            Arguments.of(DEFENSE, "Incoming damage is reduced by this percentage. Affected by Body and Luck"),
            Arguments.of(EVASION, "Reduces enemy attack rolls by a flat amount, potentially causing them to miss. Affected by Instinct and Luck"),
            Arguments.of(CRIT_THRESHOLD, "Attack rolls over this amount are crits. Attack rolls proportional to this add or reduce potential damage. Affected by Mind and Luck"),
            Arguments.of(CRIT_BONUS, "Critical hits multiply their damage by this amount. Affected by Body and Luck"),
            Arguments.of(ATTACKS_PER_SECOND, "How many times per second this unit will attack. Affected by Body and Mind"),
            Arguments.of(COOLDOWN_REDUCTION, "This unit's skill cooldown is divided by this amount. Affected by Mind and Instinct")
        )
    }

    @ParameterizedTest
    @MethodSource("baseValueArgs")
    fun `Test no modifiers is base value`(attr: AttributeType, value: Float) {
        recalculate()
        assertAttr(attr, value)
    }

    @ParameterizedTest
    @MethodSource("positiveFlatArgs")
    fun `Test Positive flat modifier`(attr: AttributeType, value: Float) {
        entity.addModifier(BODY, flatModifier = 50f)
        recalculate()
        assertAttr(attr, value)
    }

    @ParameterizedTest
    @MethodSource("positivePercentArgs")
    fun `Test Positive percent modifier`(attr: AttributeType, value: Float) {
        entity.addModifier(BODY, percentModifier = 33f)
        recalculate()
        assertAttr(attr, value)
    }

    @ParameterizedTest
    @MethodSource("positiveFlatAndPercentArgs")
    fun `Test Positive flat + percent modifiers`(attr: AttributeType, value: Float) {
        entity.addModifier(BODY, flatModifier = 50f, percentModifier = 33f)
        recalculate()
        assertAttr(attr, value)
    }

    @ParameterizedTest
    @MethodSource("negativeFlatAndPercentArgs")
    fun `Test Negative flat + percent modifiers`(attr: AttributeType, value: Float) {
        entity.addModifier(BODY, flatModifier = -50f, percentModifier = -33f)
        recalculate()
        assertAttr(attr, value)
    }

    @ParameterizedTest
    @MethodSource("mindArgs")
    fun `Test Mind with modifiers`(attr: AttributeType, value: Float) {
        entity.addModifier(MIND, flatModifier = 50f, percentModifier = 33f)
        recalculate()
        assertAttr(attr, value)
    }

    @ParameterizedTest
    @MethodSource("instinctArgs")
    fun `Test Instinct with modifiers`(attr: AttributeType, value: Float) {
        entity.addModifier(INSTINCT, flatModifier = 50f, percentModifier = 33f)
        recalculate()
        assertAttr(attr, value)
    }

    @ParameterizedTest
    @MethodSource("luckArgs")
    fun `Test Luck with modifiers`(attr: AttributeType, value: Float) {
        entity.addModifier(LUCK, flatModifier = 50f, percentModifier = 33f)
        recalculate()
        assertAttr(attr, value)
    }

    @ParameterizedTest
    @MethodSource("variousModsArgs")
    fun `Test with various modifiers to everything`(attr: AttributeType, value: Float) {
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
        assertAttr(attr, value)
    }

    @ParameterizedTest
    @MethodSource("descriptionArgs")
    fun `Test attribute descriptions`(attr: AttributeType, value: String) {
        recalculate()
        assertThat(entity.attrs[attr].description).isEqualTo(value)
    }
}
