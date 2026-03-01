package uk.cyruscastle.www.ui.system.window.windows.html.helpers

@OptIn(ExperimentalWasmJsInterop::class)
external interface NewHtmlCursorHandler : JsAny {
    fun getDraggable(): Boolean
    fun toggleDraggable()
}