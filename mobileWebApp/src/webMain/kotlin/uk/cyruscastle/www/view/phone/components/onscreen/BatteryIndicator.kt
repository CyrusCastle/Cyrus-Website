package uk.cyruscastle.www.view.phone.components.onscreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.random.Random

const val BATTERY_BLOCKS = 4
enum class BatteryOrientation { VERTICAL, HORIZONTAL }

@Composable
fun BatteryIndicator(
    charge: Float,
    modifier: Modifier = Modifier,
    orientation: BatteryOrientation = BatteryOrientation.VERTICAL,
    color: Color = Color.Black,
    dimColor: Color? = null
) = BatteryIndicator(
    level = kotlin.math.ceil(charge.coerceIn(0f, 1f) * BATTERY_BLOCKS).toInt(),
    modifier = modifier,
    orientation = orientation,
    color = color,
    dimColor = dimColor
)


@Composable
fun BatteryIndicator(
    level: Int,
    modifier: Modifier = Modifier,
    orientation: BatteryOrientation = BatteryOrientation.VERTICAL,
    width: Dp = if (orientation == BatteryOrientation.VERTICAL) 14.dp else 26.dp,
    height: Dp = if (orientation == BatteryOrientation.VERTICAL) 26.dp else 14.dp,
    color: Color = Color.Black,
    dimColor: Color? = null,
    strokeWidth: Dp = 1.5.dp,
    cornerRadius: Dp = 2.dp,
    innerPadding: Dp = 2.dp,
    blockGap: Dp = 1.dp
) {
    val filled = level.coerceIn(0, BATTERY_BLOCKS)
    val vertical = orientation == BatteryOrientation.VERTICAL

    Canvas(modifier = modifier.size(width = width, height = height)) {
        val stroke = strokeWidth.toPx()
        val radius = CornerRadius(cornerRadius.toPx())
        val pad = innerPadding.toPx()
        val gap = blockGap.toPx()

        val longAxis = if (vertical) size.height else size.width
        val shortAxis = if (vertical) size.width else size.height

        val nubDepth = longAxis * 0.09f
        val nubSpan = shortAxis * 0.40f
        val bodyLength = longAxis - nubDepth

        val bodyTop = if (vertical) nubDepth else 0f
        val bodyLeft = 0f
        val bodyW = if (vertical) size.width else bodyLength
        val bodyH = if (vertical) bodyLength else size.height

        drawRoundRect(
            color = color,
            topLeft = Offset(bodyLeft + stroke / 2f, bodyTop + stroke / 2f),
            size = Size(bodyW - stroke, bodyH - stroke),
            cornerRadius = radius,
            style = Stroke(width = stroke)
        )

        drawRoundRect(
            color = color,
            topLeft = if (vertical) {
                Offset((size.width - nubSpan) / 2f, 0f)
            } else {
                Offset(bodyLength, (size.height - nubSpan) / 2f)
            },
            size = if (vertical) Size(nubSpan, nubDepth) else Size(nubDepth, nubSpan),
            cornerRadius = CornerRadius(nubDepth / 2f)
        )

        val trackLeft = bodyLeft + stroke + pad
        val trackTop = bodyTop + stroke + pad
        val trackW = bodyW - (stroke + pad) * 2f
        val trackH = bodyH - (stroke + pad) * 2f
        val trackLong = if (vertical) trackH else trackW
        val blockLong = (trackLong - gap * (BATTERY_BLOCKS - 1)) / BATTERY_BLOCKS

        repeat(BATTERY_BLOCKS) { i ->
            val blockColor = if (i < filled && filled == 1) Color.Red else if (i < filled) color else dimColor ?: return@repeat
            val step = i * (blockLong + gap)
            drawRect(
                color = blockColor,
                topLeft = if (vertical) { Offset(trackLeft, trackTop + trackH - blockLong - step) } else { Offset(trackLeft + step, trackTop) },
                size = if (vertical) Size(trackW, blockLong) else Size(blockLong, trackH)
            )
        }
    }
}

@Composable
fun rememberRandomSlowlyDrainingBattery(): Float {
    var battery by remember { mutableFloatStateOf(1f) }

    LaunchedEffect(Unit){
        while (isActive){
            delay(Random.nextLong(900000, 2400000)) // 15 min to 40 mins

            if (battery > 0.3f){
                battery -= 0.25f
            }
        }
    }

    return battery
}