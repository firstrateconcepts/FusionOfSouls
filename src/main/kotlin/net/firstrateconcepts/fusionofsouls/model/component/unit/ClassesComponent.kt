package net.firstrateconcepts.fusionofsouls.model.component.unit

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.firstrateconcepts.fusionofsouls.model.unit.unitClass.UnitClass

class ClassesComponent(val classes: MutableMap<UnitClass, Int>) : Component
val classesMapper = mapperFor<ClassesComponent>()
val Entity.classes get() = this[classesMapper]!!.classes
