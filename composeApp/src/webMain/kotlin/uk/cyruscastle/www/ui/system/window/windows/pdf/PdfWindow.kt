package uk.cyruscastle.www.ui.system.window.windows.pdf

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cyruswebsite.composeapp.generated.resources.Res
import cyruswebsite.composeapp.generated.resources.buttonPrint
import cyruswebsite.composeapp.generated.resources.buttonSave
import cyruswebsite.composeapp.generated.resources.computer
import cyruswebsite.composeapp.generated.resources.copyIcon
import cyruswebsite.composeapp.generated.resources.externalViewer
import cyruswebsite.composeapp.generated.resources.grabbing
import cyruswebsite.composeapp.generated.resources.pdf
import cyruswebsite.composeapp.generated.resources.selectText
import cyruswebsite.composeapp.generated.resources.zoomIn
import cyruswebsite.composeapp.generated.resources.zoomOut
import dev.nucleusframework.pdfium.PdfPage
import dev.nucleusframework.pdfium.PdfReaderState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.jetbrains.compose.resources.painterResource
import uk.cyruscastle.www.model.PdfCitation
import uk.cyruscastle.www.ui.system.scroll.ScrollBarType
import uk.cyruscastle.www.ui.system.scroll.ScrollableLazyColumn
import uk.cyruscastle.www.ui.system.window.FacsimileWindow
import uk.cyruscastle.www.ui.system.window.topbar.TopBarEntry
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarButtons
import uk.cyruscastle.www.ui.system.window.windows.html.getHost
import uk.cyruscastle.www.ui.system.window.windows.shortcuts.openShortcut
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js

@OptIn(ExperimentalWasmJsInterop::class, ExperimentalComposeUiApi::class)
open class PdfWindow(
    pdfTitle: String,
    pdfFilePath: String,
    pdfCitation: PdfCitation,
    val state: PdfReaderState = PdfReaderState(PdfReaderState.DEFAULT_CACHE_BYTES, PdfReaderState.DEFAULT_THUMBNAIL_CACHE_BYTES),
    val listState: LazyListState = LazyListState(),
    val pdfScope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    val screenState: ReaderScreenState = ReaderScreenState(state, listState, pdfScope)
) : FacsimileWindow(
    programTitle = "Pdf Viewer",
    fileTitle = pdfTitle,
    icon = Res.drawable.pdf,
    initiallyVisible = true,
    topBarContent = listOf(
        {
            WindowTopBarButtons (
                { TopBarEntry(Res.drawable.selectText, screenState.mouseOption == MouseOption.SELECT_TEXT) {
                    screenState.setMouseOption(MouseOption.SELECT_TEXT)
                } },

                { TopBarEntry(Res.drawable.grabbing, screenState.mouseOption == MouseOption.GRAB_TO_MOVE) {
                    screenState.setMouseOption(MouseOption.GRAB_TO_MOVE)
                } },

                { TopBarEntry(null, false) { } },

                { TopBarEntry(Res.drawable.zoomIn, false) { state.renderScale = (state.renderScale + 0.25f).coerceAtMost(4f) } },
                { TopBarEntry(Res.drawable.zoomOut, false) { state.renderScale = (state.renderScale - 0.25f).coerceAtLeast(0.1f) } },

                { TopBarEntry(null, false) { } },

                { TopBarEntry(Res.drawable.externalViewer, false) { openShortcut("${getHost()}/composeResources/cyruswebsite.composeapp.generated.resources/files/pdf/pdfs/$pdfFilePath") } },
                { TopBarEntry(Res.drawable.buttonSave, false) { downloadPdf("${getHost()}/composeResources/cyruswebsite.composeapp.generated.resources/files/pdf/pdfs/$pdfFilePath") } },
                { TopBarEntry(Res.drawable.buttonPrint, false) { printPdf("${getHost()}/composeResources/cyruswebsite.composeapp.generated.resources/files/pdf/pdfs/$pdfFilePath") } },
            )
    }),
    content = {
        var bytes by remember { mutableStateOf<ByteArray?>(null) }

        LaunchedEffect(Unit){
            bytes = Res.readBytes("files/pdf/pdfs/$pdfFilePath")
        }

        LaunchedEffect(bytes) { bytes?.let { state.open(it) } }

        PdfReader(screenState) // TODO current surface text selection is one line off
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
                overflow = TextOverflow.Ellipsis, // TODO sometimes this works sometimes it doesn't
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

            Spacer(Modifier.width(5.dp))
            TopBarEntry(null, false) { }
            Spacer(Modifier.width(5.dp))

            Spacer(Modifier.width(5.dp))

            Text(text = "${screenState.currentPage + 1} / ${state.pageCount}")
        }
    }
)

////////////////
// PDF Viewer //
////////////////

@Composable
fun PdfReader(
    state: ReaderScreenState,
) {
    ScrollableLazyColumn(
        types = ScrollBarType.all(),
        verticalListState = state.mainListState,
        draggable = state.mouseOption == MouseOption.GRAB_TO_MOVE
    ) { modifier ->
        BoxWithConstraints(Modifier.weight(1f)) {
            val contentSlot = (maxWidth - 24.dp * 2).coerceAtLeast(120.dp)
            val pageWidth = ((contentSlot * state.reader.renderScale) - 24.dp).coerceAtLeast(80.dp)

            LazyColumn(
                state = state.mainListState,
                modifier = modifier.width(pageWidth).align(Alignment.Center),
                contentPadding = PaddingValues(vertical = 20.dp, horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(
                    items = state.spreadEntries,
                    key = { it.first },
                ) { entry ->
                    Column(Modifier.wrapContentWidth(Alignment.CenterHorizontally)) {
                        Text("Page ${entry.first + 1}", style = MaterialTheme.typography.labelMedium)

                        Box(Modifier.width(pageWidth)) {
                            PdfPage(
                                state = state.reader,
                                pageIndex = entry.first,
                                modifier = Modifier.fillMaxWidth(),
                                background = Color.White,
                                selectableText = state.mouseOption == MouseOption.SELECT_TEXT,
                            )
                        }
                    }
                }
            }
        }
    }
}


//
//
//

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