package uk.cyruscastle.www.ui.system.window.windows.email

import androidx.compose.foundation.background
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.alignCentre
import cyruswebsite.shared.generated.resources.alignJustify
import cyruswebsite.shared.generated.resources.alignLeft
import cyruswebsite.shared.generated.resources.alignRight
import cyruswebsite.shared.generated.resources.directory
import cyruswebsite.shared.generated.resources.mailSend
import cyruswebsite.shared.generated.resources.mailbox
import cyruswebsite.shared.generated.resources.textBold
import cyruswebsite.shared.generated.resources.textEmph
import cyruswebsite.shared.generated.resources.textUnderline
import kotlinx.coroutines.flow.MutableStateFlow
import uk.cyruscastle.www.ui.system.context.ContextMenuWrapper
import uk.cyruscastle.www.ui.system.context.RichTextTarget
import uk.cyruscastle.www.ui.system.scroll.ScrollBarType
import uk.cyruscastle.www.ui.system.scroll.ScrollableContainer
import uk.cyruscastle.www.ui.system.window.FacsimileWindow
import uk.cyruscastle.www.ui.system.window.UniqueWindow
import uk.cyruscastle.www.ui.system.window.topbar.TopBarEntry
import uk.cyruscastle.www.ui.system.window.topbar.TopBarSeparator
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarButtons
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarTextField
import uk.cyruscastle.www.ui.system.window.windows.text.printPage
import uk.cyruscastle.www.ui.system.window.windows.text.toggleFormatting
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js

@OptIn(ExperimentalMaterial3Api::class)
class EmailWindow(
    private val _addresseeBox: WindowTopBarTextField = WindowTopBarTextField("cyrusrobc@gmail.com", "To:", {}, Res.drawable.directory, false),
    private val _subjectBox: WindowTopBarTextField = WindowTopBarTextField("", "Subject", {}, null, false),
    private val _textState: MutableStateFlow<RichTextState> = MutableStateFlow(RichTextState())
) : UniqueWindow,  FacsimileWindow(
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
            var wrapperCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

            ContextMenuWrapper(RichTextTarget(state), wrapperCoordinates, false) {
                RichTextEditor(
                    state = state,
                    modifier = modifier
//                    .width(450.dp)
//                    .height((pageHeight * pageCount.toFloat()).dp)
                        .onGloballyPositioned { wrapperCoordinates = it }
                        .background(Color.White)
                        .onPreviewKeyEvent { event ->
                            if (event.type != KeyEventType.KeyDown) {
                                return@onPreviewKeyEvent false
                            }

                            if (!event.isCtrlPressed) {
                                return@onPreviewKeyEvent false
                            }

                            when (event.key) {
                                Key.B -> {
                                    _textState.value.toggleFormatting(SpanStyle(fontWeight = FontWeight.Bold))
                                }

                                Key.I -> {
                                    _textState.value.toggleFormatting(SpanStyle(fontStyle = FontStyle.Italic))
                                }

                                Key.U -> {
                                    _textState.value.toggleFormatting(SpanStyle(textDecoration = TextDecoration.Underline))
                                }

                                Key.P -> {
                                    printPage(_textState.value.toHtml())
                                } // Won't work because of browser's default print behaviour methinks
                                Key.Z -> {} // TODO undo
                                Key.Y -> {} // TODO redo
                                else -> {
                                    return@onPreviewKeyEvent false
                                }
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
            }
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