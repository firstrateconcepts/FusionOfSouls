package net.firstrateconcepts.fusionofsouls.service.duringRun

import com.badlogic.ashley.core.Family
import ktx.ashley.allOf
import net.firstrateconcepts.fusionofsouls.model.component.ActiveComponent
import net.firstrateconcepts.fusionofsouls.model.component.UnitComponent
import net.firstrateconcepts.fusionofsouls.model.event.UnitActivatedEvent
import net.firstrateconcepts.fusionofsouls.model.event.UnitDeactivatedEvent
import net.firstrateconcepts.fusionofsouls.service.AsyncPooledEngine
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger
import net.firstrateconcepts.fusionofsouls.util.framework.event.EventBus

class UnitActivationListener(override val eventBus: EventBus, override val engine: AsyncPooledEngine) : EventPostingEntityListener() {
    override val logger = fosLogger()
    override val family: Family = allOf(ActiveComponent::class, UnitComponent::class).get()

    override fun entityAddedEvent(id: Int) = UnitActivatedEvent(id)
    override fun entityRemovedEvent(id: Int) = UnitDeactivatedEvent(id)
}
