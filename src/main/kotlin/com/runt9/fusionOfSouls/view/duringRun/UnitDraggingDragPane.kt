package com.runt9.fusionOfSouls.view.duringRun

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.kotcrab.vis.ui.layout.DragPane
import com.kotcrab.vis.ui.widget.Draggable
import ktx.scene2d.KStack
import ktx.scene2d.vis.KVisTable

// TODO: Swap on drop where another unit is. Currently just rejects to prevent overlap
abstract class UnitDraggingDragPane(content: KVisTable) : DragPane(content) {
    init {
        draggable = Draggable()
        draggable.isInvisibleWhenDragged = true
        draggable.listener = UnitDraggingDragListener()
    }

    fun getClosestToPosition(position: Vector2) = getStacks().minByOrNull { position.dst(it.localToStageCoordinates(Vector2(0f, 0f))) }
    fun getStacks() = (this.actor as KVisTable).cells.map { it.actor as KStack }

    abstract fun addToProperStack(actor: Actor)
    abstract fun getActorToAdd(actor: Actor): Actor

    override fun contains(actor: Actor) = children.contains(actor) || super.contains(actor)

    override fun addActor(actor: Actor) {
        val actorToAdd = getActorToAdd(actor)

        addToProperStack(actor)
        doOnAdd(actorToAdd)
    }

    override fun addActorBefore(actorBefore: Actor, actor: Actor) {
        if (actorBefore is KStack) {
            val actorToAdd = getActorToAdd(actor)

            actorBefore.add(actorToAdd)
            doOnAdd(actorToAdd)
        }
    }

    override fun addActorAfter(actorAfter: Actor, actor: Actor) {
        addActorBefore(actorAfter, actor)
    }

    override fun addActorAt(index: Int, actor: Actor) {
        doOnAdd(actor)
    }

    private inner class UnitDraggingDragListener : DragPane.DefaultDragListener() {
        override fun addToOtherGroup(actor: Actor, dragPane: DragPane, directPaneChild: Actor): Boolean {
            if (dragPane is UnitDraggingDragPane) {
                if (directPaneChild is KStack && directPaneChild.children.any { it !is Image }) {
                    return CANCEL
                }

                actor.remove()
                dragPane.addActorBefore(directPaneChild, actor)
            } else {
                super.addToOtherGroup(actor, dragPane, directPaneChild)
            }

            return APPROVE
        }

        override fun addDirectlyToPane(draggable: Draggable, actor: Actor, dragPane: DragPane): Boolean {
            if (!accept(actor, dragPane)) {
                return CANCEL
            }

            if (dragPane is UnitDraggingDragPane) {
                actor.remove()
                dragPane.addActorBefore(dragPane.getClosestToPosition(DRAG_POSITION), actor)
            } else {
                super.addDirectlyToPane(draggable, actor, dragPane)
            }

            return APPROVE
        }
    }
}
