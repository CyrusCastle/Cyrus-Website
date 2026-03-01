package uk.cyruscastle.www.controller

import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import uk.cyruscastle.www.ui.system.window.FacsimileWindow
import kotlin.reflect.KClass

object WindowController {
    private val _windows = MutableStateFlow(listOf<FacsimileWindow>())
    val windows = _windows.asStateFlow()

    fun addWindow(window: FacsimileWindow){
        _windows.value += window
        setTopWindow(window)
    }

    fun removeWindow(window: FacsimileWindow){
        _windows.value -= window
    }

    fun hasWindowOpen(clazz: KClass<out FacsimileWindow>): Boolean {
        return _windows.value.any { it::class == clazz }
    }

    fun setTopWindow(topWindow: FacsimileWindow){
        // Demote a window from top
        _windows.value.maxBy { it.priority.value }.demoteFromTop()

        // Promote the new window
        _windows.value.sortedBy { it.priority.value }.forEachIndexed{ i, window ->
            val priority = if (window == topWindow) _windows.value.size else i

            window.updatePriority(priority)
        }

        // If it's minimised, un-minimise it!
        topWindow.makeVisible()
    }

    fun getHighestPriority(): Int{
        return _windows.value.maxOf {
            it.priority.value
        }
    }

    fun isWindowHighestPriority(window: FacsimileWindow): Boolean {
        return (window.priority.value == getHighestPriority())
    }

    fun getStartingOffset(): Offset {
        val winCount = _windows.value.size + 1
        val dx = 25f
        val dy = 25f

        return Offset(dx * winCount.toFloat(), dy * ((_windows.value.size % 6) + 1))
    }
}