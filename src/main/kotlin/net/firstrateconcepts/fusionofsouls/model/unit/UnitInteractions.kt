package net.firstrateconcepts.fusionofsouls.model.unit

interface UnitInteraction
data class HitCheck(val rawRoll: Int, val attackBonus: Float, val evasion: Float) : UnitInteraction
data class HitResult(val rawRoll: Int, val finalRoll: Int, val damageScale: Float) : UnitInteraction {
    val isHit get() = finalRoll >= 0
    val isCrit get() = finalRoll > 100
}
data class DamageRequest(val hit: HitResult) : UnitInteraction {
    var additionalBaseDamage = 0f
        private set
    var additionalDamageMultiplier = 1f
        private set

    fun addBaseDamage(damage: Float) { additionalBaseDamage += damage }
    fun addDamageMultiplier(multi: Float) { additionalDamageMultiplier *= multi }
}

data class DamageResult(val rawDamage: Int, val finalDamage: Int) : UnitInteraction
