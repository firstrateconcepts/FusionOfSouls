package net.firstrateconcepts.fusionofsouls.view.duringRun.ui.unitDialog

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import ktx.assets.async.AssetStorage
import ktx.scene2d.KTable
import ktx.scene2d.label
import ktx.scene2d.vis.flowGroup
import ktx.scene2d.vis.visImage
import ktx.scene2d.vis.visLabel
import ktx.scene2d.vis.visTable
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.displayName
import net.firstrateconcepts.fusionofsouls.model.attribute.definition.getDisplayValue
import net.firstrateconcepts.fusionofsouls.util.ext.displayRound
import net.firstrateconcepts.fusionofsouls.util.ext.ui.bindLabelText
import net.firstrateconcepts.fusionofsouls.util.ext.ui.bindUpdatable
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
                    label("XP Bar").cell(row = true)
                    label("Level").cell(row = true, expand = true, align = Align.center)
                }
                visTable {
                    label("Ranger").cell(row = true, expand = true, align = Align.center)
                    label("Fighter").cell(row = true, expand = true, align = Align.center)
                }
            }.cell(grow = true, row = true, spaceBottom = 20f)

            visTable {
                label(vm.abilityName()).cell(expandX = true, row = true, align = Align.center)
                label("") {
                    bindLabelText { "Cooldown: ${vm.abilityCurrentCooldown().displayRound()}s (${vm.abilityBaseCooldown()}s)" }
                }.cell(expandX = true, row = true, align = Align.center)
                label(vm.abilityDescription()) {
                    setAlignment(Align.center)
                    wrap = true
                }.cell(grow = true)
            }.cell(grow = true, row = true, spaceBottom = 20f)

            visTable {
                label("") {
                    bindLabelText { "Runes (${vm.activeRuneCount} / ${vm.runeCap()})" }
                }.cell(expandX = true, row = true, align = Align.center, spaceBottom = 10f)

                flowGroup(spacing = 5f) {
                    vm.runes().forEach { rune ->
                        val texture = if (rune.active) rune.activeTexture else rune.inactiveTexture
                        visImage(assets.get<Texture>(texture.assetFile)) {
                            onClick {
                                rune.active = !rune.active
                                val texture = if (rune.active) rune.activeTexture else rune.inactiveTexture
                                setDrawable(assets.get<Texture>(texture.assetFile))
                            }
                        }
                    }
                }.cell(expand = true, padLeft = 10f, padRight = 10f, align = Align.center)
            }.cell(grow = true)
        }.cell(growX = true, pad = 20f)

        visTable {
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
