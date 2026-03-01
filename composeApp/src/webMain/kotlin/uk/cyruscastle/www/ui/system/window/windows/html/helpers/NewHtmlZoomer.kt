package uk.cyruscastle.www.ui.system.window.windows.html.helpers

@OptIn(ExperimentalWasmJsInterop::class)
external interface NewHtmlZoomer : JsAny {
    fun zoomIn()
    fun zoomOut()

    fun getZoom(): Double
    fun setZoom(amount: Double)
}