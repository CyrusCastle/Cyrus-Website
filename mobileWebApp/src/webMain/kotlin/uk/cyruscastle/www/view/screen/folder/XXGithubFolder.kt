package uk.cyruscastle.www.view.screen.folder

import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.cyrusIconHigherRes
import cyruswebsite.shared.generated.resources.phoneBooks
import cyruswebsite.shared.generated.resources.phoneFarsigraphy
import cyruswebsite.shared.generated.resources.phoneFolderTwo
import cyruswebsite.shared.generated.resources.phonePhonotype
import cyruswebsite.shared.generated.resources.phoneSettings
import uk.cyruscastle.www.controller.DEFAULT_MESSAGE_SHORT
import uk.cyruscastle.www.controller.HelpMessage
import uk.cyruscastle.www.view.screen.dynamic.ShortcutApp

class XXGithubFolder : FolderViewer(
    name = "Github",
    helpMessage = HelpMessage(
        "Github",
        "A collection of my main GitHub projects."
    ),
    apps = listOf(ZZCyrusWebsiteGithub(), ZZFarsigraphyGitHubShortcut(), /*ZZPhonotypeGithubShortcut(),*/ ZZDrawBoxEnhancedGitHubShortcut()),
    icon = Res.drawable.phoneFolderTwo
)

class ZZCyrusWebsiteGithub : ShortcutApp(
    title = "This Website",
    shortcutIcon = Res.drawable.cyrusIconHigherRes,
    websiteURL = "https://github.com/CyrusCastle/Cyrus-Website/"
)

class ZZDrawBoxEnhancedGitHubShortcut : ShortcutApp(
    title = "DrawBox Enhanced",
    shortcutIcon = Res.drawable.phoneSettings,
    websiteURL = "https://github.com/CyrusCastle/DrawBox-Enhanced"
)

class ZZFarsigraphyGitHubShortcut : ShortcutApp(
    title = "Farsigraphy",
    shortcutIcon = Res.drawable.phoneFarsigraphy,
    websiteURL = "https://github.com/CyrusCastle/Farsigraphy"
)

class ZZPhonotypeGithubShortcut : ShortcutApp(
    title = "Phonotype",
    shortcutIcon = Res.drawable.phonePhonotype,
    websiteURL = "https://github.com/CyrusCastle/Phonotype"
)