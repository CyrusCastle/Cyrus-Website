package uk.cyruscastle.www.ui.system.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import uk.cyruscastle.www.ui.theme.ColorPalette
import uk.cyruscastle.www.ui.system.window.FacsimileWindow
import kotlin.reflect.KClass

@Composable
fun DesktopGrid(
    items: List<Pair<FacsimileWindow, IntOffset>>,
    innerPadding: PaddingValues,
    itemSize: Dp = 75.dp,
    textColor: Color = ColorPalette.DESKTOP_ON_BACKGROUND,
    selectedColor: Color = ColorPalette.DESKTOP_ACCENT,
    selectedTextColor: Color = ColorPalette.DESKTOP_ON_BACKGROUND,
    backgroundColor: Color = ColorPalette.DESKTOP_BACKGROUND,
    canMoveIcons: Boolean = false,
    modifier: Modifier = Modifier
){
    val grid = remember { DesktopGridState(itemSize) }
    var selectionBox by remember { mutableStateOf(DpRect((-1).dp, (-1).dp, (-1).dp, (-1).dp)) }

    val selectionBoxOffset = DpOffset(
        if (selectionBox.left < selectionBox.right) selectionBox.left else selectionBox.right,
        if (selectionBox.top < selectionBox.bottom) selectionBox.top else selectionBox.bottom
    )
    val selectionBoxSize = DpSize(
        if (selectionBox.left < selectionBox.right) selectionBox.right - selectionBox.left else selectionBox.left - selectionBox.right,
        if (selectionBox.top < selectionBox.bottom) selectionBox.bottom - selectionBox.top else selectionBox.top - selectionBox.bottom
    )

    BoxWithConstraints(
        modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(backgroundColor)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    grid.deselectShortcut()
                })
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()

                        selectionBox = DpRect(
                            left = selectionBox.left,
                            top = selectionBox.top,
                            right = selectionBox.right + dragAmount.x.toDp(),
                            bottom = selectionBox.bottom + dragAmount.y.toDp()
                        )
                    },
                    onDragStart = { offset ->
                        grid.deselectShortcut()
                        selectionBox = DpRect(
                            left = offset.x.toDp(),
                            top = offset.y.toDp(),
                            right = offset.x.toDp(),
                            bottom = offset.y.toDp()
                        )
                    },
                    onDragCancel = {
                        grid.deselectShortcut()
                    },
                    onDragEnd = {
                        selectionBox = DpRect((-1).dp, (-1).dp, (-1).dp, (-1).dp)
                    }
                )
            }
    ){
        if (selectionBox.top != (-1).dp){
            Box(
                Modifier
                    .offset(selectionBoxOffset.x, selectionBoxOffset.y)
                    .size(selectionBoxSize)
                    .drawBehind {
                        val stroke = Stroke(
                            width = 1f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(1f, 1f), 0f)
                        )
                        drawRect(color = ColorPalette.DESKTOP_ON_BACKGROUND, style = stroke)
                    }
            )
        }

        val offsetMap = remember { mutableStateMapOf<KClass<out FacsimileWindow>, Offset>() }

        items.forEach { item ->
            var dragOffset by remember { mutableStateOf(grid.getInitialOffset(item.second)) }
            var lastSafePosition by remember { mutableStateOf(dragOffset) }
            offsetMap[item.first::class] = dragOffset

            val checkIsInside: () -> Boolean = {
                if (selectionBox.top == 1.dp) {
                    false
                } else {
                    val bounds = Rect(
                        dragOffset.x,
                        dragOffset.y,
                        dragOffset.x + 50.dp.value,
                        dragOffset.y + 50.dp.value
                    )
                    val selectionBounds = Rect(
                        selectionBoxOffset.x.value,
                        selectionBoxOffset.y.value,
                        selectionBoxOffset.x.value + selectionBoxSize.width.value,
                        selectionBoxOffset.y.value + selectionBoxSize.height.value
                    )

                    bounds.intersect(selectionBounds).width >= 0 && bounds.intersect(selectionBounds).height >= 0
                }
            }

            val isInside = selectionBox.top != (-1).dp && (checkIsInside())

            item.first.desktopShortcut(
                selectedShortcut = grid.selectedShortcut,
                setSelectedShortcut = grid::setSelectedShortcut,
                pseudoSelected = isInside,
                textColor = textColor,
                selectedColor = selectedColor,
                selectedTextColor = selectedTextColor,
                modifier = Modifier
                    .offset { IntOffset(dragOffset.x.toInt(), dragOffset.y.toInt()) }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()

                                if (canMoveIcons){
                                    dragOffset += dragAmount
                                }
                            },
                            onDragEnd = {
                                val newPosition = grid.clampOffset(dragOffset, Size(maxWidth.value - 50.dp.value, maxHeight.value - 50.dp.value))

                                if (!offsetMap.entries.any { entry -> entry.key != item.first::class && entry.value == newPosition }){
                                    dragOffset = newPosition
                                    lastSafePosition = newPosition
                                }else {
                                    dragOffset = lastSafePosition
                                }
                            }
                        )
                    }
            )
        }
    }
}