package uk.cyruscastle.www.view.phone.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cyruswebsite.shared.generated.resources.NokiaSansModern
import cyruswebsite.shared.generated.resources.Res
import uk.cyruscastle.www.helpers.typography
import uk.cyruscastle.www.view.ColorPalette

@Composable
fun ColumnScope.KeyButton(number: Char, letters: List<Char>, rightMost: Boolean, repeatOnHold: Boolean = false, onClick: () -> Unit){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .background(ColorPalette.CaseEdge)
            .border(1.dp, ColorPalette.CaseLight)
            .repeatingPress(repeatingEnabled = repeatOnHold) { onClick() }
    ){
        Text(
            text = "$number",
            color = ColorPalette.KeyText,
            style = typography(Res.font.NokiaSansModern).headlineSmall,
            textAlign = if (rightMost) TextAlign.Center else TextAlign.End,
            modifier = Modifier.fillMaxWidth(0.5f).align(if (rightMost) Alignment.CenterEnd else Alignment.CenterStart)
        )
        Text(
            text = buildString { letters.forEach { append(it) } },
            color = ColorPalette.KeyText,
            style = typography(Res.font.NokiaSansModern).labelSmall,
            textAlign = if (rightMost) TextAlign.End else TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.5f).align(if (rightMost) Alignment.CenterStart else Alignment.CenterEnd)
        )
    }
}

@Composable
fun ColumnScope.VerticalKeyButton(icon: ImageVector, tint: Color, altText: String, repeatOnHold: Boolean = false, onClick: () -> Unit){
    Box(
        modifier = Modifier
            .width(75.dp)
            .weight(1f)
            .background(ColorPalette.CaseEdge)
            .border(1.dp, ColorPalette.CaseLight)
            .repeatingPress(repeatingEnabled = repeatOnHold) { onClick() }
    ){
        Icon(
            imageVector = icon,
            tint = tint,
            contentDescription = altText,
            modifier = Modifier.align(Alignment.Center).size(20.dp)
        )
    }
}

@Composable
fun Modifier.repeatingPress(
    enabled: Boolean = true,
    repeatingEnabled: Boolean = false,
    initialDelayMillis: Long = 450,
    minIntervalMillis: Long = 60,
    decay: Float = 0.8f,
    onPress: () -> Unit,
): Modifier {
    if (!enabled) return this

    return this.pointerInput(Unit) {
        awaitEachGesture {
            awaitFirstDown(requireUnconsumed = false).consume()
            onPress()

            var interval = initialDelayMillis
            while (repeatingEnabled) {
                val finished = withTimeoutOrNull(interval) { waitForUpOrCancellation() != null }
                if (finished != null) break

                onPress()
                interval = (interval * decay).toLong().coerceAtLeast(minIntervalMillis)
            }
        }
    }
}