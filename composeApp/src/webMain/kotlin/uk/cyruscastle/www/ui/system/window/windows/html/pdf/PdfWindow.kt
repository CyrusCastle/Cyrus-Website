package uk.cyruscastle.www.ui.system.window.windows.html.pdf

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cyruswebsite.composeapp.generated.resources.Res
import cyruswebsite.composeapp.generated.resources.buttonPrint
import cyruswebsite.composeapp.generated.resources.buttonSave
import cyruswebsite.composeapp.generated.resources.computer
import cyruswebsite.composeapp.generated.resources.copyIcon
import cyruswebsite.composeapp.generated.resources.externalViewer
import cyruswebsite.composeapp.generated.resources.grabbing
import cyruswebsite.composeapp.generated.resources.internet
import cyruswebsite.composeapp.generated.resources.internetExplorerHTML
import cyruswebsite.composeapp.generated.resources.pdf
import cyruswebsite.composeapp.generated.resources.zoomIn
import cyruswebsite.composeapp.generated.resources.zoomOut
import org.jetbrains.compose.resources.painterResource
import uk.cyruscastle.www.model.PdfCitation
import uk.cyruscastle.www.ui.system.window.FacsimileWindow
import uk.cyruscastle.www.ui.system.window.topbar.TopBarEntry
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarButtons
import uk.cyruscastle.www.ui.system.window.windows.html.HtmlView
import uk.cyruscastle.www.ui.system.window.windows.html.getHost
import uk.cyruscastle.www.ui.system.window.windows.shortcuts.openShortcut
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js

@OptIn(ExperimentalWasmJsInterop::class, ExperimentalComposeUiApi::class)
open class PdfWindow(
    pdfTitle: String,
    pdfFilePath: String,
    pdfCitation: PdfCitation,
    val view: HtmlView = HtmlView(
        "${getHost()}/composeResources/cyruswebsite.composeapp.generated.resources/files/pdf/pdf.html?file=$pdfFilePath",
        "pdfWindowContent${pdfFilePath}"
    )
) : FacsimileWindow(
    programTitle = "Pdf Viewer",
    fileTitle = pdfTitle,
    icon = Res.drawable.pdf,
    initiallyVisible = true,
    topBarContent = listOf(
        {
            val controller = view.controller.collectAsState()
            var draggable by remember(controller.value) { mutableStateOf(controller.value?.getCursorHandler()?.getDraggable() ?: true) }

            WindowTopBarButtons (
                { TopBarEntry(Res.drawable.grabbing, draggable) {
                    controller.value?.getCursorHandler()?.toggleDraggable()
                    draggable = controller.value?.getCursorHandler()?.getDraggable() ?: draggable
                } },

                { TopBarEntry(null, false) { } },

                { TopBarEntry(Res.drawable.zoomIn, false) { controller.value?.getZoomer()?.zoomIn() } },
                { TopBarEntry(Res.drawable.zoomOut, false) { controller.value?.getZoomer()?.zoomOut() } },

                { TopBarEntry(null, false) { } },

                { TopBarEntry(Res.drawable.externalViewer, false) { openShortcut("${getHost()}/composeResources/cyruswebsite.composeapp.generated.resources/files/pdf/pdfs/$pdfFilePath") } },
                { TopBarEntry(Res.drawable.buttonSave, false) { downloadPdf("${getHost()}/composeResources/cyruswebsite.composeapp.generated.resources/files/pdf/pdfs/$pdfFilePath") } },
                { TopBarEntry(Res.drawable.buttonPrint, false) { printPdf("${getHost()}/composeResources/cyruswebsite.composeapp.generated.resources/files/pdf/pdfs/$pdfFilePath") } },
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
    },
    bottomBarContent = {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(Res.drawable.copyIcon), // TODO copy icon
                contentDescription = null,
                modifier = Modifier.size(15.dp).clickable {
                    copyText(pdfCitation.render())
                }
            )

            Spacer(Modifier.width(5.dp))

            Text(
                text = pdfCitation.renderWithItalics(),
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.width(300.dp),
            )

            Spacer(Modifier.width(5.dp))
            TopBarEntry(null, false) { }
            Spacer(Modifier.width(5.dp))

            Image(
                painter = painterResource(Res.drawable.computer),
                contentDescription = null,
                modifier = Modifier.size(15.dp)
            )

            Spacer(Modifier.width(5.dp))

            Text(
                text = "My Computer",
                modifier = Modifier.width(120.dp),
            )
        }
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

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("""
(url) => {
    const a = document.createElement('a');
    a.href = url;
    a.download = '';
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
}
""")
external fun downloadPdf(url: String)

@OptIn(ExperimentalWasmJsInterop::class)
fun printPdf(url: String) {
    js("""
        const iframe = document.createElement('iframe');
        iframe.style.position = 'fixed';
        iframe.style.right = '0';
        iframe.style.bottom = '0';
        iframe.style.width = '0';
        iframe.style.height = '0';
        iframe.style.border = '0';

        iframe.onload = () => {
            try {
                iframe.contentWindow.focus();
                iframe.contentWindow.print();
            } finally {
                setTimeout(() => iframe.remove(), 1000);
            }
        };

        iframe.src = url;
        document.body.appendChild(iframe);
    """)
}

@OptIn(ExperimentalWasmJsInterop::class)
fun copyText(text: String) {
    js("navigator.clipboard.writeText(text);")
}