package net.firstrateconcepts.fusionofsouls.model.attribute

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class AttributeModifierTest {
    data class ModifierTestArgs(val type: AttributeType, val flat: Float = 0f, val percent: Float = 0f, val output: String) : Arguments {
        override fun get(): Array<Any> = arrayOf(this)
    }

    companion object {
        @JvmStatic
        fun nameArgs(): Stream<Arguments> = Stream.of(
            ModifierTestArgs(AttributeType.BODY, flat = 5f, output = "+5 Body"),
            ModifierTestArgs(AttributeType.BODY, percent = 10f, output = "10% increased Body"),
            ModifierTestArgs(AttributeType.BODY, flat = -5f, output = "-5 Body"),
            ModifierTestArgs(AttributeType.BODY, percent = -10f, output = "10% reduced Body"),
            ModifierTestArgs(AttributeType.BODY, flat = 5f, percent = 10f, output = "+5 Body\n10% increased Body"),
            ModifierTestArgs(AttributeType.BODY, flat = -5f, percent = -10f, output = "-5 Body\n10% reduced Body"),
            ModifierTestArgs(AttributeType.MIND, flat = 5f, percent = 10f, output = "+5 Mind\n10% increased Mind"),
            ModifierTestArgs(AttributeType.MIND, flat = -5f, percent = -10f, output = "-5 Mind\n10% reduced Mind"),
            ModifierTestArgs(AttributeType.INSTINCT, flat = 5f, percent = 10f, output = "+5 Instinct\n10% increased Instinct"),
            ModifierTestArgs(AttributeType.INSTINCT, flat = -5f, percent = -10f, output = "-5 Instinct\n10% reduced Instinct"),
            ModifierTestArgs(AttributeType.LUCK, flat = 5f, percent = 10f, output = "+5 Luck\n10% increased Luck"),
            ModifierTestArgs(AttributeType.LUCK, flat = -5f, percent = -10f, output = "-5 Luck\n10% reduced Luck"),
            ModifierTestArgs(AttributeType.MAX_HP, flat = 5f, percent = 10f, output = "+5 Max HP\n10% increased Max HP"),
            ModifierTestArgs(AttributeType.MAX_HP, flat = -5f, percent = -10f, output = "-5 Max HP\n10% reduced Max HP"),
            ModifierTestArgs(AttributeType.BASE_DAMAGE, flat = 5f, percent = 10f, output = "+5 Base Damage\n10% increased Base Damage"),
            ModifierTestArgs(AttributeType.BASE_DAMAGE, flat = -5f, percent = -10f, output = "-5 Base Damage\n10% reduced Base Damage"),
            ModifierTestArgs(AttributeType.ABILITY_MULTI, flat = 5f, percent = 10f, output = "+5.00x Ability Multiplier\n10% increased Ability Multiplier"),
            ModifierTestArgs(AttributeType.ABILITY_MULTI, flat = -5f, percent = -10f, output = "-5.00x Ability Multiplier\n10% reduced Ability Multiplier"),
            ModifierTestArgs(AttributeType.DEFENSE, flat = 5f, percent = 10f, output = "+5.00 Defense\n10% increased Defense"),
            ModifierTestArgs(AttributeType.DEFENSE, flat = -5f, percent = -10f, output = "-5.00 Defense\n10% reduced Defense"),
            ModifierTestArgs(AttributeType.EVASION, flat = 5f, percent = 10f, output = "+5 Evasion\n10% increased Evasion"),
            ModifierTestArgs(AttributeType.EVASION, flat = -5f, percent = -10f, output = "-5 Evasion\n10% reduced Evasion"),
            ModifierTestArgs(AttributeType.ACCURACY, flat = 5f, percent = 10f, output = "+5 Accuracy\n10% increased Accuracy"),
            ModifierTestArgs(AttributeType.ACCURACY, flat = -5f, percent = -10f, output = "-5 Accuracy\n10% reduced Accuracy"),
            ModifierTestArgs(AttributeType.CRIT_MULTI, flat = 5f, percent = 10f, output = "+5.00x Crit Multiplier\n10% increased Crit Multiplier"),
            ModifierTestArgs(AttributeType.CRIT_MULTI, flat = -5f, percent = -10f, output = "-5.00x Crit Multiplier\n10% reduced Crit Multiplier"),
            ModifierTestArgs(AttributeType.ATTACK_SPEED, flat = 5f, percent = 10f, output = "+5.00 Attack Speed\n10% increased Attack Speed"),
            ModifierTestArgs(AttributeType.ATTACK_SPEED, flat = -5f, percent = -10f, output = "-5.00 Attack Speed\n10% reduced Attack Speed"),
            ModifierTestArgs(AttributeType.COOLDOWN_REDUCTION, flat = 5f, percent = 10f, output = "+5.00 Cooldown Reduction\n10% increased Cooldown Reduction"),
            ModifierTestArgs(AttributeType.COOLDOWN_REDUCTION, flat = -5f, percent = -10f, output = "-5.00 Cooldown Reduction\n10% reduced Cooldown Reduction"),
            ModifierTestArgs(AttributeType.ATTACK_RANGE, flat = 5f, percent = 10f, output = "+5 Attack Range\n10% increased Attack Range"),
            ModifierTestArgs(AttributeType.ATTACK_RANGE, flat = -5f, percent = -10f, output = "-5 Attack Range\n10% reduced Attack Range"),
            ModifierTestArgs(AttributeType.LIFESTEAL, flat = 5f, percent = 10f, output = "+5% Lifesteal\n10% increased Lifesteal"),
            ModifierTestArgs(AttributeType.LIFESTEAL, flat = -5f, percent = -10f, output = "-5% Lifesteal\n10% reduced Lifesteal"),
            ModifierTestArgs(AttributeType.HP_REGEN, flat = 5f, percent = 10f, output = "+5.00% HP Regen\n10% increased HP Regen"),
            ModifierTestArgs(AttributeType.HP_REGEN, flat = -5f, percent = -10f, output = "-5.00% HP Regen\n10% reduced HP Regen"),
        )
    }

    @ParameterizedTest
    @MethodSource("nameArgs")
    fun `Test Attribute Modifier Name`(args: ModifierTestArgs) {
        val modifier = AttributeModifier(args.type, args.flat, args.percent)
        assertThat(modifier.name).isEqualTo(args.output)
    }
}
