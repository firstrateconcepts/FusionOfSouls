package com.runt9.fusionOfSouls.model.unit

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.utils.Disableable
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.Tooltip
import com.kotcrab.vis.ui.widget.VisImage
import com.runt9.fusionOfSouls.model.GridPoint
import com.runt9.fusionOfSouls.model.unit.ability.Ability
import com.runt9.fusionOfSouls.model.unit.attribute.PrimaryAttributes
import com.runt9.fusionOfSouls.model.unit.attribute.SecondaryAttributes
import com.runt9.fusionOfSouls.model.unit.unitClass.UnitClass
import com.runt9.fusionOfSouls.service.BattleStatus
import com.runt9.fusionOfSouls.service.runState
import com.runt9.fusionOfSouls.util.scaledLabel
import com.runt9.fusionOfSouls.view.BattleUnit
import com.soywiz.korev.Event
import com.soywiz.korev.EventDispatcher
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.scene2d
import ktx.scene2d.vis.KVisTable
import ktx.scene2d.vis.visTable
import ktx.scene2d.vis.visTooltip
import kotlin.reflect.KClass

// TODO: Refactor to 100% scene2d events
abstract class GameUnit(name: String, val unitImage: Texture, val ability: Ability, startingClasses: List<UnitClass>) : VisImage(unitImage), EventDispatcher, Disableable {
    private val dispatcher = EventDispatcher.Mixin()
    val primaryAttrs = PrimaryAttributes()
    val secondaryAttrs = SecondaryAttributes(primaryAttrs)
    val attackRange: Int
    val classes = mutableListOf<UnitClass>()
    var infoTooltip: Tooltip? = null
    var battleUnit: BattleUnit? = null
    private var isDisabled = false

    // Null means on bench
    var savedGridPos: GridPoint? = null

    init {
        this.name = name
        attackRange = startingClasses[0].baseAttackRange
        secondaryAttrs.cooldownReduction.addListener(listener = ability::updateCooldown)
        classes.addAll(startingClasses)
        runState.statusListeners.add { setDisabled(it == BattleStatus.DURING) }
        setOrigin(Align.center)

        runState.statusListeners += {
            if (it == BattleStatus.DURING) {
                infoTooltip?.detach()
                infoTooltip?.remove()
                infoTooltip = null
            } else {
                initTooltip()
            }
        }
    }

    @Scene2dDsl
    abstract fun KVisTable.additionalTooltipData()

    fun reset() {
        secondaryAttrs.purgeTemporaryModifiers()
        primaryAttrs.purgeTemporaryModifiers()
        ability.resetCooldown()
        // This is safe because only player units get reset, and they'll always be facing 0 degrees
        rotation = 0f
        battleUnit = null
        clearActions()
    }

    override fun <T : Event> addEventListener(clazz: KClass<T>, handler: (T) -> Unit) = dispatcher.addEventListener(clazz, handler)

    override fun <T : Event> dispatch(clazz: KClass<T>, event: T) {
        dispatcher.dispatch(clazz, event)
    }

    override fun setDisabled(isDisabled: Boolean) {
        this.isDisabled = isDisabled
    }

    override fun isDisabled() = this.isDisabled

    fun initTooltip() {
        if (infoTooltip != null) return

        infoTooltip = visTooltip(scene2d.visTable {
            visTable {
                primaryAttrs.all.forEach { attr ->
                    val textGetter = { "${attr.type.displayName}: ${attr.displayValue()}" }
                    scaledLabel(textGetter()) {
                        attr.addListener {
                            setText(textGetter())
                        }
                    }.cell(row = true, growY = true)
                }
            }.cell(spaceRight = 5f, growY = true)

            visTable {
                scaledLabel(this@GameUnit.name).cell(row = true, colspan = 3)

                classes.forEach { unitClass ->
                    scaledLabel(unitClass.name).cell(colspan = 4 - classes.size)
                }
                row()

                // TODO: Probably need a cooldown time listener
                val cdTextGetter = { ability.displayStr }
                scaledLabel(cdTextGetter()) {
                    secondaryAttrs.cooldownReduction.addListener { setText(cdTextGetter()) }
                }.cell(colspan = 3)

                row()

                secondaryAttrs.all.chunked(3).forEach { attrRow ->
                    attrRow.forEach { attr ->
                        val textGetter = { "${attr.type.shortName}: ${attr.displayValue()}" }
                        scaledLabel(textGetter()) {
                            attr.addListener {
                                setText(textGetter())
                            }
                        }.cell(spaceLeft = 4f, spaceRight = 4f)
                    }
                    row()
                }
            }

            additionalTooltipData()
        }) {
            appearDelayTime = 0.1f
        }
    }
}
