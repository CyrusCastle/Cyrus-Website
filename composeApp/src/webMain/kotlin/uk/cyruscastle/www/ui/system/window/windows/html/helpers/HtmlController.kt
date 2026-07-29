package uk.cyruscastle.www.ui.system.window.windows.html.helpers

import org.w3c.dom.HTMLIFrameElement
import org.w3c.dom.events.Event
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsAny

@OptIn(ExperimentalWasmJsInterop::class)
external interface HtmlController : JsAny {
    fun getScroller(): HtmlScroller
    fun getHorizontalScroller(): HtmlScroller
    fun getZoomer(): HtmlZoomer
}

fun subscribeToHtmlController(
    iframe: HTMLIFrameElement,
    callback: (HtmlController) -> Unit
): () -> Unit {
    val listener: (Event) -> Unit = {
        getControllerOf(iframe)?.let(callback)
    }

    iframe.addEventListener("load", listener)
    getControllerOf(iframe)?.let(callback)

    return { iframe.removeEventListener("load", listener) }
}

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("(iframe) => iframe.contentWindow?.HtmlController ?? null")
private external fun getControllerOf(iframe: HTMLIFrameElement): HtmlController?

///////////////////
// MINOR HELPERS //
///////////////////

@OptIn(ExperimentalWasmJsInterop::class)
external interface HtmlCursorHandler : JsAny {
    fun getDraggable(): Boolean
    fun toggleDraggable()
}

@OptIn(ExperimentalWasmJsInterop::class)
external interface HtmlZoomer : JsAny {
    fun zoomIn()
    fun zoomOut()

    fun getZoom(): Double
    fun setZoom(amount: Double)
}