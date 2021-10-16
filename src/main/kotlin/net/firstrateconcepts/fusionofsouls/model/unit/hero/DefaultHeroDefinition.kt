package net.firstrateconcepts.fusionofsouls.model.unit.hero

import net.firstrateconcepts.fusionofsouls.model.loot.passive.Ravenous
import net.firstrateconcepts.fusionofsouls.model.unit.UnitTexture
import net.firstrateconcepts.fusionofsouls.model.unit.ability.GroundSlam

object DefaultHeroDefinition : HeroDefinition {
    override val id = 1
    override val name = "Default Hero"
    override val texture = UnitTexture.HERO
    override val passive = Ravenous
    override val ability = GroundSlam
    override val unitCap = 3
    override val runeCap = 2
    override val fusionCap = 5
}
