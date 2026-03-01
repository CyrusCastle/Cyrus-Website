package uk.cyruscastle.www.ui.system.window.windows.folders

import cyruswebsite.composeapp.generated.resources.Res
import cyruswebsite.composeapp.generated.resources.cawlfytholFolder
import uk.cyruscastle.www.ui.system.window.windows.html.edge.ZZPuckNCoverSite
import uk.cyruscastle.www.ui.system.window.windows.shortcuts.ZZPuckNCoverGitupShortcut

class XXCawlfytholFolder : FileExplorerWindow(
    title = "Cawlfythol",
    folderIcon = Res.drawable.cawlfytholFolder,
    items = listOf(
        ZZPuckNCoverSite(),
        ZZPuckNCoverGitupShortcut()
    )
)