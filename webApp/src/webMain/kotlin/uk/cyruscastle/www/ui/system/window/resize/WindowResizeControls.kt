package uk.cyruscastle.www.ui.system.window.resize

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@Composable
fun WindowResizeControls(
    boxScope: BoxScope,
    windowSize: Size,
    updateSize: (Float, Float) -> Boolean,
    updatePosition: (Float, Float) -> Unit,
    disableBottom: Boolean = false,
    visible: Boolean = false
){
    with(boxScope) {
        // Sides
        WindowResizeControl(Alignment.CenterEnd, DpSize(12.dp, windowSize.height.dp - (24.dp * 2)), updateSize, updatePosition, visible)
        WindowResizeControl(Alignment.CenterStart, DpSize(12.dp, windowSize.height.dp - (24.dp * 2)), updateSize, updatePosition, visible)

        if (disableBottom){
            return
        }

        // Bottom
        WindowResizeControl(Alignment.BottomCenter, DpSize(windowSize.width.dp - (24.dp * 2), 12.dp), updateSize, updatePosition, visible)

        // Corners
        WindowResizeControl(Alignment.BottomEnd, DpSize(24.dp, 24.dp), updateSize, updatePosition, visible)
        WindowResizeControl(Alignment.BottomStart, DpSize(24.dp, 24.dp), updateSize, updatePosition, visible)
    }
}

@Composable
fun BoxScope.WindowResizeControl(
    alignment: Alignment,
    size: DpSize,
    updateSize: (Float, Float) -> Boolean,
    updatePosition: (Float, Float) -> Unit,
    visible: Boolean
){
    val dragChanges = when (alignment){ // TODO there should be a method that listens to resize start and resize stop, this is so that we can disable CSS pointer events on iframe
        Alignment.CenterStart -> { dragAmount: Offset ->
            if (updateSize(-dragAmount.x, 0f)){
                updatePosition(dragAmount.x, 0f)
            }
        }
        Alignment.CenterEnd -> { dragAmount: Offset ->
            updateSize(dragAmount.x, 0f)
        }
        Alignment.BottomStart -> { dragAmount: Offset ->
            updateSize(-dragAmount.x, dragAmount.y)
            updatePosition(dragAmount.x, 0f)
        }
        Alignment.BottomCenter -> { dragAmount: Offset ->
            updateSize(0f, dragAmount.y)
        }
        Alignment.BottomEnd -> { dragAmount: Offset ->
            updateSize(dragAmount.x, dragAmount.y)
        }

        else -> { _ ->

        }
    }

    val offset = when (alignment){
        Alignment.CenterStart -> DpOffset(-(size.width / 2), 0.dp)
        Alignment.CenterEnd -> DpOffset((size.width / 2), 0.dp)
        Alignment.BottomStart -> DpOffset(-(size.width / 2), (size.height / 2))
        Alignment.BottomCenter -> DpOffset(0.dp, (size.height / 2))
        Alignment.BottomEnd -> DpOffset((size.width / 2), (size.height / 2))

        else -> DpOffset(0.dp, 0.dp)
    }

    Box(
        modifier = Modifier
            .size(size)
            .offset(offset.x, offset.y)
            .align(alignment)
            .then(if (visible) Modifier.background(Color.Blue) else Modifier)
            .pointerHoverIcon(alignment.getResizePointerIcons())
            .pointerInput(Unit){
                detectDragGestures { _, dragAmount ->
                    dragChanges(dragAmount)
                }
            }
    )
}