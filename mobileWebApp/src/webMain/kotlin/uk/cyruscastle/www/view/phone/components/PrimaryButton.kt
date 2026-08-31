package uk.cyruscastle.www.view.phone.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import uk.cyruscastle.www.view.ColorPalette

@Composable
fun PrimaryButton(corner: BracketCorner, color: Color, onClick: () -> Boolean){
    Box(Modifier
        .width(100.dp)
        .height(50.dp)
        .background(ColorPalette.CaseEdge)
        .border(1.dp, ColorPalette.CaseLight)
        .pointerInput(Unit) {
            detectTapGestures { tap ->
                onClick()
            }
        }
    ){
        CornerBracket(
            corner = corner,
            modifier = Modifier.size(width = 50.dp, height = 20.dp).align(Alignment.Center),
            color = color,
            strokeWidth = 6.dp,
            cornerRadius = 40.dp
        )
    }
}

enum class BracketCorner { TopStart, TopEnd, BottomStart, BottomEnd }

@Composable
private fun CornerBracket(
    corner: BracketCorner,
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    strokeWidth: Dp = 2.dp,
    cornerRadius: Dp = 32.dp
) {
    val rtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    Canvas(modifier) {
        val sw = strokeWidth.toPx()
        val half = sw / 2f

        val l = half
        val t = half
        val rt = size.width - half
        val b = size.height - half

        val r = cornerRadius.toPx()
            .coerceAtMost(minOf(rt - l, b - t))

        val top = corner == BracketCorner.TopStart || corner == BracketCorner.TopEnd
        val startSide = corner == BracketCorner.TopStart || corner == BracketCorner.BottomStart
        val left = startSide != rtl

        val cx = if (left) l else rt
        val cy = if (top) t else b
        val farX = if (left) rt else l
        val farY = if (top) b else t

        val dx = if (left) r else -r
        val dy = if (top) r else -r

        val path = Path().apply {
            moveTo(cx, farY)
            lineTo(cx, cy + dy)
            quadraticTo(cx, cy, cx + dx, cy)
            lineTo(farX, cy)
        }

        drawPath(
            path = path,
            color = color,
            style = Stroke(
                width = sw,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}