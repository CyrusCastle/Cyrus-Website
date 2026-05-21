package uk.cyruscastle.www.ui.system.window.windows.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import cyruswebsite.composeapp.generated.resources.Res
import cyruswebsite.composeapp.generated.resources.alignCentre
import cyruswebsite.composeapp.generated.resources.alignJustify
import cyruswebsite.composeapp.generated.resources.alignLeft
import cyruswebsite.composeapp.generated.resources.alignRight
import cyruswebsite.composeapp.generated.resources.buttonFind
import cyruswebsite.composeapp.generated.resources.buttonNew
import cyruswebsite.composeapp.generated.resources.buttonOpen
import cyruswebsite.composeapp.generated.resources.buttonPrint
import cyruswebsite.composeapp.generated.resources.buttonSave
import cyruswebsite.composeapp.generated.resources.buttonSpellcheck
import cyruswebsite.composeapp.generated.resources.textBold
import cyruswebsite.composeapp.generated.resources.textEmph
import cyruswebsite.composeapp.generated.resources.textUnderline
import cyruswebsite.composeapp.generated.resources.wordpad
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.download
import io.github.vinceglb.filekit.readString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import uk.cyruscastle.www.ui.system.scroll.ScrollBarType
import uk.cyruscastle.www.ui.system.scroll.ScrollableContainer
import uk.cyruscastle.www.ui.system.window.FacsimileWindow
import uk.cyruscastle.www.ui.system.window.topbar.TopBarEntry
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarButtons
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarDefaultMenus
import uk.cyruscastle.www.ui.theme.ColorPalette
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js

@OptIn(ExperimentalMaterial3Api::class, ExperimentalWasmJsInterop::class)
open class WordpadWindow (
    val title: String? = null,
    val startingText: String? = null,
    private val _textState: MutableStateFlow<RichTextState> = MutableStateFlow(RichTextState())
) : FacsimileWindow(
    programTitle = "Wordpad",
    fileTitle = title,
    icon = Res.drawable.wordpad,
    initiallyVisible = true,
    topBarContent = listOf(
        { WindowTopBarDefaultMenus() },
        { WindowTopBarButtons (
            { TopBarEntry(Res.drawable.buttonNew, false) {
                _textState.value.setMarkdown("")
            } },
            { TopBarEntry(Res.drawable.buttonOpen, false) {
                CoroutineScope(Dispatchers.Default).launch {
                    val file = FileKit.openFilePicker()
                    _textState.value.setMarkdown(file?.readString() ?: "Sorry, I can't load that! Try a text file, markdown is supported.")
                }
            } },
            { TopBarEntry(Res.drawable.buttonSave, false) {
                CoroutineScope(Dispatchers.Default).launch {
                    FileKit.download(bytes = _textState.value.toMarkdown().encodeToByteArray(), fileName = "file.md")
                }
            } },

            { TopBarEntry(null, false) { } },

            { TopBarEntry(Res.drawable.buttonPrint, false) {
                printPage(_textState.value.toHtml())
            } },
            { TopBarEntry(Res.drawable.buttonFind, false) { } },
            { TopBarEntry(Res.drawable.buttonSpellcheck, false) { } },

            { TopBarEntry(null) { } },

            // cut, copy, paste

//            { TopBarEntry(null) { } },

            // Bold, Emph, Underline
            {
                TopBarEntry(
                    drawable = Res.drawable.textBold,
                    isIntruding = _textState.value.currentSpanStyle.fontWeight == FontWeight.Bold,
                    modifier = Modifier.focusProperties {
                        canFocus = false
                    }
                ) {
                    _textState.value.toggleFormatting(SpanStyle(fontWeight = FontWeight.Bold))
                }
            },
            {
                TopBarEntry(
                    drawable = Res.drawable.textEmph,
                    isIntruding = _textState.value.currentSpanStyle.fontStyle == FontStyle.Italic,
                    modifier = Modifier.focusProperties {
                        canFocus = false
                    }
                ) {
                    _textState.value.toggleFormatting(SpanStyle(fontStyle = FontStyle.Italic))
                }
            },
            {
                TopBarEntry(
                    drawable = Res.drawable.textUnderline,
                    isIntruding = _textState.value.currentSpanStyle.textDecoration == TextDecoration.Underline,
                    modifier = Modifier.focusProperties {
                        canFocus = false
                    }
                ) {
                    _textState.value.toggleFormatting(SpanStyle(textDecoration = TextDecoration.Underline))
                }
            },

            { TopBarEntry(null) { } },

            // Aligning text
            {
                TopBarEntry(
                    drawable = Res.drawable.alignLeft,
                    isIntruding = _textState.value.currentParagraphStyle.textAlign == TextAlign.Left
                ) {
                    _textState.value.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Left))
                }
            },
            {
                TopBarEntry(
                    drawable = Res.drawable.alignCentre,
                    isIntruding = _textState.value.currentParagraphStyle.textAlign == TextAlign.Center
                ) {
                    _textState.value.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Center))
                }
            },
            {
                TopBarEntry(
                    drawable = Res.drawable.alignRight,
                    isIntruding = _textState.value.currentParagraphStyle.textAlign == TextAlign.Right
                ) {
                    _textState.value.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Right))
                }
            },
            {
                TopBarEntry(
                    drawable = Res.drawable.alignJustify,
                    isIntruding = _textState.value.currentParagraphStyle.textAlign == TextAlign.Justify
                ) {
                    _textState.value.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Justify))
                }
            },

