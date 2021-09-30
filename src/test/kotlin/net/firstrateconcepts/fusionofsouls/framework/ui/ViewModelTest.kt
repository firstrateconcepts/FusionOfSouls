package net.firstrateconcepts.fusionofsouls.framework.ui

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import net.firstrateconcepts.fusionofsouls.util.framework.ui.Updatable
import net.firstrateconcepts.fusionofsouls.util.framework.ui.viewModel.ViewModel
import org.junit.jupiter.api.Test

class ViewModelTest {
    class TestViewModel : ViewModel() {
        val testVar = Binding(1)
    }

    class TestUpdater : Updatable {
        var hasUpdated = false

        override fun update() {
            hasUpdated = true
        }
    }

    @Test
    fun `Test binding properly updates`() {
        val testVm = TestViewModel()
        val testUpdater = TestUpdater()

        testVm.testVar.bind(testUpdater)
        testVm.testVar(2)

        assertThat(testVm.testVar.get()).isEqualTo(2)
        assertThat(testUpdater.hasUpdated).isTrue()
    }

    @Test
    fun `Test binding does not update if set to same value`() {
        val testVm = TestViewModel()
        val testUpdater = TestUpdater()

        testVm.testVar.bind(testUpdater)
        testVm.testVar(1)

        assertThat(testVm.testVar.get()).isEqualTo(1)
        assertThat(testUpdater.hasUpdated).isFalse()
    }

    @Test
    fun `Test auto-binding via invoke`() {
        val testVm = TestViewModel()
        val testUpdater = TestUpdater()

        testUpdater.apply { testVm.testVar() }

        testVm.testVar(2)

        assertThat(testVm.testVar.get()).isEqualTo(2)
        assertThat(testUpdater.hasUpdated).isTrue()
    }

    @Test
    fun `Test ViewModel dispose resets to initial`() {
        val testVm = TestViewModel()

        assertThat(testVm.testVar.get()).isEqualTo(1)
        testVm.testVar(2)
        assertThat(testVm.testVar.get()).isEqualTo(2)
        testVm.dispose()
        assertThat(testVm.testVar.get()).isEqualTo(1)
    }
}
