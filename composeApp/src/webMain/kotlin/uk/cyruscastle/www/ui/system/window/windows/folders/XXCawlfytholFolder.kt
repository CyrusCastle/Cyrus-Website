package uk.cyruscastle.www.ui.system.window.windows.folders

import cyruswebsite.composeapp.generated.resources.Res
import cyruswebsite.composeapp.generated.resources.cawlfythol
import cyruswebsite.composeapp.generated.resources.cawlfytholFolder
import cyruswebsite.composeapp.generated.resources.internetExplorerHTML
import uk.cyruscastle.www.ui.system.window.windows.html.edge.BrowserWindow
import uk.cyruscastle.www.ui.system.window.windows.shortcuts.ShortcutWindow

class XXCawlfytholFolder : FileExplorerWindow(
    title = "Cawlfythol",
    folderIcon = Res.drawable.cawlfytholFolder,
    items = listOf(
        ZZPuckNCoverSite(),
        ZZPuckNCoverGitupShortcut()
    )
)

class ZZPuckNCoverSite : BrowserWindow(
    websiteName = "PuckNCover",
    shortcutIcon = Res.drawable.cawlfythol,
    websiteURL = "http://localhost:8080/composeResources/cyruswebsite.composeapp.generated.resources/files/samplepage.html"
)

class ZZPuckNCoverGitupShortcut : ShortcutWindow(
    title = "PuckNCover Github",
    shortcutIcon = Res.drawable.internetExplorerHTML,
    websiteURL = "https://github.com/CyrusCastle/Puck-n-Cover/"
)