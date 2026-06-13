package uk.cyruscastle.www.ui.system.window.windows.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import cyruswebsite.composeapp.generated.resources.Res
import cyruswebsite.composeapp.generated.resources.confused
import cyruswebsite.composeapp.generated.resources.scrollRight
import cyruswebsite.composeapp.generated.resources.settings
import cyruswebsite.composeapp.generated.resources.shutdown
import cyruswebsite.composeapp.generated.resources.smallFolder
import cyruswebsite.composeapp.generated.resources.smallHelp
import cyruswebsite.composeapp.generated.resources.smallProgram
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import uk.cyruscastle.www.controller.WindowController
import uk.cyruscastle.www.ui.extensions.modifier.intrudeExtrudeBorder
import uk.cyruscastle.www.ui.extensions.underlineFirst
import uk.cyruscastle.www.ui.system.window.FacsimileWindow
import uk.cyruscastle.www.ui.system.window.windows.email.EmailWindow
import uk.cyruscastle.www.ui.system.window.windows.folders.XXCawlfytholFolder
import uk.cyruscastle.www.ui.system.window.windows.folders.XXFriendSitesFolder
import uk.cyruscastle.www.ui.system.window.windows.folders.XXGithubFolder
import uk.cyruscastle.www.ui.system.window.windows.folders.XXPapersFolder
import uk.cyruscastle.www.ui.system.window.windows.html.edge.ZZInternetExplorer
import uk.cyruscastle.www.ui.system.window.windows.html.pdf.ZZSamplePdf
import uk.cyruscastle.www.ui.system.window.windows.map.GlobeWindow
import uk.cyruscastle.www.ui.system.window.windows.picture.PaintWindow
import uk.cyruscastle.www.ui.system.window.windows.picture.ZZImageWindow
import uk.cyruscastle.www.ui.system.window.windows.text.NotepadWindow
import uk.cyruscastle.www.ui.system.window.windows.text.WordpadWindow
import uk.cyruscastle.www.ui.system.window.windows.text.ZZHelpWordpad
import uk.cyruscastle.www.ui.system.window.windows.theme.ThemeEditorWindow
import uk.cyruscastle.www.ui.theme.ColorPalette
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js

@Composable
fun StartMenu(
    closeStartMenu: () -> Unit,
    modifier: Modifier = Modifier
){
    val entries = listOf(
        StartMenuEntry(
            "Programs", Res.drawable.smallProgram, listOf(
                StartMenuSubentry(
                    EmailWindow()
                ),
                StartMenuSubentry(
                    ZZImageWindow()
                ),
                StartMenuSubentry(
                    ZZInternetExplorer()
                ),
                StartMenuSubentry(
                    NotepadWindow()
                ),
                StartMenuSubentry(
                    PaintWindow()
                ),
                StartMenuSubentry(
                    ZZSamplePdf()
                ),
                StartMenuSubentry(
                    GlobeWindow()
                ),
                StartMenuSubentry(
                    WordpadWindow()
                ),
                StartMenuSubentry(
                    null,
                    "Audio Player *TODO?*",
                    Res.drawable.confused
                ),
                StartMenuSubentry(
                    null,
                    "Code Editor *TODO?*",
                    Res.drawable.confused
                ),
                StartMenuSubentry(
                    null,
                    "LaTeX Editor *TODO?*",
                    Res.drawable.confused
                ),
                StartMenuSubentry(
                    null,
                    "Video Player *TODO?*",
                    Res.drawable.confused
                ),
            )
        ),
        StartMenuEntry(
            "Documents", Res.drawable.smallFolder, listOf(
                StartMenuSubentry(
                    XXCawlfytholFolder(),
                    "Cawlfythol Projects"
                ),
                StartMenuSubentry(
                    XXFriendSitesFolder(),
                    "Friends' Sites"
                ),
                StartMenuSubentry(
                    XXGithubFolder(),
                    "GitHub Projects"
                ),
                StartMenuSubentry(
                    XXPapersFolder(),
                    "Publications"
                )
            )
        ),
        StartMenuEntry(
            "Settings", Res.drawable.settings, listOf(
                StartMenuSubentry(
                    ThemeEditorWindow()
                ),
            )
        ),
        StartMenuEntry(
            "Help",
            Res.drawable.smallHelp,
            listOf()
        ) {
            closeStartMenu()

            if (!WindowController.hasWindowOpen(ZZHelpWordpad::class)) {
                WindowController.addWindow(ZZHelpWordpad)
            } else {
                WindowController.setTopWindow(ZZHelpWordpad)
            }
        }
    )

    var hoveredMenu by remember { mutableStateOf<StartMenuEntry?>(null) }

    Box(modifier = modifier.size(220.dp, 300.dp)){
        MainStartMenu(entries) { hoveredMenu = it }


        hoveredMenu?.let { master ->
            val verticalOffset = entries.indexOfFirst { it.title == master.title } * (50).dp

            Column(
                Modifier
                    .offset(x = 220.dp, y = verticalOffset)
                    .background(ColorPalette.TOOL_BAR_BACKGROUND)
                    .intrudeExtrudeBorder(RectangleShape, isIntruding = false)
            ) {
                master.subentry.forEach { entry ->
                    entry.invoke(closeStartMenu)
                }
            }
        }
    }
}

