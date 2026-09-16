package uk.cyruscastle.www.helpers.html

import org.w3c.dom.HTMLIFrameElement
import org.w3c.dom.events.Event
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsAny
import kotlin.js.JsArray

@OptIn(ExperimentalWasmJsInterop::class)
external interface FocusedElement : JsAny {
    val tag: String
    val type: String?
    val text: String
    val href: String?
    val id: String?
    val editable: Boolean
}
// TODO if we're >=1 deep into navigating, the left primary button should say "Back"
@OptIn(ExperimentalWasmJsInterop::class)
external interface HtmlMobileController : JsAny {
    fun list(): JsArray<FocusedElement>
    fun next(): FocusedElement?
    fun prev(): FocusedElement?
    fun current(): FocusedElement?
    fun focusIndex(index: Int): FocusedElement?
    fun activate(): Boolean
    fun setText(text: String): Boolean
    fun blur(): Int
    fun restore(): FocusedElement?
}

fun subscribeToHtmlMobileController(
    iframe: HTMLIFrameElement,
    callback: (HtmlMobileController) -> Unit
): () -> Unit {
    val listener: (Event) -> Unit = {
        getControllerOf(iframe)?.let(callback)
    }

    iframe.addEventListener("load", listener)
    getControllerOf(iframe)?.let(callback)

    return { iframe.removeEventListener("load", listener) }
}

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("(iframe) => iframe.contentWindow?.HtmlMobileController ?? null")
private external fun getControllerOf(iframe: HTMLIFrameElement): HtmlMobileController?