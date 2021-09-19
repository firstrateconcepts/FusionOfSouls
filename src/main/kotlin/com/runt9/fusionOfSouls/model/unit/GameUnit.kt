package com.runt9.fusionOfSouls.model.unit

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.utils.Disableable
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.VisImage
import com.runt9.fusionOfSouls.model.GridPoint
import com.runt9.fusionOfSouls.model.unit.ability.Ability
import com.runt9.fusionOfSouls.model.unit.attribute.PrimaryAttributes
import com.runt9.fusionOfSouls.model.unit.attribute.SecondaryAttributes
import com.runt9.fusionOfSouls.model.unit.unitClass.UnitClass
import com.runt9.fusionOfSouls.service.BattleStatus
import com.runt9.fusionOfSouls.service.runState
import com.soywiz.korev.Event
import com.soywiz.korev.EventDispatcher
import kotlin.reflect.KClass

// TODO: Refactor to 100% scene2d events
abstract class GameUnit(name: String, val unitImage: Texture, val ability: Ability, startingClasses: List<UnitClass>) : VisImage(unitImage), EventDispatcher, Disableable {
    private val dispatcher = EventDispatcher.Mixin()
    val primaryAttrs = PrimaryAttributes()
    val secondaryAttrs = SecondaryAttributes(primaryAttrs)
    val attackRange: Int
    val classes = mutableListOf<UnitClass>()
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
    }

    fun reset() {
        secondaryAttrs.purgeTemporaryModifiers()
        primaryAttrs.purgeTemporaryModifiers()
        ability.resetCooldown()
    }

    override fun <T : Event> addEventListener(clazz: KClass<T>, handler: (T) -> Unit) = dispatcher.addEventListener(clazz, handler)

    override fun <T : Event> dispatch(clazz: KClass<T>, event: T) {
        dispatcher.dispatch(clazz, event)
    }

    override fun setDisabled(isDisabled: Boolean) {
        this.isDisabled = isDisabled
    }

    override fun isDisabled() = this.isDisabled
}