@Composable
fun MainStartMenu(
    entries: List<StartMenuEntry>,
    setHovered: (StartMenuEntry?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorPalette.TOOL_BAR_BACKGROUND)
            .intrudeExtrudeBorder(RectangleShape, isIntruding = false)
    ){
        Column(modifier = Modifier.width(50.dp).fillMaxHeight().background(ColorPalette.WINDOW_CONTAINER_BACKGROUND)) {
            Spacer(Modifier.weight(1f))
            Text(
                "Cyrus Castle",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.ExtraBold,
                color = ColorPalette.TOOL_BAR_BACKGROUND,
                modifier = Modifier
//                    .align(Alignment.Start)
                    .fillMaxHeight()
                    .wrapContentSize(unbounded = true)
                    .rotate(-90f)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            entries.forEach { it.invoke(setHovered) }

            Spacer(Modifier.weight(1f))
            Spacer(Modifier.fillMaxWidth().height(1.dp).border(1.dp, ColorPalette.WINDOW_CONTAINER_BACKGROUND))
            Spacer(Modifier.fillMaxWidth().height(1.dp).border(1.dp, ColorPalette.TOOL_BAR_ENTRY_INDENT_BOTTOM))

            Spacer(Modifier.height(5.dp))

            StartMenuEntry(
                "Shutdown",
                Res.drawable.shutdown,
                listOf()
            ) { closeSite() }.invoke(setHovered)
        }
    }
}

class StartMenuEntry(
    val title: String,
    val icon: DrawableResource,
    val subentry: List<StartMenuSubentry> = listOf(),
    val onClick: (() -> Unit)? = null
){
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun invoke(setHovered: (StartMenuEntry?) -> Unit){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .onPointerEvent(PointerEventType.Enter){
                    setHovered(this@StartMenuEntry)
                }
                .clickable(onClick = onClick ?: {})
                .then(if (onClick != null) Modifier.pointerHoverIcon(PointerIcon.Hand) else Modifier)
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )

            Text(title.underlineFirst(), style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.weight(1f))

            if (subentry.isNotEmpty()){
                Image(
                    painter = painterResource(Res.drawable.scrollRight),
                    contentDescription = null,
                    modifier = Modifier.size(15.dp)
                )
            }

            Spacer(Modifier.width(5.dp))
        }
    }
}

class StartMenuSubentry(
    val window: FacsimileWindow? = null,
    val title: String? = null,
    val icon: DrawableResource? = null
){
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun invoke(closeStartMenu: () -> Unit){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(25.dp)
                .clickable {
                    closeStartMenu()

                    // Don't allow for multiple of the same folder open
                    if (window is uk.cyruscastle.www.ui.system.window.windows.folders.FileExplorerWindow && WindowController.hasWindowOpen(window::class)){
                        return@clickable
                    }

                    window?.let { WindowController.addWindow(window) }
                }
                .pointerHoverIcon(PointerIcon.Hand)
        ) {
            Spacer(Modifier.width(5.dp))
            Image(
                painter = painterResource(resource = icon ?: window?.icon ?: Res.drawable.confused),
                contentDescription = null,
                modifier = Modifier.size(25.dp)
            )

            Spacer(Modifier.width(5.dp))

            Text(title ?: window?.programTitle ?: "???", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
fun closeSite() {
    js("window.close();")
}