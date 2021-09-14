package com.runt9.fusionOfSouls.screen

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.runt9.fusionOfSouls.FosGame
import com.runt9.fusionOfSouls.model.loot.Rarity
import com.runt9.fusionOfSouls.model.loot.rune.Rune
import com.runt9.fusionOfSouls.model.unit.GameUnit
import com.runt9.fusionOfSouls.model.unit.ability.DefaultAbility
import com.runt9.fusionOfSouls.model.unit.hero.defaultHero
import com.runt9.fusionOfSouls.model.unit.unitClass.TankClass
import com.runt9.fusionOfSouls.screen.duringRun.DuringRunScreen
import com.runt9.fusionOfSouls.service.runState
import com.runt9.fusionOfSouls.util.fosVisTable
import com.runt9.fusionOfSouls.util.rectPixmapTexture
import ktx.actors.onClick
import ktx.scene2d.actors
import ktx.scene2d.vis.visLabel
import ktx.scene2d.vis.visTextButton

class RunStartScreen(private val game: FosGame, override val stage: Stage) : FosScreen {
    override fun show() {
        stage.actors {
            fosVisTable {
                visLabel("Choose Hero")

                row()

                visTextButton("Select") {
                    onClick {
                        // TODO: Hero selection
                        runState.hero = defaultHero
                        runState.unequippedRunes.add(Rune(Rarity.LEGENDARY))
                        repeat(5) {
                            runState.inactiveUnits.add(GameUnit("Basic Unit $it", rectPixmapTexture(30, 30, Color.GREEN), DefaultAbility(), listOf(TankClass())))
                        }
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
