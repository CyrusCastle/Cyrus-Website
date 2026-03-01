package uk.cyruscastle.www.ui.system.window.windows.html.helpers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@OptIn(ExperimentalWasmJsInterop::class)
external interface NewHtmlScroller : JsAny {
    fun getCurrentScroll(): Double
    fun getMaxScroll(): Double
    fun scrollBy(amount: Double)

    fun addScrollListener(callback: () -> Unit)
    fun removeScrollListener(callback: () -> Unit)
}

class HtmlScrollerState(private val scroller: NewHtmlScroller?) {
    var currentScroll by mutableStateOf(0)
        private set

    var maxScroll by mutableStateOf(0)
        private set

    fun attach() {
        scroller?.addScrollListener(this::update)
        update()
    }

    fun detach(){
        scroller?.removeScrollListener(this::update)
    }

    fun update() {
        currentScroll = scroller?.getCurrentScroll()?.toInt() ?: 0
        maxScroll = scroller?.getMaxScroll()?.toInt() ?: 0
    }

    fun scrollBy(amount: Double) {
        scroller?.scrollBy(amount)
    }
}

@Composable
fun rememberHtmlScrollState(scroller: NewHtmlScroller?): HtmlScrollerState {
    val state = remember(scroller) {
        HtmlScrollerState(
            scroller
        )
    }

    DisposableEffect(scroller) {
        state.attach()

        onDispose {
            state.detach()
        }
    }

    return state
}