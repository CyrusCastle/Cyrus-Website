package uk.cyruscastle.www.ui.system.window.windows.html.edge

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.HtmlElementView
import cyruswebsite.composeapp.generated.resources.Res
import cyruswebsite.composeapp.generated.resources.internet
import cyruswebsite.composeapp.generated.resources.internetExplorer
import cyruswebsite.composeapp.generated.resources.internetExplorerHTML
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLIFrameElement
import uk.cyruscastle.www.ui.system.scroll.HtmlScrollableContainer
import uk.cyruscastle.www.ui.system.window.FacsimileWindow
import uk.cyruscastle.www.ui.system.window.topbar.TopBarSeparator
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarTextField
import uk.cyruscastle.www.ui.system.window.windows.html.helpers.HtmlController
import uk.cyruscastle.www.ui.system.window.windows.html.helpers.URLChecker
import uk.cyruscastle.www.ui.system.window.windows.html.helpers.subscribeToHtmlController

@OptIn(ExperimentalComposeUiApi::class)
open class BrowserWindow(
    websiteName: String,
    shortcutIcon: DrawableResource,
    websiteURL: String,
    val view: HtmlView = HtmlView(
        websiteURL,
        websiteURL
    )
) : FacsimileWindow(
    programTitle = "Internet Explorer",
    fileTitle = websiteName,
    icon = Res.drawable.internetExplorer,
    shortcutIcon = shortcutIcon,
    initiallyVisible = true,
    defaultSize = Size(750f, 650f),
    topBarContent = listOf(
        {
            val trueUrl by view.trueUrl.collectAsState()
            WindowTopBarTextField(trueUrl, "Address:", view::setUrl)()
        }
    ),
    content = {
        view.getContent()
    },
    bottomBarContent = {
//        val trueUrl by view.trueUrl.collectAsState()

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(Res.drawable.internetExplorerHTML),
                contentDescription = null,
                modifier = Modifier.size(15.dp)
            )

            Spacer(Modifier.width(5.dp))

            Text(
                text = "Done",  // TODO could link this "done" to the uh HtmlController loading
                modifier = Modifier.width(120.dp),
            )

            Spacer(Modifier.width(5.dp))
            TopBarSeparator()
            Spacer(Modifier.width(5.dp))

            Text(
                text = "",
                modifier = Modifier.width(120.dp),
            )

            Spacer(Modifier.width(5.dp))
            TopBarSeparator()
            Spacer(Modifier.width(5.dp))

            Text(
                text = "",
                modifier = Modifier.width(30.dp),
            )

            Spacer(Modifier.width(5.dp))
            TopBarSeparator()
            Spacer(Modifier.width(5.dp))

            Text(
                text = "",
                modifier = Modifier.width(30.dp),
            )

            Spacer(Modifier.width(5.dp))
            TopBarSeparator()
            Spacer(Modifier.width(5.dp))

            Image(
                painter = painterResource(Res.drawable.internet),
                contentDescription = null,
                modifier = Modifier.size(15.dp)
            )

            Spacer(Modifier.width(5.dp))

            Text(
                text = "Internet",
                modifier = Modifier.width(120.dp),
            )
        }

//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Text(trueUrl)
//        }
    }
){
    override fun setTopWindow(){
        super.setTopWindow()
        view.setTopPriority(true)
    }

    override fun demoteFromTop() {
        super.demoteFromTop()
        view.setTopPriority(false)
    }
}

class HtmlView(val url: String, val elementID: String) {
    ///////////
    // STATE //
    ///////////

    private val _topPriority = MutableStateFlow(false)
    val topPriority = _topPriority.asStateFlow()

    fun setTopPriority(isTop: Boolean) {
        _topPriority.value = isTop
    }

    private val _url = MutableStateFlow(url)
    val trueUrl = _url.asStateFlow()

    private var pendingZoom: Double? = null

    fun setUrl(newUrl: String) {
        pendingZoom = _controller.value?.getZoomer()?.getZoom()
        _url.value = URLChecker.validateURL(newUrl)
    }

    private val _controller = MutableStateFlow<HtmlController?>(null)
    val controller = _controller.asStateFlow()

    private var iframeRef: HTMLIFrameElement? = null
    private var controllerUnsubscribe: (() -> Unit)? = null

    ///////////////
    // RENDERING //
    ///////////////

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun getContent() {
        val controllerState by controller.collectAsState()
        val isTopState = topPriority.collectAsState()
        val urlState by trueUrl.collectAsState()

        Box(Modifier.fillMaxSize()) {
            HtmlScrollableContainer(controllerState) { modifier ->
                HtmlElementView(
                    factory = {
                        val created = document.createElement("iframe") as HTMLIFrameElement
                        created.apply {
                            src = url
                            id = elementID
                        }

                        iframeRef = created
                        controllerUnsubscribe = subscribeToHtmlController(created) { newController ->
                            _controller.value = newController

                            pendingZoom?.let { zoom ->
                                newController.getZoomer().setZoom(zoom)
                                pendingZoom = null
                            }
                        }

                        created
                    },
                    modifier = modifier.drawBehind {
                        drawRect(color = Color.Transparent, blendMode = BlendMode.Clear)
                    },
                    update = { frame ->
                        frame.src = urlState
                        val isTop = isTopState.value

                        window.requestAnimationFrame {
                            (frame.parentElement as? HTMLElement)?.style?.zIndex =
                                if (isTop) "0" else "-1"
                        }
                    },
                    onRelease = { frame ->
                        controllerUnsubscribe?.invoke()
                        controllerUnsubscribe = null

                        if (iframeRef === frame) iframeRef = null
                        _controller.value = null
                    }
                )
            }
        }
    }

    fun getFrame(): HTMLIFrameElement? = iframeRef

    fun getCurrentUrl(): String? = iframeRef?.src
}

fun getHost(): String = window.location.origin