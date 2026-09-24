package uk.cyruscastle.www.ui.extensions.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.checkerboardBackground(
    lightColor: Color = Color.White,
    darkColor: Color = Color(0xFFCCCCCC),
    squareSize: Dp = 12.dp
) = this.drawWithCache {

    val sizePx = squareSize.toPx()

    val columns = (size.width / sizePx).toInt() + 1
    val rows = (size.height / sizePx).toInt() + 1

    onDrawBehind {
        clipRect {
            for (row in 0..rows) {
                for (col in 0..columns) {

                    val isDark = (row + col) % 2 == 0

                    drawRect(
                        color = if (isDark) darkColor else lightColor,
                        topLeft = Offset(col * sizePx, row * sizePx),
                        size = Size(sizePx, sizePx)
                    )
                }
            }
        }
    }
}