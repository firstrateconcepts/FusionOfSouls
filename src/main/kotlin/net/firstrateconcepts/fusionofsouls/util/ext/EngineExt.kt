package net.firstrateconcepts.fusionofsouls.util.ext

import com.badlogic.ashley.core.Engine
import net.firstrateconcepts.fusionofsouls.model.component.id
import net.firstrateconcepts.fusionofsouls.model.component.idFamily
import java.util.*

fun Engine.findById(id: UUID) = getEntitiesFor(idFamily).find { it.id == id }
