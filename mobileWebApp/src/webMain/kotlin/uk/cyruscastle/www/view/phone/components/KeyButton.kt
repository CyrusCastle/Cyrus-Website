package uk.cyruscastle.www.view.phone.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import uk.cyruscastle.www.view.ColorPalette

@Composable
fun ColumnScope.KeyButton(number: Char, letters: List<Char>, rightMost: Boolean, onClick: () -> Unit){
    Box(
        modifier = Modifier
            .width(75.dp)
            .weight(1f)
            .background(ColorPalette.CaseEdge)
            .border(1.dp, ColorPalette.CaseLight)
            .pointerInput(Unit) {
                detectTapGestures { tap ->
                    onClick()
                }
            }
    ){
        Text(
            text = "$number",
            color = ColorPalette.KeyText,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = if (rightMost) TextAlign.Center else TextAlign.End,
            modifier = Modifier.fillMaxWidth(0.5f).align(if (rightMost) Alignment.CenterEnd else Alignment.CenterStart)
        )
        Text(
            text = buildString { letters.forEach { append(it) } },
            color = ColorPalette.KeyText,
            style = MaterialTheme.typography.labelSmall,
            textAlign = if (rightMost) TextAlign.End else TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.5f).align(if (rightMost) Alignment.CenterStart else Alignment.CenterEnd)
        )
    }
}

@Composable
fun ColumnScope.VerticalKeyButton(icon: ImageVector, tint: Color, altText: String, onClick: () -> Unit){
    Box(
        modifier = Modifier
            .width(75.dp)
            .weight(1f)
            .background(ColorPalette.CaseEdge)
            .border(1.dp, ColorPalette.CaseLight)
            .pointerInput(Unit) {
                detectTapGestures { tap ->
                    onClick()
                }
            }
    ){
        Icon(
            imageVector = icon,
            tint = tint,
            contentDescription = altText,
            modifier = Modifier.align(Alignment.Center).size(20.dp)
        )
    }
}