package uk.cyruscastle.www.ui.system.window.windows.folders

import cyruswebsite.composeapp.generated.resources.Res
import cyruswebsite.composeapp.generated.resources.scriptFolder
import cyruswebsite.composeapp.generated.resources.scriptYellow
import uk.cyruscastle.www.ui.system.window.windows.shortcuts.ShortcutWindow

class XXGithubFolder : FileExplorerWindow(
    title = "GitHub Projects",
    folderIcon = Res.drawable.scriptFolder,
    items = listOf(ZZCyrusWebsiteGithub())
)

class ZZCyrusWebsiteGithub : ShortcutWindow(
    title = "This Website",
    shortcutIcon = Res.drawable.scriptYellow,
    websiteURL = "https://github.com/CyrusCastle/Cyrus-Website/"
)