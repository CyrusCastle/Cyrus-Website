package uk.cyruscastle.www.view.screen.static.paint

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import uk.cyruscastle.www.model.Control
import uk.cyruscastle.www.view.ColorPalette
import uk.cyruscastle.www.view.HandleControls
import kotlin.enums.enumEntries

@Composable
fun BoxScope.OptionsPage(helper: PaintHelper, hide: () -> Unit){
    var controlsEnabled by remember { mutableStateOf(true) }
    var optionIndex by remember { mutableIntStateOf(0) }

    var tapMode by remember { mutableStateOf(helper.stage == Stage.TAP_MODE) }
    var selectedColor by remember { mutableStateOf(helper.controller.getAppColor()) }
    val tool by helper.controller.canvasTool.collectAsState()
    val canUndo by helper.controller.canUndo.collectAsState(false)
    val canRedo by helper.controller.canRedo.collectAsState(false)

    LaunchedEffect(tapMode){
        if (helper.stage == Stage.DRAG_MODE_ACTIVE){
            helper.controller.onDragEnd()
        }

        if (tapMode){
            helper.stage = Stage.TAP_MODE
        }else {
            helper.stage = Stage.DRAG_MODE_INACTIVE
        }
    }

    LaunchedEffect(selectedColor){
        helper.controller.color.value = selectedColor.color
    }

    HandleControls(controlsEnabled) {
        when (it){
            Control.Up -> { optionIndex = (optionIndex - 1).coerceAtLeast(0); true }
            Control.Down -> { optionIndex = (optionIndex + 1).coerceAtMost(5); true }

            Control.Left -> {
                when (optionIndex){
                    0 -> { tapMode = !tapMode; true }
                    1 -> { selectedColor = selectedColor.previous(); true }
                    2 -> { helper.controller.canvasTool.value = tool.previous(); true }
                    3, 4, 5 -> true
                    else -> false
                }
            }
            Control.Right -> {
                when (optionIndex){
                    0 -> { tapMode = !tapMode; true }
                    1 -> { selectedColor = selectedColor.next(); true }
                    2 -> { helper.controller.canvasTool.value = tool.next(); true}
                    3, 4, 5 -> true
                    else -> false
                }
            }

            Control.Select -> {
                when (optionIndex){
                    0, 1, 2 -> true
                    3 -> { helper.controller.reset(); true }
                    4 -> { helper.controller.undo(); true }
                    5 -> { helper.controller.redo(); true }
                    else -> false
                }
            }

            Control.PRIMARY_RIGHT -> {
                if (isToolValidForStage(helper.stage, tool)){
                    controlsEnabled = false
                    hide()

                    true
                }else {
                    true
                }
            }

            else -> false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .align(Alignment.Center)
            .background(ColorPalette.ScreenGreen)
            .border(1.dp, Color.Black)
            .padding(horizontal = 5.dp, vertical = 5.dp)
    ) {
        OptionsEntry(
            optionTitle = "Draw Mode",
            optionSelected = if (tapMode) "Tap" else "Drag",
            icon = Icons.Default.Draw,
            valid = true,
            selected = optionIndex == 0
        )

        OptionsEntry(
            optionTitle = "Colour",
            optionSelected = selectedColor.name,
            icon = Icons.Default.Colorize,
            valid = true,
            selected = optionIndex == 1
        )

        OptionsEntry(
            optionTitle = "Tool",
            optionSelected = tool.name,
            icon = Icons.Default.PanTool,
            valid = isToolValidForStage(helper.stage, tool),
            selected = optionIndex == 2
        )

        OptionsEntryButton(
            optionTitle = "Clear Drawing",
            selected = optionIndex == 3,
            valid = true
        )

        OptionsEntryButton(
            optionTitle = "Undo",
            selected = optionIndex == 4,
            valid = canUndo
        )

        OptionsEntryButton(
            optionTitle = "Redo",
            selected = optionIndex == 5,
            valid = canRedo
        )
    }
}

@Composable
fun OptionsEntry(
    optionTitle: String,
    optionSelected: String,
    icon: ImageVector,
    valid: Boolean,
    selected: Boolean
){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .background(if (selected) Color.White.copy(alpha = 0.5f) else Color.Transparent)
    ) {
        Text(
            text = "$optionTitle:",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )

        Icon(
            imageVector = Icons.Default.ArrowLeft,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )

        Text(
            text = optionSelected,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            modifier = Modifier.background(if (valid) Color.Transparent else Color.Red.copy(alpha = 0.5f))
        )

        Icon(
            imageVector = Icons.Default.ArrowRight,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun OptionsEntryButton(optionTitle: String, selected: Boolean, valid: Boolean){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (selected) Color.White.copy(alpha = 0.5f) else Color.Transparent)
    ) {
        Text(
            text = optionTitle,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            modifier = Modifier.background(if (valid) Color.Transparent else Color.Gray.copy(alpha = 0.5f))
        )
    }
}

inline fun <reified T : Enum<T>> T.previous(): T {
    val entries = enumEntries<T>()
    return entries[(ordinal - 1).mod(entries.size)]
}

inline fun <reified T : Enum<T>> T.next(): T {
    val entries = enumEntries<T>()
    return entries[(ordinal + 1).mod(entries.size)]
}