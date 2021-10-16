package net.firstrateconcepts.fusionofsouls.model.loot.passive

import net.firstrateconcepts.fusionofsouls.model.component.attrs
import net.firstrateconcepts.fusionofsouls.model.component.maxHp
import net.firstrateconcepts.fusionofsouls.model.component.name
import net.firstrateconcepts.fusionofsouls.model.loot.Rarity
import net.firstrateconcepts.fusionofsouls.model.unit.beforeDamage
import net.firstrateconcepts.fusionofsouls.util.ext.fosLogger

object Ravenous : PassiveDefinition {
    private val logger = fosLogger()
    private const val percentHp = 1.5f

    private val interceptor = beforeDamage { unit, target, request ->
        val bonusDamage = target.attrs.maxHp() * (percentHp / 100f)
        logger.debug { "${unit.name} adding base damage $bonusDamage to attack against ${target.name} from $name" }
        request.addBaseDamage(bonusDamage)
    }

    override val id = 1
    override val name = "Ravenous"
    override val description = "Attacks deal additional base damage equal to ${percentHp}% of the defender's Max HP."
    override val rarity = Rarity.COMMON
    override val strategy = InterceptorPassiveStrategy(InterceptorTarget.UNIT to interceptor)
}
