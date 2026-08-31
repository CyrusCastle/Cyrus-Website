package uk.cyruscastle.www.view.screen.dynamic

import org.jetbrains.compose.resources.DrawableResource
import uk.cyruscastle.www.controller.Navigator
import uk.cyruscastle.www.view.screen.App
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js

class ShortcutApp(
    name: String,
    icon: DrawableResource,
    websiteURL: String
) : App(
    name = name,
    icon = icon,
    content = {
        openShortcut(
            websiteURL
        )

        Navigator.pop()
    }
)

@OptIn(ExperimentalWasmJsInterop::class)
fun openShortcut(url: String){
    js("window.open(url, '_blank');")
}