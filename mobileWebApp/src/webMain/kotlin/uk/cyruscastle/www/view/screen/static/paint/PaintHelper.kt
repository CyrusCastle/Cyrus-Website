package uk.cyruscastle.www.view.screen.static.paint

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import uk.codecymru.drawbox.controller.DrawController
import uk.codecymru.drawbox.model.CanvasTool

enum class PaintAppColorPalette(val color: Color) { Black(Color.Black), Red(Color.Red), Green(Color.Green), Blue(Color.Blue), White(Color.White) }

fun DrawController.getAppColor(): PaintAppColorPalette {
    return PaintAppColorPalette.entries.firstOrNull { it.color == this.color.value } ?: PaintAppColorPalette.Black
}

enum class Direction { UP, DOWN, LEFT, RIGHT }

enum class Stage { DRAG_MODE_INACTIVE, DRAG_MODE_ACTIVE, TAP_MODE }

fun isToolValidForStage(stage: Stage, tool: CanvasTool): Boolean{
    return when (tool) {
        CanvasTool.SHAPE_LINE, CanvasTool.SHAPE_RECT, CanvasTool.SHAPE_CIRCLE -> { stage != Stage.TAP_MODE }
        else -> true
    }
}

class PaintHelper(val controller: DrawController) {
    var stage by mutableStateOf(Stage.DRAG_MODE_INACTIVE)
    var maxSize by mutableStateOf(Size.Zero)
    var offset by mutableStateOf(Offset.Zero)
    var speed by mutableFloatStateOf(10f)

    fun setMaxSize(size: Size){
        maxSize = size
        offset = Offset(maxSize.width / 2, maxSize.height / 2)
    }

    fun select(): Boolean {
        when (stage){
            Stage.DRAG_MODE_INACTIVE -> {
                controller.onDragStart(offset)
                stage = Stage.DRAG_MODE_ACTIVE
            }

            Stage.DRAG_MODE_ACTIVE -> {
                controller.onDragEnd()
                stage = Stage.DRAG_MODE_INACTIVE
            }

            Stage.TAP_MODE -> controller.onTap(offset)
        }

        return true
    }

    fun moveCursor(direction: Direction): Boolean {
        val (dX, dY) = when (direction) {
            Direction.UP -> Pair(0f, -speed)
            Direction.DOWN -> Pair(0f, speed)
            Direction.LEFT -> Pair(-speed, 0f)
            Direction.RIGHT -> Pair(speed, 0f)
        }

        val x = (offset.x + dX).coerceIn(0f, maxSize.width)
        val y = (offset.y + dY).coerceIn(0f, maxSize.height)

        offset = Offset(x, y)

        if (stage == Stage.DRAG_MODE_ACTIVE){
            controller.onDrag(offset)
        }

        return true
    }
}