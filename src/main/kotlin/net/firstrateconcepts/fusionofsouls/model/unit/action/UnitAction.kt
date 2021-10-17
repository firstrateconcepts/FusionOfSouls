package net.firstrateconcepts.fusionofsouls.model.unit.action

enum class UnitActionType { MOVEMENT, TARGET, ATTACK, ABILITY }
data class UnitAction(val type: UnitActionType, val callback: () -> Unit)
class ActionBlocker(val source: Any, val action: UnitActionType)
