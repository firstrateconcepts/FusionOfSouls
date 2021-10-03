package net.firstrateconcepts.fusionofsouls.view.heroSelect

import com.badlogic.gdx.graphics.Texture
import net.firstrateconcepts.fusionofsouls.util.framework.ui.viewModel.ViewModel

class HeroSelectViewModel : ViewModel() {
    data class HeroDefinitionDto(val id: Int, val name: String, val texture: Texture)

    val seed = Binding("")
    var heroDefs = listOf<HeroDefinitionDto>()
    var selectedHero: HeroDefinitionDto? = null

    override fun dispose() {
        selectedHero = null
        heroDefs = emptyList()
        super.dispose()
    }
}
