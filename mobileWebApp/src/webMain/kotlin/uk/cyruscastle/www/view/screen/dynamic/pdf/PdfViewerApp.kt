package uk.cyruscastle.www.view.screen.dynamic.pdf

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.fromKeyword
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.phoneBooks
import dev.nucleusframework.pdfium.PdfPage
import dev.nucleusframework.pdfium.PdfReaderState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.cyruscastle.www.controller.Navigator
import uk.cyruscastle.www.model.Control
import uk.cyruscastle.www.view.screen.App
import uk.cyruscastle.www.view.screen.ScreenScaffold

open class PdfViewerApp(
    pdfTitle: String,
    pdfFilePath: String,
    val state: PdfReaderState = PdfReaderState(PdfReaderState.DEFAULT_CACHE_BYTES, PdfReaderState.DEFAULT_THUMBNAIL_CACHE_BYTES),
    val listState: LazyListState = LazyListState(),
    val pdfScope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    val screenState: ReaderScreenState = ReaderScreenState(state, listState, pdfScope)
) : App(
    name = pdfTitle,
    icon = Res.drawable.phoneBooks,
    content = {
        var scale by remember { mutableFloatStateOf(1f) }
        LaunchedEffect(scale){
            state.renderScale = scale
        }

        val scrollBy = 40f

        val horizontalScroll = rememberScrollState()

        ScreenScaffold(
            leftButtonLabel = "Zoom (${scale * 50}%)",
            rightButtonLabel = "Exit",
            onControl = { control ->
                when (control) {
                    Control.PRIMARY_RIGHT -> Navigator.pop()
                    Control.PRIMARY_LEFT -> {
                        if (scale >= 4f){
                            scale = 0.5f
                        } else {
                            scale += 0.5f
                        }

                        true
                    }

                    Control.Up -> { pdfScope.launch { listState.scrollBy(-scrollBy) }; true }
                    Control.Down -> { pdfScope.launch { listState.scrollBy(scrollBy) }; true }
                    Control.Left -> { pdfScope.launch { horizontalScroll.scrollBy(-scrollBy) }; true }
                    Control.Right -> { pdfScope.launch { horizontalScroll.scrollBy(scrollBy) }; true }

                    else -> false
                }
            }
        ) {
            var bytes by remember { mutableStateOf<ByteArray?>(null) }

            LaunchedEffect(Unit){
                bytes = Res.readBytes("files/pdf/pdfs/$pdfFilePath")
            }

            LaunchedEffect(bytes) { bytes?.let { state.open(it) } }

            PdfReader(screenState, horizontalScroll)
        }
    }
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PdfReader(
    state: ReaderScreenState,
    horizontalScroll: ScrollState
) {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val contentSlot = (maxWidth - 24.dp * 2).coerceAtLeast(120.dp)
        val pageWidth = ((contentSlot * state.reader.renderScale) - 24.dp).coerceAtLeast(80.dp)

        Box(Modifier.fillMaxSize().horizontalScroll(horizontalScroll)){
            LazyColumn(
                state = state.mainListState,
                modifier = Modifier.width(pageWidth).align(Alignment.Center),
                contentPadding = PaddingValues(vertical = 20.dp, horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(
                    items = state.spreadEntries,
                    key = { it.first },
                ) { entry ->
                    Column(Modifier.wrapContentWidth(Alignment.CenterHorizontally, unbounded = false)) {
                        Text("Page ${entry.first + 1}", style = MaterialTheme.typography.labelMedium)

                        Box(Modifier.width(pageWidth)) {
                            PdfPage(
                                state = state.reader,
                                pageIndex = entry.first,
                                modifier = Modifier.fillMaxWidth().then(if (state.mouseOption == MouseOption.SELECT_TEXT) Modifier.pointerHoverIcon(PointerIcon.fromKeyword("text")) else Modifier),
                                background = Color.White,
                                selectableText = state.mouseOption == MouseOption.SELECT_TEXT,
                            )
                        }
                    }
                }
            }
        }
    }

//    ScrollableLazyColumn(
//        types = ScrollBarType.all(),
//        verticalListState = state.mainListState,
//        draggable = state.mouseOption == uk.cyruscastle.www.ui.system.window.windows.pdf.MouseOption.GRAB_TO_MOVE
//    ) { modifier ->
//
//    }
}