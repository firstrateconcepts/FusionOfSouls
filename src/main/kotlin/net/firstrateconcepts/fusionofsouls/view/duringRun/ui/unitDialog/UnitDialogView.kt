package net.firstrateconcepts.fusionofsouls.view.duringRun.ui.unitDialog

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.VisUI
import ktx.actors.onClick
import ktx.assets.async.AssetStorage
import ktx.scene2d.KTable
import ktx.scene2d.label
import ktx.scene2d.progressBar
import ktx.scene2d.stack
import ktx.scene2d.vis.flowGroup
import ktx.scene2d.vis.visImage
import ktx.scene2d.vis.visLabel
import ktx.scene2d.vis.visTable
import ktx.style.progressBar
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.displayName
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.getDisplayValue
import net.firstrateconcepts.fusionofsouls.util.ext.displayRound
import net.firstrateconcepts.fusionofsouls.util.ext.ui.bindLabelText
import net.firstrateconcepts.fusionofsouls.util.ext.ui.bindUpdatable
import net.firstrateconcepts.fusionofsouls.util.ext.ui.rectPixmapTexture
import net.firstrateconcepts.fusionofsouls.util.ext.ui.toDrawable
import net.firstrateconcepts.fusionofsouls.util.framework.ui.view.DialogView

class UnitDialogView(
    override val controller: UnitDialogController,
    override val vm: UnitDialogViewModel,
    // TODO: This is not where this is going long-term, rune controller component will handle loading asset
    private val assets: AssetStorage,
    screenWidth: Int,
    screenHeight: Int
) : DialogView(controller, vm, vm.name, screenWidth, screenHeight) {
    override val widthScale = 0.75f
    override val heightScale = 0.9f

    override fun KTable.initContentTable() {
        visTable {
            visTable {
                visImage(vm.texture)
                visTable {
                    val xpBarStyle = VisUI.getSkin().progressBar {
                        background = rectPixmapTexture(2, 2, Color.DARK_GRAY).toDrawable()
                        background.minHeight = 20f
                        background.minWidth = 0f
                        knobBefore = rectPixmapTexture(2, 2, Color.SLATE).toDrawable()
                        knobBefore.minHeight = 20f
                        knobBefore.minWidth = 0f
                    }

                    stack {
                        progressBar {
                            style = xpBarStyle

                            bindUpdatable(vm.xp) { value = vm.xp().toFloat() / vm.xpToLevel() }

                            setSize(100f, 20f)
                            setOrigin(Align.center)
                            setRound(false)
                        }

                        label("") {
                            bindLabelText { "${vm.xp()} / ${vm.xpToLevel()}" }
                            setAlignment(Align.center)
                        }
                    }.cell(width = 100f, height = 20f, row = true)

                    label("") {
                        bindLabelText { "Level ${vm.level()}" }
                    }.cell(row = true, expand = true, align = Align.center)
                }
                visTable {
                    bindUpdatable(vm.classes) {
                        clear()
                        vm.classes().forEach { (c, v) ->
                            label(c.name).cell(expand = true, align = Align.left)
                            label(v.toString()).cell(row = true, expand = true, align = Align.center)
                        }
                    }
                }
            }.cell(grow = true, row = true, spaceBottom = 20f)

            visTable {
                label("") {
                    bindLabelText { "Runes (${vm.activeRuneCount} / ${vm.runeCap()})" }
                }.cell(expandX = true, row = true, align = Align.center, spaceBottom = 10f)

                flowGroup(spacing = 5f) {
                    vm.runes().forEach { rune ->
                        val texture = if (rune.active) rune.activeTexture else rune.inactiveTexture
                        visImage(assets.get<Texture>(texture.assetFile)) {
                            onClick { controller.runeClick(rune) }
                        }
                    }
                }.cell(expand = true, padLeft = 10f, padRight = 10f, align = Align.center)
            }.cell(grow = true, row = true, spaceBottom = 10f)

            visTable {
                label("") {
                    bindLabelText { "Fusions (${vm.fusionCount()} / ${vm.fusionCap()})" }
                }.cell(expandX = true, row = true, align = Align.center, spaceBottom = 10f)

                visTable {
                    label("Passives").cell(expandX = true, row = true, align = Align.center, spaceBottom = 10f)

                    visTable {
                        bindUpdatable(vm.passives) {
                            clear()
                            vm.passives().forEach {
                                label("- ${it.name}").cell(expandX = true, row = true)
                            }
                        }
                    }.cell(grow = true, row = true)

                    label("Attribute Modifiers").cell(expandX = true, row = true, align = Align.center, spaceBottom = 10f)

                    visTable {
                        bindUpdatable(vm.attrMods) {
                            clear()
                            vm.attrMods().forEach {
                                label("- ${it.name}").cell(expandX = true, row = true)
                            }
                        }
                    }.cell(grow = true, row = true)

                    label("Ability Augments").cell(expandX = true, row = true, align = Align.center, spaceBottom = 10f)

                    visTable {
                        bindUpdatable(vm.abilityAugs) {
                            clear()
                            vm.abilityAugs().forEach {
                                label("- ${it.name}").cell(expandX = true, row = true)
                            }
                        }
                    }.cell(grow = true, row = true)

                    label("Synergies").cell(expandX = true, row = true, align = Align.center, spaceBottom = 10f)

                    visTable {
                        bindUpdatable(vm.synergies) {
                            clear()
                            vm.synergies().forEach {
                                label("- ${it.name}").cell(expandX = true, row = true)
                            }
                        }
                    }.cell(grow = true, row = true)
                }.cell(grow = true, row = true, pad = 5f)
            }
        }.cell(growX = true, pad = 20f)

        visTable {
            visTable {
                vm.passive.apply {
                    label(name).cell(expandX = true, row = true, align = Align.center)
                    label(description) {
                        setAlignment(Align.center)
                        wrap = true
                    }.cell(grow = true)
                }
            }.cell(grow = true, row = true, spaceBottom = 10f)

            visTable {
                label(vm.abilityName()).cell(expandX = true, row = true, align = Align.center)
                label("") {
                    bindLabelText { "Cooldown: ${vm.abilityCurrentCooldown().displayRound()}s (${vm.abilityBaseCooldown()}s)" }
                }.cell(expandX = true, row = true, align = Align.center)
                label(vm.abilityDescription()) {
                    setAlignment(Align.center)
                    wrap = true
                }.cell(grow = true)
            }.cell(grow = true, row = true, spaceBottom = 10f)

            label("Attributes").cell(row = true)
            visTable {
                bindUpdatable(vm.attrs) {
                    clear()
                    vm.attrs().forEach { attr ->
                        visLabel(attr.type.displayName).cell(align = Align.left, expandX = true)
                        visLabel(attr.getDisplayValue()).cell(align = Align.center, row = true)
                    }
                }
            }
        }.cell(grow = true)
    }

    override fun KTable.initButtons() { closeButton() }
}
