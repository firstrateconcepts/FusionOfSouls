package net.firstrateconcepts.fusionofsouls.view.duringRun.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import net.firstrateconcepts.fusionofsouls.util.framework.ui.viewModel.ViewModel
import java.util.*

class UnitViewModel(val id: UUID, val name: String, val texture: Texture, initialPosition: Vector2) : ViewModel() {
    val position = Binding(initialPosition)

    override fun toString() = "Unit($id | $name)"
}
