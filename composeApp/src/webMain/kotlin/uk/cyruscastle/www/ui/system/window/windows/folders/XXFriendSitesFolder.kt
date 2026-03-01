package uk.cyruscastle.www.ui.system.window.windows.folders

import cyruswebsite.composeapp.generated.resources.Res
import cyruswebsite.composeapp.generated.resources.globeFolder
import uk.cyruscastle.www.ui.system.window.windows.shortcuts.ZZEmilyShortcut

class XXFriendSitesFolder : FileExplorerWindow(
    title = "Friends' Sites",
    folderIcon = Res.drawable.globeFolder,
    items = listOf(ZZEmilyShortcut())
)