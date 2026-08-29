package uk.cyruscastle.www.ui.system.window.windows.folders

import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.cawlfythol
import cyruswebsite.shared.generated.resources.cawlfytholFolder
import cyruswebsite.shared.generated.resources.internetExplorerHTML
import uk.cyruscastle.www.ui.system.window.UniqueWindow
import uk.cyruscastle.www.ui.system.window.windows.html.edge.BrowserWindow
import uk.cyruscastle.www.ui.system.window.windows.html.edge.getHost
import uk.cyruscastle.www.ui.system.window.windows.shortcuts.ShortcutWindow

class XXCawlfytholFolder : FileExplorerWindow(
    title = "Cawlfythol",
    folderIcon = Res.drawable.cawlfytholFolder,
    items = listOf(
        ZZPuckNCoverSite(),
        ZZPuckNCoverGitupShortcut()
    )
)

class ZZPuckNCoverSite : UniqueWindow, BrowserWindow(
    websiteName = "PuckNCover",
    shortcutIcon = Res.drawable.cawlfythol,
    websiteURL = "${getHost()}/composeResources/cyruswebsite.shared.generated.resources/files/samplepage.html"
)

class ZZPuckNCoverGitupShortcut : UniqueWindow, ShortcutWindow(
    title = "PuckNCover Github",
    shortcutIcon = Res.drawable.internetExplorerHTML,
    websiteURL = "https://github.com/CyrusCastle/Puck-n-Cover/"
)