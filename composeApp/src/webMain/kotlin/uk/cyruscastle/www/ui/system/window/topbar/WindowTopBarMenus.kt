package uk.cyruscastle.www.ui.system.window.topbar

import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import uk.cyruscastle.www.ui.extensions.modifier.intrudeExtrudeBorder
import uk.cyruscastle.www.ui.extensions.underlineFirst

@Composable
fun WindowTopBarMenus(){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        for (entry in listOf("File", "Edit", "View", "Tools", "Window", "Help")){
            Spacer(Modifier.width(10.dp))

            val hoverableSource = remember { MutableInteractionSource() }
            val isHovered by hoverableSource.collectIsHoveredAsState()

            Text(
                entry.underlineFirst(),
                modifier = Modifier
                    .hoverable(hoverableSource)
                    .then(if (isHovered) Modifier.intrudeExtrudeBorder(RectangleShape, isIntruding = false).hoverable(hoverableSource) else Modifier.hoverable(hoverableSource))
            )
        }
    }
}