package uk.cyruscastle.www.ui.system.window.windows.folders

import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.cyfeiriad
import cyruswebsite.shared.generated.resources.cyrusIconShortcut
import cyruswebsite.shared.generated.resources.farsigraphy
import cyruswebsite.shared.generated.resources.phonotype
import cyruswebsite.shared.generated.resources.scriptFolder
import cyruswebsite.shared.generated.resources.scriptYellow
import cyruswebsite.shared.generated.resources.whiteboard
import uk.cyruscastle.www.ui.system.window.windows.shortcuts.ShortcutWindow

class XXGithubFolder : FileExplorerWindow(
    title = "GitHub Projects",
    folderIcon = Res.drawable.scriptFolder,
    items = listOf(ZZCyrusWebsiteGithub(), ZZFarsigraphyGitHubShortcut(), /*ZZPhonotypeGithubShortcut(),*/ ZZDrawBoxEnhancedGitHubShortcut(), ZZWhiteboardsGithubShortcut(), ZZCyfeiriadGithubShortcut())
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

class ZZWhiteboardsGithubShortcut : ShortcutWindow(
    title = "Whiteboard",
    shortcutIcon = Res.drawable.whiteboard,
    websiteURL = "https://github.com/CyrusCastle/CheckForUnderstanding/"
)

class ZZCyfeiriadGithubShortcut : ShortcutWindow(
    title = "Cyfeiriad (.bst)",
    shortcutIcon = Res.drawable.cyfeiriad,
    websiteURL = "https://github.com/CyrusCastle/Cyfeiriad/"
)