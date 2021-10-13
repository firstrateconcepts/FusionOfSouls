package net.firstrateconcepts.fusionofsouls.service.unit

import assertk.assertThat
import assertk.assertions.isCloseTo
import assertk.assertions.isEqualTo
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import io.mockk.mockk
import ktx.ashley.entity
import ktx.ashley.with
import net.firstrateconcepts.fusionofsouls.config.Injector
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.ABILITY_MULTI
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.ATTACK_BONUS
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.ATTACK_SPEED
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.BASE_DAMAGE
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.BODY
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.COOLDOWN_REDUCTION
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.CRIT_MULTI
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.DEFENSE
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.EVASION
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.INSTINCT
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.LUCK
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.MAX_HP
import net.firstrateconcepts.fusionofsouls.model.attribute.AttributeType.MIND
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.description
import net.firstrateconcepts.fusionofsouls.model.component.AttributeModifiersComponent
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent
import net.firstrateconcepts.fusionofsouls.model.component.IdComponent
import net.firstrateconcepts.fusionofsouls.model.component.addModifier
import net.firstrateconcepts.fusionofsouls.model.component.attrs
import net.firstrateconcepts.fusionofsouls.model.component.get
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
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
        attrCalculator = AttributeCalculator(mockk(relaxed = true))
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

    private fun recalculate() = attrCalculator.recalculate(entity)
    private fun assertAttr(attr: AttributeType, value: Float) = assertThat(entity.attrs[attr]).transform { it() }.isCloseTo(value, 0.01f)

    companion object {
        @JvmStatic
        fun baseValueArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(BODY, 100f),
            Arguments.of(MIND, 100f),
            Arguments.of(INSTINCT, 100f),
            Arguments.of(LUCK, 100f),
            Arguments.of(MAX_HP, 1000f),
            Arguments.of(BASE_DAMAGE, 100f),
            Arguments.of(ABILITY_MULTI, 1.5f),
            Arguments.of(DEFENSE, 1f),
            Arguments.of(EVASION, 5f),
            Arguments.of(ATTACK_BONUS, 5f),
            Arguments.of(CRIT_MULTI, 1.5f),
            Arguments.of(ATTACK_SPEED, 0.5f),
            Arguments.of(COOLDOWN_REDUCTION, 1f)
        )

        @JvmStatic
        fun positiveFlatArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(BODY, 150f),
            Arguments.of(MIND, 100f),
            Arguments.of(INSTINCT, 100f),
            Arguments.of(LUCK, 100f),
            Arguments.of(MAX_HP, 1375f),
            Arguments.of(BASE_DAMAGE, 125f),
            Arguments.of(ABILITY_MULTI, 1.5f),
            Arguments.of(DEFENSE, 1.13f),
            Arguments.of(EVASION, 5f),
            Arguments.of(ATTACK_BONUS, 5f),
            Arguments.of(CRIT_MULTI, 1.62f),
            Arguments.of(ATTACK_SPEED, 0.63f),
            Arguments.of(COOLDOWN_REDUCTION, 1f)
        )

        @JvmStatic
        fun positivePercentArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(BODY, 133f),
            Arguments.of(MIND, 100f),
            Arguments.of(INSTINCT, 100f),
            Arguments.of(LUCK, 100f),
            Arguments.of(MAX_HP, 1247.5f),
            Arguments.of(BASE_DAMAGE, 116.50f),
            Arguments.of(ABILITY_MULTI, 1.5f),
            Arguments.of(DEFENSE, 1.09f),
            Arguments.of(EVASION, 5f),
            Arguments.of(ATTACK_BONUS, 5f),
            Arguments.of(CRIT_MULTI, 1.59f),
            Arguments.of(ATTACK_SPEED, 0.58f),
            Arguments.of(COOLDOWN_REDUCTION, 1f)
        )

        @JvmStatic
        fun positiveFlatAndPercentArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(BODY, 199.5f),
            Arguments.of(MIND, 100f),
            Arguments.of(INSTINCT, 100f),
            Arguments.of(LUCK, 100f),
            Arguments.of(MAX_HP, 1746.25f),
            Arguments.of(BASE_DAMAGE, 149.75f),
            Arguments.of(ABILITY_MULTI, 1.5f),
            Arguments.of(DEFENSE, 1.19f),
            Arguments.of(EVASION, 5f),
            Arguments.of(ATTACK_BONUS, 5f),
            Arguments.of(CRIT_MULTI, 1.69f),
            Arguments.of(ATTACK_SPEED, 0.75f),
            Arguments.of(COOLDOWN_REDUCTION, 1f)
        )

        @JvmStatic
        fun negativeFlatAndPercentArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(BODY, 33.5f),
            Arguments.of(MIND, 100f),
            Arguments.of(INSTINCT, 100f),
            Arguments.of(LUCK, 100f),
            Arguments.of(MAX_HP, 501.25f),
            Arguments.of(BASE_DAMAGE, 66.75f),
            Arguments.of(ABILITY_MULTI, 1.5f),
            Arguments.of(DEFENSE, 0.26f),
            Arguments.of(EVASION, 5f),
            Arguments.of(ATTACK_BONUS, 5f),
            Arguments.of(CRIT_MULTI, 0.76f),
            Arguments.of(ATTACK_SPEED, 0.33f),
            Arguments.of(COOLDOWN_REDUCTION, 1f)
        )

        @JvmStatic
        fun mindArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(BODY, 100f),
            Arguments.of(MIND, 199.5f),
            Arguments.of(INSTINCT, 100f),
            Arguments.of(LUCK, 100f),
            Arguments.of(MAX_HP, 1248.75f),
            Arguments.of(BASE_DAMAGE, 100f),
            Arguments.of(ABILITY_MULTI, 1.87f),
            Arguments.of(DEFENSE, 1f),
            Arguments.of(EVASION, 5f),
            Arguments.of(ATTACK_BONUS, 6.99f),
            Arguments.of(CRIT_MULTI, 1.5f),
            Arguments.of(ATTACK_SPEED, 0.75f),
            Arguments.of(COOLDOWN_REDUCTION, 1.25f)
        )

        @JvmStatic
        fun instinctArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(BODY, 100f),
            Arguments.of(MIND, 100f),
            Arguments.of(INSTINCT, 199.5f),
            Arguments.of(LUCK, 100f),
            Arguments.of(MAX_HP, 1000f),
            Arguments.of(BASE_DAMAGE, 149.75f),
            Arguments.of(ABILITY_MULTI, 1.87f),
            Arguments.of(DEFENSE, 1f),
            Arguments.of(EVASION, 6f),
            Arguments.of(ATTACK_BONUS, 5f),
            Arguments.of(CRIT_MULTI, 1.5f),
            Arguments.of(ATTACK_SPEED, 0.5f),
            Arguments.of(COOLDOWN_REDUCTION, 1.25f)
        )

        @JvmStatic
        fun luckArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(BODY, 100f),
            Arguments.of(MIND, 100f),
            Arguments.of(INSTINCT, 100f),
            Arguments.of(LUCK, 199.5f),
            Arguments.of(MAX_HP, 1000f),
            Arguments.of(BASE_DAMAGE, 100f),
            Arguments.of(ABILITY_MULTI, 1.5f),
            Arguments.of(DEFENSE, 1.31f),
            Arguments.of(EVASION, 8.98f),
            Arguments.of(ATTACK_BONUS, 7.99f),
            Arguments.of(CRIT_MULTI, 2.06f),
            Arguments.of(ATTACK_SPEED, 0.5f),
            Arguments.of(COOLDOWN_REDUCTION, 1f)
        )

        @JvmStatic
        fun variousModsArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(BODY, 112.5f),
            Arguments.of(MIND, 247f),
            Arguments.of(INSTINCT, 253.75f),
            Arguments.of(LUCK, 165f),
            Arguments.of(MAX_HP, 4403.13f),
            Arguments.of(BASE_DAMAGE, 230.26f),
            Arguments.of(ABILITY_MULTI, 2.93f),
            Arguments.of(DEFENSE, 3.16f),
            Arguments.of(EVASION, 10.58f),
            Arguments.of(ATTACK_BONUS, 29.84f),
            Arguments.of(CRIT_MULTI, 2.64f),
            Arguments.of(ATTACK_SPEED, 1.8f),
            Arguments.of(COOLDOWN_REDUCTION, 1.43f)
        )

        @JvmStatic
        fun descriptionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(BODY, "Represents the physical prowess of this unit. Affects Max HP, Base Damage, Defense, Crit Multiplier, Attack Speed"),
            Arguments.of(MIND, "Represents the mental capacity of this unit. Affects Max HP, Ability Multiplier, Attack Bonus, Attack Speed, Cooldown Reduction"),
            Arguments.of(INSTINCT, "Represents the innate focus and reactions of this unit. Affects Base Damage, Ability Multiplier, Evasion, Cooldown Reduction"),
            Arguments.of(LUCK, "Represents how much randomness favors this unit. Affects Defense, Evasion, Attack Bonus, Crit Multiplier"),

            Arguments.of(MAX_HP, "How much damage the unit can take before it dies. Affected by Body and Mind"),
            Arguments.of(BASE_DAMAGE, "The base amount of damage done by attacks and abilities. Reduced by enemy Defense. Affected by Body and Instinct"),
            Arguments.of(ABILITY_MULTI, "Ability damage is multiplied by this amount. Affected by Mind and Instinct"),
            Arguments.of(DEFENSE, "Incoming damage divided by this amount. Affected by Body and Luck"),
            Arguments.of(EVASION, "Reduces enemy attack rolls by a flat amount, countered by Attack Bonus. Affected by Instinct and Luck"),
            Arguments.of(ATTACK_BONUS, "Flat amount added to attack rolls, countered by Evasion. Affected by Mind and Luck"),
            Arguments.of(CRIT_MULTI, "Critical hits multiply their damage by this amount. Affected by Body and Luck"),
            Arguments.of(ATTACK_SPEED, "How many times per second this unit will attack. Affected by Body and Mind"),
            Arguments.of(COOLDOWN_REDUCTION, "This unit's ability cooldown is divided by this amount. Affected by Mind and Instinct")
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
        entity.addModifier(MAX_HP, flatModifier = 300f, percentModifier = 150f)
        entity.addModifier(BASE_DAMAGE, flatModifier = -10f, percentModifier = 33f)
        entity.addModifier(ABILITY_MULTI, flatModifier = 0.26f, percentModifier = 10f)
        entity.addModifier(DEFENSE, flatModifier = 1.24f, percentModifier = 25f)
        entity.addModifier(EVASION, flatModifier = 2f, percentModifier = -5f)
        entity.addModifier(ATTACK_BONUS, flatModifier = 10f, percentModifier = 50f)
        entity.addModifier(CRIT_MULTI, flatModifier = -0.1f, percentModifier = 40f)
        entity.addModifier(ATTACK_SPEED, flatModifier = 0.075f, percentModifier = 85f)
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
