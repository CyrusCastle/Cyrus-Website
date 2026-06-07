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
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.utils.maxDecimals
import cyruswebsite.composeapp.generated.resources.Res
import cyruswebsite.composeapp.generated.resources.computer
import cyruswebsite.composeapp.generated.resources.folder
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.ImageFormat
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.util.encodeToByteArray
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.download
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import uk.cyruscastle.www.controller.WindowController
import uk.cyruscastle.www.ui.system.desktop.DesktopGrid
import uk.cyruscastle.www.ui.theme.ColorPalette
import uk.cyruscastle.www.ui.system.window.FacsimileWindow
import uk.cyruscastle.www.ui.system.window.topbar.TopBarEntry
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarDefaultMenus
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarMenuItem
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarMenuSubItemEntry
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarMenus
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarTextField
import kotlin.random.Random

open class FileExplorerWindow(
    val title: String,
    val folderIcon: DrawableResource = Res.drawable.folder,
    val items: List<FacsimileWindow>,
//    val fakeFolderAddress: String,
//    val fakeFolderSize: Float = 1.37f,
    val fakeFolderSize: Float = items.fold(0f) { value, window -> value + Random(window::class.hashCode()).nextFloat() }.maxDecimals(2),
    val fakeFolderLocation: String = "My Computer"
) : FacsimileWindow(
    programTitle = "File Explorer",
    fileTitle = title,
    icon = folderIcon,
    initiallyVisible = true,
    topBarContent = listOf(
        {
            WindowTopBarMenus(
                listOf(
                    WindowTopBarMenuItem(
                        "File",
                        listOf(
                            WindowTopBarMenuSubItemEntry("New", false) {},
                            WindowTopBarMenuSubItemEntry("Rename", false) {},
                            WindowTopBarMenuSubItemEntry("Delete", false) {},
                            WindowTopBarMenuSubItemEntry("Close", false) {},
                        )
                    ),

                    WindowTopBarMenuItem(
                        "Edit",
                        listOf(
                            WindowTopBarMenuSubItemEntry("Undo", false) {},
                            WindowTopBarMenuSubItemEntry("Redo", false) {},
                            WindowTopBarMenuSubItemEntry("Cut", false) {},
                            WindowTopBarMenuSubItemEntry("Copy", false) {},
                            WindowTopBarMenuSubItemEntry("Paste", false) {},
                        )
                    ),

                    WindowTopBarMenuItem(
                        "View",
                        listOf(
                            WindowTopBarMenuSubItemEntry("Toolbars", false) {},
                            WindowTopBarMenuSubItemEntry("Status Bar", false) {},
                            WindowTopBarMenuSubItemEntry("Explorer Bar", false) {},
                        )
                    ),

                    WindowTopBarMenuItem(
                        "Help",
                        listOf(
                            WindowTopBarMenuSubItemEntry("Help Page", false, { }),
                            WindowTopBarMenuSubItemEntry("About Explorer", false, { }),
                        )
                    )
                )
            )
        },
        { WindowTopBarTextField("C:/Users/Cyrus/Desktop/$title/", "Address:", {}, null, true)() }
    ),
    content = {
        val orderedItems = items.mapIndexed { index, it -> Pair(it, IntOffset(index, 0)) }

        Box(Modifier.fillMaxSize().background(Color.White)){
            DesktopGrid(
                items = orderedItems,
                innerPadding = PaddingValues(20.dp),
                itemSize = 90.dp,
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