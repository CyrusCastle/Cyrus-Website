package uk.cyruscastle.www.ui.system.window.windows.html

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.WebElementView
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLIFrameElement
import uk.cyruscastle.www.ui.system.scroll.HtmlScrollableContainer
import uk.cyruscastle.www.ui.system.window.windows.html.helpers.HtmlController
import uk.cyruscastle.www.ui.system.window.windows.html.helpers.URLChecker
import uk.cyruscastle.www.ui.system.window.windows.html.helpers.subscribeToHtmlController

class HtmlView(val url: String, val elementID: String){
    ///////////
    // STATE //
    ///////////

    // Priority
    private val _topPriority = MutableStateFlow(false)
    val topPriority = _topPriority.asStateFlow()

    fun setTopPriority(isTop: Boolean){
        _topPriority.value = isTop
    }

    // Url
    private val _url = MutableStateFlow(url)
    val trueUrl = _url.asStateFlow()
    fun setUrl(newUrl: String){
        _url.value = URLChecker.validateURL(newUrl)
    }

    // Controller
    private val _controller = MutableStateFlow<HtmlController?>(null)
    val controller = _controller.asStateFlow()

    ///////////////
    // RENDERING //
    ///////////////

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun getContent() {
        val controllerState by controller.collectAsState()

        val isTopState = topPriority.collectAsState()
        val urlState = trueUrl.collectAsState()

        LaunchedEffect(Unit){
            subscribeToController { _controller.value = it }
        }

        Box(Modifier.fillMaxSize()){
            HtmlScrollableContainer(
                controllerState,
//                hideHorizontalIfUnscrollable = true
            ){ modifier ->
                WebElementView(
                    factory = {
                        val iframe = document.createElement("iframe") as HTMLIFrameElement

                        iframe.apply {
                            src = url
                            id = elementID
                        }
                    },
                    modifier = modifier.drawBehind {
                        drawRect(
                            color = Color.Transparent,
                            blendMode = BlendMode.Clear
                        )
                    },
                    update = { iframe ->
                        iframe.src = urlState.value
                        val isTop = isTopState.value

                        window.requestAnimationFrame {
                            (iframe.parentElement as HTMLElement).style.apply {
                                zIndex = if (isTop) "0" else "-1"
                            }
                        }
                    }
                )
            }
        }
    }

    fun getFrame(): HTMLIFrameElement? {
        return document.body?.shadowRoot?.getElementById(elementID) as? HTMLIFrameElement
    }

    fun getCurrentUrl(): String? {
        val iframe = getFrame() ?: return null

        return getCurrentURL(
            iframe
        )
    }

    //////////////////////////
    // INTEROP - CONTROLLER //
    //////////////////////////

    fun subscribeToController(callback: (HtmlController) -> Unit){
        subscribeToHtmlController(
            elementID
        ) {
            callback(it)
        }
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun(
    """
    function(iframe) {
        return iframe
            .src;
    }
    """
)
private external fun getCurrentURL(iframe: HTMLIFrameElement): String?