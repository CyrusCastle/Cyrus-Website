package uk.cyruscastle.www.view.phone.shapes

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class BulgingRoundedShape(
    private val cornerRadius: Dp = 12.dp,
    private val bulge: Dp = 5.dp
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val w = size.width
        val h = size.height
        val b = with(density) { bulge.toPx() }.coerceAtMost(minOf(w, h) / 8f)

        val l = b; val t = b; val rt = w - b; val bt = h - b
        val r = with(density) { cornerRadius.toPx() }
            .coerceAtMost(minOf(rt - l, bt - t) / 2f)

        val d = 2 * b
        val cx = w / 2f; val cy = h / 2f

        val path = Path().apply {
            moveTo(l + r, t)
            quadraticTo(cx, t - d, rt - r, t)           // top edge
            quadraticTo(rt, t, rt, t + r)               // TR corner
            quadraticTo(rt + d, cy, rt, bt - r)         // right edge
            quadraticTo(rt, bt, rt - r, bt)             // BR corner
            quadraticTo(cx, bt + d, l + r, bt)          // bottom edge
            quadraticTo(l, bt, l, bt - r)               // BL corner
            quadraticTo(l - d, cy, l, t + r)            // left edge
            quadraticTo(l, t, l + r, t)                 // TL corner
            close()
        }
        return Outline.Generic(path)
    }
}