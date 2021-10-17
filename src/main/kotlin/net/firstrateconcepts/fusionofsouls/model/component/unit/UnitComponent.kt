package net.firstrateconcepts.fusionofsouls.model.component.unit

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.get
import ktx.ashley.mapperFor
import ktx.ashley.oneOf
import net.firstrateconcepts.fusionofsouls.model.unit.UnitTeam
import net.firstrateconcepts.fusionofsouls.model.unit.UnitType
import net.firstrateconcepts.fusionofsouls.model.unit.ability.AbilityDefinition

class UnitComponent(val type: UnitType, val team: UnitTeam, val ability: AbilityDefinition) : Component
val unitFamily = oneOf(UnitComponent::class).get()!!
val unitMapper = mapperFor<UnitComponent>()
val Entity.unitInfo get() = this[unitMapper]!!
val Entity.team get() = unitInfo.team
val Entity.ability get() = unitInfo.ability
