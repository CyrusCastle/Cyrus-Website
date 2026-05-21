package uk.cyruscastle.www.ui.system.window.windows.theme

import cyruswebsite.composeapp.generated.resources.Res
import cyruswebsite.composeapp.generated.resources.themes
import uk.cyruscastle.www.ui.system.window.FacsimileWindow
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarDefaultMenus

class ThemeEditorWindow : FacsimileWindow(
    programTitle = "Theme Editor *TODO?*",
    icon = Res.drawable.themes,
    initiallyVisible = true,
    topBarContent = listOf({ WindowTopBarDefaultMenus() }),
    content = content@{

    }
)