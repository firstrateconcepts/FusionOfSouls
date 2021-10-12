package net.firstrateconcepts.fusionofsouls.view.duringRun.game.unit

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Action
import net.firstrateconcepts.fusionofsouls.util.framework.ui.viewModel.ViewModel

class UnitViewModel(val id: Int, val name: String, val texture: Texture, initialPosition: Vector2, initialRotation: Float) : ViewModel() {
    val position = Binding(initialPosition)
    val rotation = Binding(initialRotation)
    val actorActions = Binding(mutableListOf<Action>())
    val hpPercent = Binding(1f)
    val attackTimerPercent = Binding(0f)
    val abilityTimerPercent = Binding(0f)

    override fun toString() = "Unit($id | $name)"
}