//            { TopBarEntry(null) { } },

//            { TopBarEntry(Res.drawable.undo, false) { } },
//            { TopBarEntry(Res.drawable.redo, false) { } },
        )}
    ),
    content = {
        val state = _textState.collectAsState()
        var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
        var pageCount by remember { mutableIntStateOf(1) }
        var pageHeight by remember { mutableFloatStateOf(800.dp.value) }

        LaunchedEffect(Unit){
            state.value.setMarkdown(startingText ?: "")
        }

        ScrollableContainer(listOf(ScrollBarType.VERTICAL), behindContentColor = ColorPalette.WINDOW_CONTAINER_BACKGROUND){ modifier ->
            Row(horizontalArrangement = Arrangement.Center, modifier = modifier.fillMaxSize()) {
                RichTextEditor(
                    state = state.value,
                    modifier = Modifier
                        .width(450.dp)
                        .height((pageHeight * pageCount.toFloat()).dp)
                        .background(Color.White)
                        .onPreviewKeyEvent { event ->
                            if (event.type != KeyEventType.KeyDown){
                                return@onPreviewKeyEvent false
                            }

                            if (!event.isCtrlPressed){
                                return@onPreviewKeyEvent false
                            }

                            when (event.key){
                                Key.B -> { _textState.value.toggleFormatting(SpanStyle(fontWeight = FontWeight.Bold)) }
                                Key.I -> { _textState.value.toggleFormatting(SpanStyle(fontStyle = FontStyle.Italic)) }
                                Key.U -> { _textState.value.toggleFormatting(SpanStyle(textDecoration = TextDecoration.Underline)) }
                                Key.P -> { printPage(_textState.value.toHtml()) } // Won't work because of browser's default print behaviour methinks
                                Key.Z -> {  } // TODO undo
                                Key.Y -> {  } // TODO redo
                                else -> { return@onPreviewKeyEvent false }
                            }

                            return@onPreviewKeyEvent true
                        }
                        .drawWithContent {
                            drawContent()

                            val layout = layoutResult ?: return@drawWithContent

                            val lineCount = layout.lineCount
                            val linesPerPage = 20
                            var newPageCount = 1

                            // Remind us of page height
                            val lineHeight = layout.getLineBottom(0) - layout.getLineTop(0)
                            pageHeight = lineHeight * linesPerPage

                            for (i in linesPerPage until lineCount step linesPerPage) {
                                val y = layout.getLineBottom(i - 1)

                                drawLine(
                                    ColorPalette.WINDOW_CONTAINER_BACKGROUND,
                                    start = Offset(0f, y - 7.dp.value),
                                    end = Offset(size.width, y - 7.dp.value),
                                    strokeWidth = 5f
                                )
                                newPageCount++
                            }

                            pageCount = newPageCount
                        },

                    onTextLayout = { result ->
                        layoutResult = result
                    },

                    colors = RichTextEditorDefaults.richTextEditorColors(
                        containerColor = Color.Transparent,//Color.White, // We use transparent here, and a .background above, so that we don't interfere with the drawBehind of the "pagination"
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = Color.Black
                    )
                )
            }
        }


//        TextField(
//            textValue.value,
//            {
//                _textValue.value = it
//            },
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(scrollState),
//
//            singleLine = false,
//            maxLines = Int.MAX_VALUE,
//
//            colors = TextFieldDefaults.colors().copy(
//                focusedContainerColor = Color.White,
//                unfocusedContainerColor = Color.White,
//                focusedIndicatorColor = Color.Transparent,
//                unfocusedIndicatorColor = Color.Transparent,
//                disabledIndicatorColor = Color.Transparent,
//                cursorColor = Color.Unspecified
//            )
//        )
    },
    bottomBarContent = {
        val textValue = _textState.collectAsState()

        // Line / col counting
        val pos = textValue.value.selection.start
        val lines = textValue.value.toText().split("\n")

        var runningLength = 0
        var lineIndex = 0
        var columnIndex = 0

        for ((index, line) in lines.withIndex()) {
            val lineLengthWithNewline = line.length + 1
            if (pos < runningLength + lineLengthWithNewline) {
                lineIndex = index
                columnIndex = pos - runningLength
                break
            }
            runningLength += lineLengthWithNewline
        }

        // Word counting
        val words = textValue.value.toText()
            .trim()
            .split(Regex("\\s+"))
            .filter { it.isNotEmpty() }
            .size
        val chars = textValue.value.toText().count { it != '\n' }

        Row {
            Text(
                text = "Ln $lineIndex, Col $columnIndex",
                modifier = Modifier.width(120.dp),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.width(5.dp))
            TopBarEntry(null, false) { }
            Spacer(Modifier.width(5.dp))

            Text(
                text = "$words words, $chars characters",
//                modifier = Modifier.width(120.dp)
            )
        }
    }
)

