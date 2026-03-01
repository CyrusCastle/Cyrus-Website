package uk.cyruscastle.www.ui.system.toolbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import uk.cyruscastle.www.ui.theme.ColorPalette
import uk.cyruscastle.www.ui.extensions.modifier.intrudeExtrudeBorder

@Composable
fun ToolBarEntry(
    text: String,
    icon: DrawableResource? = null,
    shouldIndent: Boolean,
    size: DpSize = DpSize(250.dp, 35.dp),
    arrangement: Arrangement.Horizontal = Arrangement.Start,
    onClick: (() -> Unit)? = null
) {
    Row(
        horizontalArrangement = arrangement,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .size(size)
            .intrudeExtrudeBorder(RectangleShape, isIntruding = shouldIndent)
            .then (
                if (onClick == null){
                    Modifier
                }else{
                    Modifier.clickable(onClick = onClick)
                }
            )
            .padding(horizontal = 10.dp)
            .pointerHoverIcon(PointerIcon.Hand)
    ){
        icon?.let{
            Image(
                painter = painterResource(it),
                contentDescription = null,
                modifier = Modifier.size(25.dp)
            )

            Spacer(Modifier.width(10.dp))
        }
        Text(
            text,
            fontWeight = FontWeight(FontWeight.Bold.weight),
            color = ColorPalette.TOOL_BAR_ON_BACKGROUND,
            style = MaterialTheme.typography.titleMedium
        )
    }
}