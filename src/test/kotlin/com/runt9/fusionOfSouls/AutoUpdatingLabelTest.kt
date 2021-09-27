package com.runt9.fusionOfSouls

object AutoUpdatingLabelTest {
    @JvmStatic
    fun main(args: Array<String>) {
        val vm = TopBarViewModel(0, 0, 3, 1, 1)
        val label = AutoUpdatingLabel { "Gold: ${vm.gold()}" }
        println(label.text)
        vm.gold += 3
        println(label.text)
    }
}

private operator fun Observable<Int>.plusAssign(t: Int) {
    set(get() + t)
}

interface Updateable {
    fun update()

    operator fun <T : Any> Observable<T>.invoke(): T {
        bind(this@Updateable)
        return get()
    }
}

class Observable<T : Any>(initialValue: T) {
    private var realValue = initialValue
    private val binds = mutableSetOf<Updateable>()

    operator fun invoke(value: T) = set(value)

    fun set(value: T) {
        if (value == realValue) return
        realValue = value
        binds.forEach(Updateable::update)
    }

    fun get() = realValue

    fun bind(updateable: Updateable) = binds.add(updateable)
}

class TopBarViewModel(gold: Int, activeUnitCount: Int, unitCap: Int, floor: Int, room: Int) {
    val gold = Observable(gold)
    val activeUnitCount = Observable(activeUnitCount)
    val unitCap = Observable(unitCap)
    val floor = Observable(floor)
    val room = Observable(room)
}

class AutoUpdatingLabel(val strGetter: AutoUpdatingLabel.() -> String) : Updateable {
    var text: String
    init {
        text = strGetter()
    }

    override fun update() {
        text = strGetter()
    }
}
