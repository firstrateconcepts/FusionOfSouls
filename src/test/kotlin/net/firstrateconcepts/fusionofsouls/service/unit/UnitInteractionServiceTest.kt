package net.firstrateconcepts.fusionofsouls.service.unit

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.badlogic.ashley.core.Entity
import io.mockk.every
import io.mockk.impl.annotations.OverrideMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
import net.firstrateconcepts.fusionofsouls.model.component.AttributesComponent
import net.firstrateconcepts.fusionofsouls.model.component.attrs
import net.firstrateconcepts.fusionofsouls.model.component.baseDamage
import net.firstrateconcepts.fusionofsouls.model.component.critBonus
import net.firstrateconcepts.fusionofsouls.model.component.defense
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.component.interceptAsTarget
import net.firstrateconcepts.fusionofsouls.model.component.interceptAsUnit
import net.firstrateconcepts.fusionofsouls.model.unit.DamageRequest
import net.firstrateconcepts.fusionofsouls.model.unit.HitCheck
import net.firstrateconcepts.fusionofsouls.model.unit.HitResult
import net.firstrateconcepts.fusionofsouls.model.unit.InterceptorScope
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.service.duringRun.RandomizerService
import net.firstrateconcepts.fusionofsouls.service.duringRun.RunServiceRegistry
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@ExtendWith(MockKExtension::class)
class UnitInteractionServiceTest {
    @RelaxedMockK lateinit var eventBus: EventBus
    @RelaxedMockK lateinit var registry: RunServiceRegistry
    @RelaxedMockK lateinit var randomizer: RandomizerService
    @RelaxedMockK lateinit var engine: AsyncPooledEngine
    @RelaxedMockK lateinit var unitManager: UnitManager
    @OverrideMockKs lateinit var service: UnitInteractionService

    data class HitCheckArgs(
        val rawRoll: Int = 50,
        val attackBonus: Float = 10f,
        val evasion: Float = 10f,
        val finalRoll: Int = 50,
        val damageScale: Float = 1f,
        val isHit: Boolean = true,
        val isCrit: Boolean = false
    ) : Arguments {
        override fun get(): Array<Any> = arrayOf(this)
        val hitCheck get() = HitCheck(rawRoll, attackBonus, evasion)
    }

    data class DamageArgs(
        val rawRoll: Int = 50,
        val finalRoll: Int = 50,
        val damageScale: Float = 1f,
        val baseDamage: Float = 100f,
        val extraDamage: Float = 0f,
        val extraMultiplier: Float = 1.0f,
        val critBonus: Float = 1.5f,
        val defense: Float = 1f,
        val rawDamage: Int = baseDamage.toInt(),
        val finalDamage: Int = rawDamage
    ) : Arguments {
        override fun get(): Array<Any> = arrayOf(this)
        val damageRequest get() = DamageRequest(HitResult(rawRoll, finalRoll, damageScale)).apply {
            addBaseDamage(extraDamage)
            addDamageMultiplier(extraMultiplier)
        }
    }

    companion object {
        @JvmStatic
        fun hitCheckArgs(): Stream<Arguments> = Stream.of(
            HitCheckArgs(),
            HitCheckArgs(rawRoll = 0, finalRoll = 0, damageScale = 0.75f),
            HitCheckArgs(rawRoll = 100, finalRoll = 100, damageScale = 1.25f),
            HitCheckArgs(rawRoll = 100, attackBonus = 15f, finalRoll = 105, damageScale = 1.25f, isCrit = true),
            HitCheckArgs(rawRoll = 0, attackBonus = 5f, finalRoll = -5, damageScale = 0.75f, isHit = false)
        )

        @JvmStatic
        fun damageArgs(): Stream<Arguments> = Stream.of(
            DamageArgs(),
            DamageArgs(finalRoll = 105, damageScale = 1.25f, rawDamage = 188),
            DamageArgs(defense = 2f, finalDamage = 50),
            DamageArgs(baseDamage = 200f),
            DamageArgs(damageScale = 0.75f, rawDamage = 75),
            DamageArgs(damageScale = 0.75f, defense = 2f, rawDamage = 75, finalDamage = 38),
            DamageArgs(extraDamage = 10f, rawDamage = 110),
            DamageArgs(extraMultiplier = 1.25f, rawDamage = 125)
        )
    }

    @ParameterizedTest
    @MethodSource("hitCheckArgs")
    fun `Test Hit Check`(hitCheck: HitCheckArgs) {
        mockkStatic(Entity::interceptAsUnit)
        mockkStatic(Entity::interceptAsTarget)

        val unit = mockk<Entity>(relaxed = true) { justRun { interceptAsUnit(any(), any(), any(), any()) } }
        val target = mockk<Entity>(relaxed = true) { justRun { interceptAsTarget(any(), any(), any(), any()) } }

        hitCheck.apply {
            val result = service.hitCheck(InterceptorScope.BOTH, unit, target, this.hitCheck)
            assertThat(result.rawRoll).isEqualTo(hitCheck.rawRoll)
            assertThat(result.finalRoll).isEqualTo(finalRoll)
            assertThat(result.damageScale).isEqualTo(damageScale)
            assertThat(result.isHit).isEqualTo(isHit)
            assertThat(result.isCrit).isEqualTo(isCrit)
        }
    }

    @ParameterizedTest
    @MethodSource("damageArgs")
    fun `Test Damage`(damageArgs: DamageArgs) {
        mockkStatic(Entity::interceptAsUnit)
        mockkStatic(Entity::interceptAsTarget)
        mockkStatic(Entity::attrs)
        mockkStatic(Entity::id)

        val attrs = AttributesComponent()
        attrs.baseDamage(damageArgs.baseDamage)
        attrs.critBonus(damageArgs.critBonus)
        attrs.defense(damageArgs.defense)

        val unit = mockk<Entity>(relaxed = true) {
            justRun { interceptAsUnit(any(), any(), any(), any()) }
            every { this@mockk.attrs } returns attrs
            every { this@mockk.id } returns 0
        }

        val target = mockk<Entity>(relaxed = true) {
            justRun { interceptAsTarget(any(), any(), any(), any()) }
            every { this@mockk.attrs } returns attrs
            every { this@mockk.id } returns 1
        }

        val result = service.damage(InterceptorScope.BOTH, unit, target, damageArgs.damageRequest)
        assertThat(result.rawDamage).isEqualTo(damageArgs.rawDamage)
        assertThat(result.finalDamage).isEqualTo(damageArgs.finalDamage)
    }
}
