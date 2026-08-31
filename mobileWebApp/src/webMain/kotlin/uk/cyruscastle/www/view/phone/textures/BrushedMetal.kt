package uk.cyruscastle.www.view.phone.textures

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import kotlin.random.Random

private fun brushedTile(
    width: Int = 256,
    seed: Int = 0
): ImageBitmap {
    val rnd = Random(seed)
    val bitmap = ImageBitmap(width, 1)
    val canvas = Canvas(bitmap)
    val paint = Paint()

    for (x in 0 until width) {
        val base = rnd.nextFloat() * 0.10f
        val streak = if (rnd.nextFloat() < 0.06f) rnd.nextFloat() * 0.5f else 0f
        val a = (base + streak).coerceAtMost(1f)
        if (a <= 0.001f) continue

        paint.color = Color.White.copy(alpha = a)
        canvas.drawRect(
            left = x.toFloat(), top = 0f,
            right = x + 1f, bottom = 1f,
            paint = paint
        )
    }
    return bitmap
}

fun Modifier.brushedMetal(
    intensity: Float = 1f,
    seed: Int = 0
): Modifier = composed {
    val tile = remember(seed) { brushedTile(seed = seed) }
    val brush = remember(tile) {
        ShaderBrush(ImageShader(tile, TileMode.Repeated, TileMode.Repeated))
    }
    drawWithCache {
        onDrawWithContent {
            drawRect(brush = brush, alpha = intensity)
            drawContent()
        }
    }
}