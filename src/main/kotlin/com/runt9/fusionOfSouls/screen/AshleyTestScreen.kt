package com.runt9.fusionOfSouls.screen

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.runt9.fusionOfSouls.model.unit.attack.AttackRollResult
import com.runt9.fusionOfSouls.model.unit.attack.CritCheckResult
import com.runt9.fusionOfSouls.model.unit.attack.DamageCalcRequest
import com.runt9.fusionOfSouls.model.unit.attack.DamageCalcResult
import com.runt9.fusionOfSouls.view.BattleUnit
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import ktx.ashley.oneOf

class AshleyTestScreen(override val stage: Stage, val engine: PooledEngine) : FosScreen {
    override fun show() {
        val entity = TestEntity()
        engine.addEntity(entity)
        entity[unitHooksMapper]?.hooks?.add(object : UnitHook {
            override fun beforeAttack(attacker: BattleUnit, defender: BattleUnit, attackRoll: AttackRollResult, critResult: CritCheckResult, damageCalcRequest: DamageCalcRequest) {
                // do stuff
            }
        })

        engine.entities
    }
}

interface UnitHook {
    fun beforeAttack(attacker: BattleUnit, defender: BattleUnit, attackRoll: AttackRollResult, critResult: CritCheckResult, damageCalcRequest: DamageCalcRequest) {}
    fun whenHit(attacker: BattleUnit, defender: BattleUnit, attackRoll: AttackRollResult, critResult: CritCheckResult, damage: DamageCalcResult) {}
    fun onHit(attacker: BattleUnit, defender: BattleUnit, attackRoll: AttackRollResult, critResult: CritCheckResult, damage: DamageCalcResult) {}
}

class UnitHooksComponent : Component, UnitHook {
    val hooks = mutableListOf<UnitHook>()

    override fun beforeAttack(attacker: BattleUnit, defender: BattleUnit, attackRoll: AttackRollResult, critResult: CritCheckResult, damageCalcRequest: DamageCalcRequest) =
        hooks.forEach { it.beforeAttack(attacker, defender, attackRoll, critResult, damageCalcRequest) }

    override fun whenHit(attacker: BattleUnit, defender: BattleUnit, attackRoll: AttackRollResult, critResult: CritCheckResult, damage: DamageCalcResult) =
        hooks.forEach { it.whenHit(attacker, defender, attackRoll, critResult, damage) }

    override fun onHit(attacker: BattleUnit, defender: BattleUnit, attackRoll: AttackRollResult, critResult: CritCheckResult, damage: DamageCalcResult) =
        hooks.forEach { it.onHit(attacker, defender, attackRoll, critResult, damage) }
}


class AttackSystem : IteratingSystem(oneOf(CanAttackComponent::class).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
//        val hooks = entity[unitHooksMapper]
//        hooks?.beforeAttack(attacker, defender, attackRoll, critResult, damageCalcRequest)
//        // attack happens
//        // damage happens
//        hooks?.onHit(attacker, defender, attackRoll, critResult, damage)
//        hooks?.whenHit(attacker, defender, attackRoll, critResult, damage)
    }
}

class PositionComponent(val location: Scene2dLocation = Scene2dLocation()) : Component
class ActorComponent : Actor(), Component
class ImmovableComponent : Component
class CanAttackComponent: Component

val positionMapper = mapperFor<PositionComponent>()
val actorMapper = mapperFor<ActorComponent>()
val unitHooksMapper = mapperFor<UnitHooksComponent>()

class TestEntity : Entity() {
    init {
        add(ActorComponent())
    }
}

val positionFamily = allOf(PositionComponent::class, ActorComponent::class).exclude(ImmovableComponent::class.java).get()

class PositionToActorSystem : IteratingSystem(positionFamily) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val pos = entity[positionMapper]!!.location.position
        val actor = entity[actorMapper]!!

        actor.setPosition(pos.x, pos.y)
    }
}
