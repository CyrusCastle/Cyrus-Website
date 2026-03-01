package uk.cyruscastle.www.ui.system.desktop

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import uk.cyruscastle.www.ui.system.window.FacsimileWindow
import kotlin.math.roundToInt
import kotlin.reflect.KClass

class DesktopGridState(val itemSize: Dp) {
    ///////////////
    // SELECTION //
    ///////////////

    private val _selectedShortcut = MutableStateFlow<KClass<out FacsimileWindow>?>(null)

    val selectedShortcut: StateFlow<KClass<out FacsimileWindow>?> = _selectedShortcut.asStateFlow()

    fun setSelectedShortcut(clazz: KClass<out FacsimileWindow>?) {
        _selectedShortcut.value = clazz
    }

    fun deselectShortcut(){
        _selectedShortcut.value = null
    }

    ////////////
    // OFFSET //
    ////////////

    fun clampOffset(offset: Offset, maxSize: Size): Offset{
        return clampOffset(
            Offset(
                x = offset.x.coerceIn(minimumValue = null, maximumValue = maxSize.width),
                y = offset.y.coerceIn(minimumValue = null, maximumValue = maxSize.height)
            )
        )
    }

    fun clampOffset(offset: Offset): Offset{
        fun snap(value: Float): Float {
            val index = (value / itemSize.value).roundToInt()
            return (index * itemSize.value).coerceIn(0f, Float.MAX_VALUE) // TODO find a max, not urgent
        }

        return Offset(
            x = snap(offset.x),
            y = snap(offset.y)
        )
    }

    fun getInitialOffset(index: IntOffset): Offset{
        return Offset(
            x = index.x * itemSize.value,
            y = index.y * itemSize.value
        )
    }
}