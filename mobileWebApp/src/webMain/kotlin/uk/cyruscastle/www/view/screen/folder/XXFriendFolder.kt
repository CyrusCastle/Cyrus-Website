package uk.cyruscastle.www.view.screen.folder

import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.alex
import cyruswebsite.shared.generated.resources.emily
import cyruswebsite.shared.generated.resources.phoneAlexSite
import cyruswebsite.shared.generated.resources.phoneEmilySite
import cyruswebsite.shared.generated.resources.phoneFolderTwo
import uk.cyruscastle.www.view.screen.dynamic.ShortcutApp

class XXFriendFolder : FolderViewer(
    name = "Friends",
    apps = listOf(ZZAlexShortcut(), ZZEmilyShortcut()),
    icon = Res.drawable.phoneFolderTwo
)

class ZZAlexShortcut : ShortcutApp(
    title = "Alex's Site",
    shortcutIcon = Res.drawable.phoneAlexSite,
    websiteURL = "https://a-doye.github.io/" //https://a-doye.io
)

class ZZEmilyShortcut : ShortcutApp(
    title = "Emily's Site",
    shortcutIcon = Res.drawable.phoneEmilySite,
    websiteURL = "https://emilyprust.com"
)