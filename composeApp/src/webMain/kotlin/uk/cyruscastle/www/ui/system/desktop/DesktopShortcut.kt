package uk.cyruscastle.www.ui.system.desktop

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import kotlin.math.max

@Composable
fun DesktopShortcut( // TODO can this be fed into Facsimile Window?
    text: String,
    icon: DrawableResource,
    selected: Boolean,
    textColor: Color,
    selectedColor: Color,
    selectedTextColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(75.dp).height(if (selected) 150.dp else 75.dp)
    ){
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            colorFilter = if (selected) ColorFilter.tint(selectedColor, BlendMode.Hue) else null, // TODO a better colour filter
            modifier = Modifier.height(37.5.dp).pointerInput(Unit) {
                detectTapGestures(onTap = {
                    onClick() // TODO split this into onTap and onDoubleTap??
                })
            }
        )
        Box(Modifier.weight(1f)) {
            var displayText by remember(selected) { mutableStateOf(text) }

            Text(
                displayText,
                color = if (selected) selectedTextColor else textColor,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { result ->
                    // We do this because there is no ellipsis in our font
                    if (result.hasVisualOverflow){
                        val endIndex = result.getLineEnd(result.lineCount - 1, visibleEnd = true)
                        displayText = text.take(max(0, endIndex - 1)).trimEnd() + "..."
                    }
                },
                modifier = Modifier
                    .background(if (selected) selectedColor else Color.Transparent)
                    .drawBehind {
                        if (!selected) return@drawBehind

                        val stroke = Stroke(
                            width = 1f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(1f, 1f), 0f)
                        )
                        drawRect(color = selectedTextColor, style = stroke)
                    }.pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            onClick()
                        })
                    }
            )
        }
    }
}