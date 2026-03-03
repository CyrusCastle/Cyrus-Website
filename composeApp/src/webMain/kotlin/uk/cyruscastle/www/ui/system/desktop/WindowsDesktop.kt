package uk.cyruscastle.www.ui.system.desktop

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import uk.cyruscastle.www.controller.TooltipController
import uk.cyruscastle.www.controller.WindowController
import uk.cyruscastle.www.ui.system.toolbar.ToolBar
import uk.cyruscastle.www.ui.system.window.windows.folders.XXCawlfytholFolder
import uk.cyruscastle.www.ui.system.window.windows.folders.XXFriendSitesFolder
import uk.cyruscastle.www.ui.system.window.windows.folders.XXGithubFolder
import uk.cyruscastle.www.ui.system.window.windows.html.edge.ZZInternetExplorer
import uk.cyruscastle.www.ui.system.window.windows.html.pdf.ZZCVPdfWindow
import uk.cyruscastle.www.ui.system.window.windows.html.pdf.ZZWelshForSoldiersWindow
import uk.cyruscastle.www.ui.system.window.windows.map.GlobeWindow
import uk.cyruscastle.www.ui.system.window.windows.picture.PaintWindow
import uk.cyruscastle.www.ui.system.window.windows.start.StartMenu
import uk.cyruscastle.www.ui.system.window.windows.text.NotepadWindow
import uk.cyruscastle.www.ui.system.window.windows.text.WordpadWindow

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WindowsDesktop() {
    val windows by WindowController.windows.collectAsState()
    var showStartMenu by remember { mutableStateOf(false) }
    var mousePosition by remember { mutableStateOf(Offset.Zero) }

    Box(
        Modifier
            .fillMaxSize()
            .onPointerEvent(PointerEventType.Move) {
                mousePosition = it.changes.first().position
            }
    ){
        Scaffold(
            Modifier.fillMaxWidth().fillMaxHeight(),
            bottomBar = {
                ToolBar(windows, showStartMenu) { showStartMenu = !showStartMenu }
            }
        ){ innerPadding ->
            DesktopGrid(
                items = listOf(
                    Pair(ZZInternetExplorer(), IntOffset(0, 0)),
                    Pair(NotepadWindow(), IntOffset(0, 1)),
                    Pair(WordpadWindow(), IntOffset(0, 2)),
                    Pair(PaintWindow(), IntOffset(0, 3)),

                    Pair(XXCawlfytholFolder(), IntOffset(1, 0)),
                    Pair(XXFriendSitesFolder(), IntOffset(1, 1)),
                    Pair(XXGithubFolder(), IntOffset(1, 2)),

                    Pair(GlobeWindow(), IntOffset(2, 0)),

                    Pair(ZZCVPdfWindow(), IntOffset(3, 0)),
                    Pair(ZZWelshForSoldiersWindow(), IntOffset(3, 1)),
                ),
                innerPadding = innerPadding,
                canMoveIcons = true,
                modifier = Modifier
                    .onPointerEvent(PointerEventType.Press){
                        showStartMenu = false
                    }
            )

            // Render the open windows
            windows.forEach { window ->
                key(window::class){
                    val visible by window.visible.collectAsState()
                    val priority by window.priority.collectAsState()

                    AnimatedVisibility(
                        visible = visible,
                        modifier = Modifier.zIndex(priority.toFloat()),
                        enter = slideInVertically (
                            initialOffsetY = {
                                it / 2
                            }
                        ),
                        exit = slideOutVertically(
                            targetOffsetY = {
                                it / 2
                            }
                        ) + fadeOut()
                    ) {
                        window.Window()
                    }
                }
            }
        }

        if (showStartMenu){
            StartMenu({
                showStartMenu = false
            }, Modifier.offset(y = (-45).dp).align(Alignment.BottomStart))
        }

        TooltipController.Tooltip(mousePosition.x.dp, mousePosition.y.dp)
    }
}