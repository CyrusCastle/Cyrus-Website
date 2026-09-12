package uk.cyruscastle.www.view.screen.folder

import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.cyrusIconShortcut
import cyruswebsite.shared.generated.resources.farsigraphy
import cyruswebsite.shared.generated.resources.phoneFolderTwo
import cyruswebsite.shared.generated.resources.phonotype
import cyruswebsite.shared.generated.resources.scriptYellow
import uk.cyruscastle.www.view.screen.dynamic.ShortcutApp

class XXGithubFolder : FolderViewer(
    name = "Github",
    apps = listOf(ZZCyrusWebsiteGithub(), ZZFarsigraphyGitHubShortcut(), /*ZZPhonotypeGithubShortcut(),*/ ZZDrawBoxEnhancedGitHubShortcut()),
    icon = Res.drawable.phoneFolderTwo
)

class ZZCyrusWebsiteGithub : ShortcutApp(
    title = "This Website",
    shortcutIcon = Res.drawable.cyrusIconShortcut,
    websiteURL = "https://github.com/CyrusCastle/Cyrus-Website/"
)

class ZZDrawBoxEnhancedGitHubShortcut : ShortcutApp(
    title = "DrawBox Enhanced",
    shortcutIcon = Res.drawable.scriptYellow,
    websiteURL = "https://github.com/CyrusCastle/DrawBox-Enhanced"
)

class ZZFarsigraphyGitHubShortcut : ShortcutApp(
    title = "Farsigraphy",
    shortcutIcon = Res.drawable.farsigraphy,
    websiteURL = "https://github.com/CyrusCastle/Farsigraphy"
)

class ZZPhonotypeGithubShortcut : ShortcutApp(
    title = "Phonotype",
    shortcutIcon = Res.drawable.phonotype,
    websiteURL = "https://github.com/CyrusCastle/Phonotype"
)