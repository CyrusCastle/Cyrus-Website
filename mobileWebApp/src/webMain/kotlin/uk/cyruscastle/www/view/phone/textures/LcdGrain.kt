package uk.cyruscastle.www.view.phone.textures

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import kotlin.random.Random

private fun noiseTile(
    size: Int = 128,
    stripePeriod: Int = 2,
    stripeAlpha: Int = 28,
    grainAlpha: Int = 22,
    seed: Int = 0
): ImageBitmap {
    val rnd = Random(seed)
    val bitmap = ImageBitmap(size, size)
    val canvas = Canvas(bitmap)
    val paint = Paint()
    val px = Size(1f, 1f)

    for (y in 0 until size) {
        for (x in 0 until size) {
            val stripe = if (x % stripePeriod == 0) stripeAlpha else 0
            val a = (stripe + rnd.nextInt(grainAlpha)).coerceAtMost(255)
            if (a == 0) continue
            paint.color = Color.Black.copy(alpha = a / 255f)
            canvas.drawRect(
                left = x.toFloat(), top = y.toFloat(),
                right = x + px.width, bottom = y + px.height,
                paint = paint
            )
        }
    }
    return bitmap
}

fun Modifier.lcdGrain(
    intensity: Float = 1f,
    seed: Int = 0
): Modifier = composed {
    val tile = remember(seed) { noiseTile(seed = seed) }
    val brush = remember(tile) {
        ShaderBrush(ImageShader(tile, TileMode.Repeated, TileMode.Repeated))
    }
    drawWithCache {
        onDrawWithContent {
            drawContent()
            drawRect(brush = brush, alpha = intensity)
        }
    }
}