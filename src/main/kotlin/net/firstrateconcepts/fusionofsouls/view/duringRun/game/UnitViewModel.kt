package net.firstrateconcepts.fusionofsouls.view.duringRun.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import net.firstrateconcepts.fusionofsouls.util.framework.ui.viewModel.ViewModel

class UnitViewModel(val id: Int, val name: String, val texture: Texture, initialPosition: Vector2) : ViewModel() {
    val position = Binding(initialPosition)

    override fun toString() = "Unit($id | $name)"
}
