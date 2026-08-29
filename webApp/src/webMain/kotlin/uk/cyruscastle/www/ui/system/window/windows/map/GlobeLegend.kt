package uk.cyruscastle.www.ui.system.window.windows.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource

@Composable
fun BoxScope.GlobeLegend() {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Box(
        Modifier
            .size(16.dp)
            .align(Alignment.BottomStart)
            .offset(30.dp, (-60).dp)
            .border(1.dp, Color.Black)
            .background(Color.White)
            .hoverable(interactionSource)
    ){
        Text("?", style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center, modifier = Modifier.align(Alignment.Center))
    }

    if (isHovered){
        Column(
            Modifier
                .align(Alignment.BottomStart)
                .offset(30.dp, (-84).dp)
                .border(1.dp, Color.Black)
                .background(Color.White)
        ){
            GlobeMarkerType.entries.forEach { markerType ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(Modifier.width(10.dp))
                    Image(
                        painter = painterResource(markerType.flag),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(11.dp)
                    )
                    Spacer(Modifier.width(10.dp))

                    Text(markerType.description)
                    Spacer(Modifier.width(10.dp))
                }
            }
        }
    }
}