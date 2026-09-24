package uk.cyruscastle.www.ui.system.window.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import uk.cyruscastle.www.ui.extensions.modifier.intrudeExtrudeBorder
import uk.cyruscastle.www.ui.extensions.underlineFirst
import uk.cyruscastle.www.ui.theme.ColorPalette

@Composable
fun WindowTopBarDefaultMenus(){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        var selectedEntry by remember { mutableStateOf<String?>(null) }
        val selectEntry = { entry: String ->
            if (selectedEntry == entry) selectedEntry = null
            else selectedEntry = entry
        }

        for (entry in listOf("File", "Edit", "View", "Tools", "Window", "Help")){
            Spacer(Modifier.width(10.dp))
            WindowTopBarMenuItem(entry, selectEntry, selectedEntry == entry, listOf())
        }
    }
}

@Composable
fun WindowTopBarMenus(items: List<WindowTopBarMenuItem>){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        var selectedEntry by remember { mutableStateOf<String?>(null) }
        val selectEntry = { entry: String ->
            if (selectedEntry == entry) selectedEntry = null
            else selectedEntry = entry
        }

        items.forEach {
            Spacer(Modifier.width(10.dp))
            WindowTopBarMenuItem(it.first, selectEntry, selectedEntry == it.first, it.second)
        }
    }
}

typealias WindowTopBarMenuItem = Pair<String, List<WindowTopBarMenuSubItemEntry>>
typealias WindowTopBarMenuSubItemEntry = Triple<String, Boolean, () -> Unit>

@Composable
fun WindowTopBarMenuItem(text: String, selectEntry: (String) -> Unit, isSelected: Boolean, items: List<WindowTopBarMenuSubItemEntry>){
    val hoverableSource = remember { MutableInteractionSource() }
    val isHovered by hoverableSource.collectIsHoveredAsState()
    val shouldHighlight = isHovered || isSelected

    Column {
        Text(
            text.underlineFirst(),
            modifier = Modifier
                .hoverable(hoverableSource)
                .clickable(onClick = { selectEntry(text) })
                .then(if (shouldHighlight) Modifier.intrudeExtrudeBorder(RectangleShape, isIntruding = false) else Modifier)
        )

        Box(Modifier.size(1.dp)){
            if (isSelected) {
                Column(
                    modifier = Modifier
                        .wrapContentSize(Alignment.TopStart, true)
                        .width(150.dp)
                        .background(ColorPalette.WINDOW_BODY_BACKGROUND)
                        .intrudeExtrudeBorder(RectangleShape, isIntruding = false)
                ) {
                    items.forEach {
                        WindowTopBarMenuSubItem(it.first, it.second) { it.third(); selectEntry(text) }
                    }
                }
            }
        }
    }
}

@Composable
fun WindowTopBarMenuSubItem(text: String, enabled: Boolean, onClick: () -> Unit){
    val hoverableSource = remember { MutableInteractionSource() }
    val isHovered by hoverableSource.collectIsHoveredAsState()

    val textColor = when {
        isHovered && enabled -> Color.White
        enabled -> Color.Black
        else -> ColorPalette.WINDOW_CONTAINER_BEZEL
    }

    Row(
        Modifier.fillMaxWidth()
            .background(if (isHovered && enabled) ColorPalette.WINDOW_ACCENT else ColorPalette.WINDOW_BODY_BACKGROUND)
            .hoverable(hoverableSource)
            .clickable(enabled = enabled, onClick = onClick)
    ){
        Spacer(Modifier.width(10.dp))
        Text(text, color = textColor)
    }
}