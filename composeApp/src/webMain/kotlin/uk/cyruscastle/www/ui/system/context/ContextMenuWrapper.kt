package uk.cyruscastle.www.ui.system.context

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.contextmenu.data.TextContextMenuSession
import androidx.compose.foundation.text.contextmenu.provider.LocalTextContextMenuDropdownProvider
import androidx.compose.foundation.text.contextmenu.provider.LocalTextContextMenuToolbarProvider
import androidx.compose.foundation.text.contextmenu.provider.TextContextMenuDataProvider
import androidx.compose.foundation.text.contextmenu.provider.TextContextMenuProvider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import uk.cyruscastle.www.ui.extensions.modifier.intrudeExtrudeBorder
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarMenuSubItem
import uk.cyruscastle.www.ui.theme.ColorPalette
import kotlin.coroutines.resume
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsAny
import kotlin.js.JsString
import kotlin.js.Promise
import kotlin.js.js

@Composable
fun ContextMenuWrapper(
    state: MutableState<TextFieldValue>,
    wrapperCoordinates: LayoutCoordinates?,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    var menuRequest by remember { mutableStateOf<Pair<Offset, TextContextMenuSession>?>(null) }

    fun selectedRange() = state.value.selection
    fun selectedText() = state.value.text.substring(selectedRange().min, selectedRange().max)

    fun doCut() {
        val range = selectedRange()
        if (range.collapsed) return
        val cutText = selectedText()
        val newText = state.value.text.removeRange(range.min, range.max)
        state.value = TextFieldValue(newText, TextRange(range.min))
        scope.launch { writeClipboardText(cutText) }
    }

    fun doCopy() {
        val range = selectedRange()
        if (range.collapsed) return
        scope.launch { writeClipboardText(selectedText()) }
    }

    fun doPaste() {
        scope.launch {
            val pasted = readClipboardText() ?: return@launch
            val range = selectedRange()
            val newText = state.value.text.replaceRange(range.min, range.max, pasted)
            state.value = TextFieldValue(newText, TextRange(range.min + pasted.length))
        }
    }

    fun doSelectAll() {
        state.value = state.value.copy(selection = TextRange(0, state.value.text.length))
    }

    val provider = remember(wrapperCoordinates) {
        object : TextContextMenuProvider {
            override suspend fun showTextContextMenu(dataProvider: TextContextMenuDataProvider) {
                suspendCancellableCoroutine { continuation ->
                    val session = object : TextContextMenuSession {
                        override fun close() {
                            menuRequest = null
                            if (continuation.isActive) continuation.resume(Unit)
                        }
                    }

                    val point = wrapperCoordinates?.localToRoot(dataProvider.position(wrapperCoordinates)) ?: Offset.Zero
                    menuRequest = point to session

                    continuation.invokeOnCancellation { menuRequest = null }
                }
            }
        }
    }

    CompositionLocalProvider(
        LocalTextContextMenuDropdownProvider provides provider,
        LocalTextContextMenuToolbarProvider provides provider
    ) {
        content()
    }

    menuRequest?.let { (point, session) ->
        Popup(
            popupPositionProvider = PointPositionProvider(point),
            onDismissRequest = { session.close() },
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize(Alignment.TopStart, true)
                    .width(150.dp)
                    .background(ColorPalette.WINDOW_BODY_BACKGROUND)
                    .intrudeExtrudeBorder(RectangleShape, isIntruding = false)
            ) {
                var canPaste by remember(point) { mutableStateOf(true) }

                LaunchedEffect(point){
                    val clipboard = readClipboardText()
                    canPaste = (clipboard != null && clipboard != "")
                }

                Spacer(Modifier.height(5.dp))
                WindowTopBarMenuSubItem("Cut", !selectedRange().collapsed) { doCut(); session.close() }
                WindowTopBarMenuSubItem("Copy", !selectedRange().collapsed) { doCopy(); session.close() }
                WindowTopBarMenuSubItem("Paste", canPaste) { doPaste(); session.close() }
                WindowTopBarMenuSubItem("Select All", true) { doSelectAll(); session.close() }
                Spacer(Modifier.height(5.dp))
            }
        }
    }
}

private class PointPositionProvider(private val point: Offset) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val x = point.x.toInt().coerceIn(0, (windowSize.width - popupContentSize.width).coerceAtLeast(0))
        val y = point.y.toInt().coerceIn(0, (windowSize.height - popupContentSize.height).coerceAtLeast(0))
        return IntOffset(x, y)
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
private fun readClipboardTextRaw(): Promise<JsString> =
    js("window.navigator.clipboard.readText()")

@OptIn(ExperimentalWasmJsInterop::class)
private fun writeClipboardTextRaw(text: String): Promise<JsAny?> =
    js("window.navigator.clipboard.writeText(text)")

@OptIn(ExperimentalWasmJsInterop::class)
suspend fun readClipboardText(): String? = try {
    readClipboardTextRaw().await().toString()
} catch (e: Throwable) {
    null
}

@OptIn(ExperimentalWasmJsInterop::class)
suspend fun writeClipboardText(text: String) {
    try {
        writeClipboardTextRaw(text).await()
    } catch (e: Throwable) {

    }
}