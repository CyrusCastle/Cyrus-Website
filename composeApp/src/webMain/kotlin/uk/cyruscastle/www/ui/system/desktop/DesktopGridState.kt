package uk.cyruscastle.www.ui.system.desktop

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
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

    fun clampOffset(offset: DpOffset, maxSize: DpSize): DpOffset{
        return clampOffset(
            DpOffset(
                x = offset.x.coerceIn(minimumValue = null, maximumValue = maxSize.width),
                y = offset.y.coerceIn(minimumValue = null, maximumValue = maxSize.height)
            )
        )
    }

    fun clampOffset(offset: DpOffset): DpOffset{
        fun snap(value: Dp): Dp {
            val index = (value / itemSize).roundToInt()
            return (index * itemSize).coerceIn(0.dp, Dp.Infinity) // TODO find a max, not urgent
        }

        return DpOffset(
            x = snap(offset.x),
            y = snap(offset.y)
        )
    }

    fun getInitialOffset(index: IntOffset): DpOffset{
        return DpOffset(
            x = itemSize * index.x,
            y = itemSize * index.y
        )
    }
}