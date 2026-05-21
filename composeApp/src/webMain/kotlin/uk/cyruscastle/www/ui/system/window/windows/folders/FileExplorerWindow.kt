package uk.cyruscastle.www.ui.system.window.windows.folders

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import cyruswebsite.composeapp.generated.resources.Res
import cyruswebsite.composeapp.generated.resources.computer
import cyruswebsite.composeapp.generated.resources.folder
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import uk.cyruscastle.www.ui.system.desktop.DesktopGrid
import uk.cyruscastle.www.ui.theme.ColorPalette
import uk.cyruscastle.www.ui.system.window.FacsimileWindow
import uk.cyruscastle.www.ui.system.window.topbar.TopBarEntry
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarDefaultMenus
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarTextField

open class FileExplorerWindow(
    val title: String,
    val folderIcon: DrawableResource = Res.drawable.folder,
    val items: List<FacsimileWindow>,
//    val fakeFolderAddress: String,
    val fakeFolderSize: Float = 1.37f,
    val fakeFolderLocation: String = "My Computer"
) : FacsimileWindow(
    programTitle = "File Explorer",
    fileTitle = title,
    icon = folderIcon,
    initiallyVisible = true,
    topBarContent = listOf(
        { WindowTopBarDefaultMenus() },
        { WindowTopBarTextField("C:/Users/Cyrus/Desktop/$title/", "Address:", {}, null, true)() }
    ),
    content = {
        val orderedItems = items.mapIndexed { index, it -> Pair(it, IntOffset(index, 0)) }

        Box(Modifier.fillMaxSize().background(Color.White)){
            DesktopGrid(
                items = orderedItems,
                innerPadding = PaddingValues(20.dp),
                textColor = ColorPalette.STROKE,
                backgroundColor = Color.White,
                canMoveIcons = false
            )
        }
    },
    bottomBarContent = {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "${items.size} object(s)",
                modifier = Modifier.width(120.dp),
            )

            Spacer(Modifier.width(5.dp))
            TopBarEntry(null, false) { }
            Spacer(Modifier.width(5.dp))

            Text(
                text = "${fakeFolderSize}MB",
                modifier = Modifier.width(120.dp),
            )

            Spacer(Modifier.width(5.dp))
            TopBarEntry(null, false) { }
            Spacer(Modifier.width(5.dp))

            Image(
                painter = painterResource(Res.drawable.computer),
                contentDescription = null,
                modifier = Modifier.size(15.dp)
            )

            Spacer(Modifier.width(5.dp))

            Text(
                text = fakeFolderLocation,
                modifier = Modifier.width(120.dp),
            )
        }
    }
)