package uk.cyruscastle.www.ui.system.window.windows.shortcuts

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import uk.cyruscastle.www.controller.WindowController
import uk.cyruscastle.www.ui.system.window.FacsimileWindow

@OptIn(ExperimentalResourceApi::class)
open class ShortcutWindow(
    title: String,
    shortcutIcon: DrawableResource,
    websiteURL: String
) : FacsimileWindow(
    programTitle = "Shortcut",
    fileTitle = title,
    icon = shortcutIcon,
    initiallyVisible = true,
    content = content@{
        openShortcut(
            websiteURL
        )
        WindowController.removeWindow(this)
    }
)

@OptIn(ExperimentalWasmJsInterop::class)
fun openShortcut(url: String){
    js("window.open(url, '_blank');")
}