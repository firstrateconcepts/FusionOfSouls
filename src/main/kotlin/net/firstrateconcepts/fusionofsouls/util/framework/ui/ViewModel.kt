package net.firstrateconcepts.fusionofsouls.util.framework.ui

import com.badlogic.gdx.utils.Disposable

abstract class ViewModel : Disposable {
    private val fields = mutableListOf<Binding<*>>()

    override fun dispose() {
        fields.forEach(Disposable::dispose)
    }

    inner class Binding<T : Any>(private val initialValue: T) : Disposable {
        private var realValue = initialValue
        private val binds = mutableSetOf<Updatable>()

        init {
            fields.add(this)
        }

        operator fun invoke(value: T) = set(value)

        fun set(value: T) {
            if (value == realValue) return
            realValue = value
            binds.forEach(Updatable::update)
        }

        fun get() = realValue

        fun bind(updatable: Updatable) = binds.add(updatable)

        override fun dispose() {
            binds.clear()
            realValue = initialValue
        }
    }
}

