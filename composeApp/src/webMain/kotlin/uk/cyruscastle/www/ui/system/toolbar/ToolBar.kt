package uk.cyruscastle.www.ui.system.toolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import cyruswebsite.composeapp.generated.resources.Res
import cyruswebsite.composeapp.generated.resources.windows
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import uk.cyruscastle.www.controller.WindowController
import uk.cyruscastle.www.ui.system.window.FacsimileWindow
import uk.cyruscastle.www.ui.theme.ColorPalette
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

const val TOOL_BAR_HEIGHT = 50

@Composable
fun ToolBar(
    windows: List<FacsimileWindow> = listOf(),
    showStartMenu: Boolean,
    toggleStartMenu: () -> Unit
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .height(TOOL_BAR_HEIGHT.dp)
            .fillMaxWidth()
            .background(ColorPalette.TOOL_BAR_BACKGROUND)
            .border(2.dp, ColorPalette.TOOL_BAR_BORDER)
            .zIndex(Float.MAX_VALUE)
    ){
        // Start menu
        Spacer(Modifier.width(15.dp))
        ToolBarEntry("Start", Res.drawable.windows, showStartMenu, 35.dp, specificWidth = 100.dp){
            toggleStartMenu()
        }

        // Each bottom icon
        Row(Modifier.height(35.dp).weight(1f, true)){
            windows.sortedBy { it.creationOrder }.forEach { window ->
                val priority by window.priority.collectAsState()
                val visible by window.visible.collectAsState()

                Spacer(Modifier.width(15.dp))

                ToolBarEntry(
                    window.programTitle,
                    window.icon,
                    priority == WindowController.getHighestPriority() && visible
                ){
                    WindowController.setTopWindow(window)
                }
            }
        }


        // Info
        Spacer(Modifier.width(15.dp))
        ToolBarClock()
        Spacer(Modifier.width(15.dp))
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun RowScope.ToolBarClock(){
    var time by remember { mutableStateOf(Clock.System.now()) }
    val timezone = TimeZone.currentSystemDefault()

    LaunchedEffect(Unit){
        while (true){
            time = Clock.System.now()
            delay(1000)
        }
    }

    val localtime = time.toLocalDateTime(timezone)
    val timeAsText = localtime.format(LocalDateTime.Format { hour(); char(':'); minute(); })

    ToolBarEntry(
        text = timeAsText,
        icon = null,
        shouldIndent = true,
        height = 35.dp,
        specificWidth = 75.dp,
        arrangement = Arrangement.Center
    )
}