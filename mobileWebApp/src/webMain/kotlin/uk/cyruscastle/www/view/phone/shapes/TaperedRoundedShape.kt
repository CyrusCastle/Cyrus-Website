package uk.cyruscastle.www.view.phone.shapes

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class TaperedRoundedShape(
    private val cornerRadius: Dp = 28.dp,
    private val taper: Dp = 14.dp,
    private val taperStart: Float = 0.55f
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val w = size.width
        val h = size.height
        val r = with(density) { cornerRadius.toPx() }
            .coerceAtMost(minOf(w, h) / 2f)
        val t = with(density) { taper.toPx() }.coerceAtMost(w / 4f)
        val yStart = h * taperStart
        val k = (h - r - yStart) / 2f

        val path = Path().apply {
            moveTo(r, 0f)
            lineTo(w - r, 0f)
            quadraticTo(w, 0f, w, r)

            lineTo(w, yStart)
            cubicTo(w, yStart + k, w - t, h - r - k, w - t, h - r)
            quadraticTo(w - t, h, w - t - r, h)

            lineTo(t + r, h)
            quadraticTo(t, h, t, h - r)
            cubicTo(t, h - r - k, 0f, yStart + k, 0f, yStart)

            lineTo(0f, r)
            quadraticTo(0f, 0f, r, 0f)
            close()
        }
        return Outline.Generic(path)
    }
}