package uk.cyruscastle.www.ui.system.window.windows.email

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import cyruswebsite.composeapp.generated.resources.directory
import cyruswebsite.composeapp.generated.resources.mailSend
import cyruswebsite.composeapp.generated.resources.mailbox
import cyruswebsite.composeapp.generated.resources.textBold
import cyruswebsite.composeapp.generated.resources.textEmph
import cyruswebsite.composeapp.generated.resources.textUnderline
import kotlinx.coroutines.flow.MutableStateFlow
import uk.cyruscastle.www.ui.system.scroll.ScrollBarType
import uk.cyruscastle.www.ui.system.scroll.ScrollableContainer
import uk.cyruscastle.www.ui.system.window.FacsimileWindow
import uk.cyruscastle.www.ui.system.window.topbar.TopBarEntry
import uk.cyruscastle.www.ui.system.window.topbar.TopBarSeparator
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarButtons
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarDefaultMenus
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarMenuItem
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarMenuSubItemEntry
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarMenus
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarTextField
import uk.cyruscastle.www.ui.system.window.windows.text.printPage
import uk.cyruscastle.www.ui.system.window.windows.text.toggleFormatting
import uk.cyruscastle.www.ui.theme.ColorPalette
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js

@OptIn(ExperimentalMaterial3Api::class)
class EmailWindow(
    private val _addresseeBox: WindowTopBarTextField = WindowTopBarTextField("cyrusrobc@gmail.com", "To:", {}, Res.drawable.directory, false),
    private val _subjectBox: WindowTopBarTextField = WindowTopBarTextField("", "Subject", {}, null, false),
    private val _textState: MutableStateFlow<RichTextState> = MutableStateFlow(RichTextState())
) : FacsimileWindow(
    programTitle = "E-Mail",
    icon = Res.drawable.mailbox,
    initiallyVisible = true,
    topBarContent = listOf(
        { _addresseeBox() },
        { _subjectBox() },
        {
            WindowTopBarButtons (
                {
                    TopBarEntry(Res.drawable.mailSend, false) {
                        sendEmail(_addresseeBox.getText(), _subjectBox.getText(), _textState.value.toMarkdown())
                    }
                },

                { TopBarSeparator() },

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

                { TopBarSeparator() },

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
                }
            )
        }
    ),
    content = {
        val state by _textState.collectAsState()

        ScrollableContainer(ScrollBarType.all(), behindContentColor = Color.White) { modifier ->
            RichTextEditor(
                state = state,
                modifier = modifier
//                    .width(450.dp)
//                    .height((pageHeight * pageCount.toFloat()).dp)
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
                    },

                colors = RichTextEditorDefaults.richTextEditorColors(
                    containerColor = Color.Transparent,//Color.White, // We use transparent here, and a .background above, so that we don't interfere with the drawBehind of the "pagination"
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                )
            )
//            TextField(
//                text,
//                { _textState.value = it },
//                modifier = modifier,
//
//                singleLine = false,
//                maxLines = Int.MAX_VALUE,
//
//                colors = TextFieldDefaults.colors().copy(
//                    focusedContainerColor = Color.White,
//                    unfocusedContainerColor = Color.White,
//                    focusedIndicatorColor = Color.Transparent,
//                    unfocusedIndicatorColor = Color.Transparent,
//                    disabledIndicatorColor = Color.Transparent,
//                    cursorColor = Color.Unspecified
//                )
//            )
        }
    }
)

@OptIn(ExperimentalWasmJsInterop::class)
fun sendEmail(addressee: String, subject: String, content: String){
    js(
    $$"""
    const url = `mailto:${encodeURIComponent(addressee)}?subject=${encodeURIComponent(subject)}&body=${encodeURIComponent(content)}`;
    console.log(subject);
    console.log(url);
    window.open(url, '_blank');
    """)
}