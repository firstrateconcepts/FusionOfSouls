package net.firstrateconcepts.fusionofsouls.util.framework.ui

interface Updatable {
    fun update()

    operator fun <T : Any> ViewModel.Binding<T>.invoke(): T {
        bind(this@Updatable)
        return get()
    }
}
