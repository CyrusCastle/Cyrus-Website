package uk.cyruscastle.www.ui.system.window.windows.html.pdf

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import cyruswebsite.composeapp.generated.resources.Res
import cyruswebsite.composeapp.generated.resources.grabbing
import cyruswebsite.composeapp.generated.resources.pdf
import cyruswebsite.composeapp.generated.resources.zoomIn
import cyruswebsite.composeapp.generated.resources.zoomOut
import uk.cyruscastle.www.ui.system.window.FacsimileWindow
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarButtons
import uk.cyruscastle.www.ui.system.window.topbar.TopBarEntry
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarMenus
import uk.cyruscastle.www.ui.system.window.windows.html.HtmlView

@OptIn(ExperimentalWasmJsInterop::class, ExperimentalComposeUiApi::class)
open class PdfWindow(
    pdfTitle: String,
    pdfFilePath: String,
    val view: HtmlView = HtmlView(
        "http://localhost:8080/composeResources/cyruswebsite.composeapp.generated.resources/files/pdf/pdf.html?file=$pdfFilePath",
        "pdfWindowContent${pdfFilePath}"
    )
) : FacsimileWindow(
    programTitle = "Pdf Viewer",
    fileTitle = pdfTitle,
    icon = Res.drawable.pdf,
    initiallyVisible = true,
    topBarContent = listOf(
        { WindowTopBarMenus() },
        {
            val controller = view.controller.collectAsState()
            var draggable by remember(controller.value) { mutableStateOf(controller.value?.getCursorHandler()?.getDraggable() ?: true) }

            WindowTopBarButtons (
                { TopBarEntry(Res.drawable.grabbing, draggable) {
                    controller.value?.getCursorHandler()?.toggleDraggable()
                    draggable = controller.value?.getCursorHandler()?.getDraggable() ?: draggable
                } },
                { TopBarEntry(Res.drawable.zoomIn, false) { controller.value?.getZoomer()?.zoomIn() } },
                { TopBarEntry(Res.drawable.zoomOut, false) { controller.value?.getZoomer()?.zoomOut() } }
            )
    }),
    content = {
        view.getContent()
        // TODO is it possible for us to repeat a mouse click that happens when a HtmlView is not priority i.e. not in focus?


//                    // TODO Second, when we're resizing, our pointer events get captured by the IFRAME. We can fix this by running:
//                        // TODO document.body.shadowRoot.getElementById('pdfWindowContent').parentNode.style.pointerEvents = "none"
//                        // ^ while resizing
//                        // TODO document.body.shadowRoot.getElementById('pdfWindowContent').parentNode.style.pointerEvents = "auto"
//                        // ^ after resizing
//                    // TODO but then the question becomes, can we do this to ALL iframes while ANY window is being resized?
//                    // TODO I guess just iterate through all iframes and find they parent...
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
