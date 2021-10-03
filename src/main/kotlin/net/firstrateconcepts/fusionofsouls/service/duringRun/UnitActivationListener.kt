package net.firstrateconcepts.fusionofsouls.service.duringRun

import com.badlogic.ashley.core.Family
import com.badlogic.ashley.core.PooledEngine
import ktx.ashley.allOf
import net.firstrateconcepts.fusionofsouls.model.component.ActiveComponent
import net.firstrateconcepts.fusionofsouls.model.component.UnitComponent
import net.firstrateconcepts.fusionofsouls.model.event.UnitActivatedEvent
import net.firstrateconcepts.fusionofsouls.model.event.UnitDeactivatedEvent
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus
import java.util.*

class UnitActivationListener(override val eventBus: EventBus, override val engine: PooledEngine) : EventPostingEntityListener() {
    override val logger = fosLogger()
    override val family: Family = allOf(ActiveComponent::class, UnitComponent::class).get()

    override fun entityAddedEvent(id: UUID) = UnitActivatedEvent(id)
    override fun entityRemovedEvent(id: UUID) = UnitDeactivatedEvent(id)
}
