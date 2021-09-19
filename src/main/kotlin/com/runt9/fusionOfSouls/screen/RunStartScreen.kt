package com.runt9.fusionOfSouls.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.runt9.fusionOfSouls.FosGame
import com.runt9.fusionOfSouls.model.loot.Rarity
import com.runt9.fusionOfSouls.model.unit.hero.defaultHero
import com.runt9.fusionOfSouls.service.UnitGenerator
import com.runt9.fusionOfSouls.service.runState
import com.runt9.fusionOfSouls.util.fosVisTable
import ktx.actors.onClick
import ktx.scene2d.actors
import ktx.scene2d.vis.visLabel
import ktx.scene2d.vis.visTextButton

class RunStartScreen(private val game: FosGame, private val unitGenerator: UnitGenerator, override val stage: Stage) : FosScreen {
    override fun show() {
        stage.actors {
            fosVisTable {
                visLabel("Choose Hero")

                row()

                visTextButton("Select") {
                    onClick {
                        // TODO: Hero selection
                        runState.hero = defaultHero
                        runState.addNewUnit(unitGenerator.generateUnit(Rarity.COMMON, Texture(Gdx.files.internal("blueArrow-tp.png"))))
                        game.setScreen<DuringRunScreen>()
                    }
                }

                row()

                visTextButton("Back") {
                    onClick {
                        game.setScreen<MainMenuScreen>()
                    }
                }
            }
        }
    }
}
