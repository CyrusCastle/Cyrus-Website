package uk.cyruscastle.www.ui.system.window.windows.folders

import cyruswebsite.composeapp.generated.resources.Res
import cyruswebsite.composeapp.generated.resources.cyrusIconShortcut
import cyruswebsite.composeapp.generated.resources.farsigraphy
import cyruswebsite.composeapp.generated.resources.internetExplorerHTML
import cyruswebsite.composeapp.generated.resources.scriptFolder
import cyruswebsite.composeapp.generated.resources.scriptYellow
import uk.cyruscastle.www.ui.system.window.windows.shortcuts.ShortcutWindow

class XXGithubFolder : FileExplorerWindow(
    title = "GitHub Projects",
    folderIcon = Res.drawable.scriptFolder,
    items = listOf(ZZCyrusWebsiteGithub(), ZZFarsigraphyGitHubShortcut(), ZZDrawBoxEnhancedGitHubShortcut())
)

class ZZCyrusWebsiteGithub : ShortcutWindow(
    title = "This Website",
    shortcutIcon = Res.drawable.cyrusIconShortcut,
    websiteURL = "https://github.com/CyrusCastle/Cyrus-Website/"
)

class ZZDrawBoxEnhancedGitHubShortcut : ShortcutWindow(
    title = "DrawBox Enhanced",
    shortcutIcon = Res.drawable.scriptYellow,
    websiteURL = "https://github.com/CyrusCastle/DrawBox-Enhanced"
)

class ZZFarsigraphyGitHubShortcut : ShortcutWindow(
    title = "Farsigraphy",
    shortcutIcon = Res.drawable.farsigraphy,
    websiteURL = "https://github.com/CyrusCastle/Farsigraphy"
)