package uk.cyruscastle.www.view.screen.static

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.HtmlElementView
import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.phoneAppAlt
import cyruswebsite.shared.generated.resources.phoneBrowser
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLIFrameElement
import uk.cyruscastle.www.controller.Navigator
import uk.cyruscastle.www.helpers.html.getHost
import uk.cyruscastle.www.helpers.html.subscribeToHtmlController
import uk.cyruscastle.www.model.Control
import uk.cyruscastle.www.view.screen.App
import uk.cyruscastle.www.view.screen.ScreenScaffold
import uk.cyruscastle.www.view.screen.main.HomeScreen

@OptIn(ExperimentalComposeUiApi::class)
class BrowserApp(
    val url: String = "${getHost()}/composeResources/cyruswebsite.shared.generated.resources/files/browser/"
) : App(
    name = "Web",
    icon = Res.drawable.phoneBrowser,
    content = {
        ScreenScaffold(
            rightButtonLabel = "Exit",
            onControl = { control ->
                when (control) {
                    Control.PRIMARY_RIGHT -> Navigator.pop()
                    // TODO this should eventually scroll up/down, or jump between elements and let select actually select them (this would involve interop?)
                    else -> false
                }
            }
        ) {
            HtmlElementView(
                factory = {
                    val created = document.createElement("iframe") as HTMLIFrameElement

                    created.apply {
                        src = url
                    }

                    created
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
)