private fun RichTextState.toggleFormatting(spanStyle: SpanStyle) {
    if (this.selection.collapsed){
        this.toggleSpanStyle(spanStyle)
    }else {
        if (!this.currentSpanStyle.matches(spanStyle)){
            this.addSpanStyle(
                spanStyle = spanStyle,
                textRange = this.selection
            )
        }else {
            this.removeSpanStyle(
                spanStyle = spanStyle,
                textRange = this.selection
            )
        }
    }
}

private fun SpanStyle.matches(other: SpanStyle): Boolean {
    if (other.fontWeight != null && fontWeight != other.fontWeight) return false
    if (other.fontStyle != null && fontStyle != other.fontStyle) return false
    if (other.textDecoration != null && textDecoration != other.textDecoration) return false
    if (other.color != Color.Unspecified && color != other.color) return false
    return true
}

@OptIn(ExperimentalWasmJsInterop::class)
private fun printPage(html: String){
    js("""
    const iframe = document.createElement('iframe');
    iframe.style.position = 'fixed';
    iframe.style.right = '0';
    iframe.style.bottom = '0';
    iframe.style.width = '0';
    iframe.style.height = '0';
    iframe.style.border = '0';
    
    document.body.appendChild(iframe);
    
    iframe.onload = function () {
        iframe.contentWindow.focus();
        iframe.contentWindow.print();
    
        // Cleanup after printing
        setTimeout(() => {
            document.body.removeChild(iframe);
        }, 1000);
    };
    
    
    iframe.srcdoc = html;
    """)
}