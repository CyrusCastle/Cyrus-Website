package uk.cyruscastle.www.ui.system.window.windows.folders

import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.cyrusIconShortcut
import cyruswebsite.shared.generated.resources.farsigraphy
import cyruswebsite.shared.generated.resources.phonotype
import cyruswebsite.shared.generated.resources.scriptFolder
import cyruswebsite.shared.generated.resources.scriptYellow
import uk.cyruscastle.www.ui.system.window.windows.shortcuts.ShortcutWindow

class XXGithubFolder : FileExplorerWindow(
    title = "GitHub Projects",
    folderIcon = Res.drawable.scriptFolder,
    items = listOf(ZZCyrusWebsiteGithub(), ZZFarsigraphyGitHubShortcut(), /*ZZPhonotypeGithubShortcut(),*/ ZZDrawBoxEnhancedGitHubShortcut())
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

class ZZPhonotypeGithubShortcut : ShortcutWindow(
    title = "Phonotype",
    shortcutIcon = Res.drawable.phonotype,
    websiteURL = "https://github.com/CyrusCastle/Phonotype"
)