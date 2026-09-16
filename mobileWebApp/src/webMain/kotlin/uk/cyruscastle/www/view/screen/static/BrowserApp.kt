package uk.cyruscastle.www.view.screen.static

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.HtmlElementView
import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.phoneBrowser
import kotlinx.browser.document
import kotlinx.coroutines.flow.MutableStateFlow
import org.w3c.dom.HTMLIFrameElement
import uk.cyruscastle.www.controller.Navigator
import uk.cyruscastle.www.helpers.html.FocusedElement
import uk.cyruscastle.www.helpers.html.HtmlMobileController
import uk.cyruscastle.www.helpers.html.getHost
import uk.cyruscastle.www.helpers.html.subscribeToHtmlMobileController
import uk.cyruscastle.www.model.Control
import uk.cyruscastle.www.model.MultiTapInput
import uk.cyruscastle.www.view.screen.App
import uk.cyruscastle.www.view.screen.ScreenScaffold

@OptIn(ExperimentalComposeUiApi::class)
class BrowserApp(
    val url: String = "${getHost()}/composeResources/cyruswebsite.shared.generated.resources/files/browser/"
) : App(
    name = "Web",
    icon = Res.drawable.phoneBrowser,
    content = {
        // Controller
        var controller by remember { mutableStateOf<HtmlMobileController?>(null) }
        var controllerUnsubscribe by remember { mutableStateOf<(() -> Unit)?>(null) }

        // Typist
        val scope = rememberCoroutineScope()
        val input = remember { MultiTapInput(scope) }

        // Bringing together
        val iterateElement: ((FocusedElement?) -> Boolean) = { element ->
            if (element != null && element.editable){
                input.overwrite(element.text)

                true
            }else {
                false
            }
        }

        LaunchedEffect(input.value){
            controller?.setText(input.value.text)
        }

        ScreenScaffold(
            rightButtonLabel = "Exit",
            onControl = { control ->
                when (control) {
                    Control.PRIMARY_RIGHT -> Navigator.pop()

                    Control.Select -> { controller?.activate() == true }

                    // Temporarily disabling left/right because otherwise it blocks our typing
//                    Control.Left -> { controller?.prev(); true }
//                    Control.Right -> { controller?.next(); true }
                    Control.Up -> { iterateElement(controller?.prev()) }
                    Control.Down -> { iterateElement(controller?.next()) }

                    else -> input.onControl(control)
                }
            }
        ) {
            HtmlElementView(
                factory = {
                    val created = document.createElement("iframe") as HTMLIFrameElement

                    created.apply {
                        src = url
                    }
                    controllerUnsubscribe = subscribeToHtmlMobileController(created) { newController ->
                        controller = newController
                    }

                    created
                },
                onRelease = { _ ->
                    controllerUnsubscribe?.invoke()
                    controllerUnsubscribe = null
                    controller = null
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
)