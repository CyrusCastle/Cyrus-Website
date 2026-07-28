package uk.cyruscastle.www.ui.system.window.windows.folders

import cyruswebsite.composeapp.generated.resources.Res
import cyruswebsite.composeapp.generated.resources.alex
import cyruswebsite.composeapp.generated.resources.emily
import cyruswebsite.composeapp.generated.resources.globeFolder
import uk.cyruscastle.www.ui.system.window.windows.shortcuts.ShortcutWindow

class XXFriendSitesFolder : FileExplorerWindow(
    title = "Friends' Sites",
    folderIcon = Res.drawable.globeFolder,
    items = listOf(ZZAlexShortcut(), ZZEmilyShortcut())
)

class ZZAlexShortcut : ShortcutWindow(
    title = "Alex's Site",
    shortcutIcon = Res.drawable.alex,
    websiteURL = "https://a-doye.github.io/" //https://a-doye.io
)

class ZZEmilyShortcut : ShortcutWindow(
    title = "Emily's Site",
    shortcutIcon = Res.drawable.emily,
    websiteURL = "https://emilyprust.com"
)